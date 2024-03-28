import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  TemplateRef,
} from "@angular/core";
import { SubadminService } from "./../../../_services/subadmin-service";
import { Subadmin } from "./../../../model/subadmin";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { Subscription } from "rxjs";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { ManageuserService } from "../../../_services/manageuser-service";
import { LoaderService } from "../../../_services/loader.service";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-subadmin-list",
  templateUrl: "./subadmin-list.component.html",
  styleUrls: ["./subadmin-list.component.scss"],
})
export class SubadminListComponent implements OnDestroy, OnInit {
  modalRef: BsModalRef;
  private activeRoleList: any[] = [];

  // Get datatble configuration -- start
  reorderable = CONFIGCONSTANTS.datatableConfig.reorderable;
  scrollbarH = CONFIGCONSTANTS.datatableConfig.scrollbarH;
  serverSorting = CONFIGCONSTANTS.datatableConfig.serverSorting;
  serverPaging = CONFIGCONSTANTS.datatableConfig.serverPaging;
  piningRight = CONFIGCONSTANTS.datatableConfig.piningRight;
  headerHeight = CONFIGCONSTANTS.datatableConfig.headerHeight;
  footerHeight = CONFIGCONSTANTS.datatableConfig.footerHeight;
  rowHeight = CONFIGCONSTANTS.datatableConfig.rowHeight;
  limitList: any[] = CONFIGCONSTANTS.datatableConfig.limitList;
  ColumnMode = ColumnMode;
  totalReords = CONFIGCONSTANTS.datatableConfig.page.totalReords;
  pageNumber = CONFIGCONSTANTS.datatableConfig.page.pageNumber;
  size = CONFIGCONSTANTS.datatableConfig.page.size;
  dtMessages = CONFIGCONSTANTS.datatableConfig.dtMessages;
  @ViewChild(DatatableComponent, { static: false })
  datatable: DatatableComponent;
  subadminList: any[] = [];
  sortParam = "";
  sortOrder = "";
  // Get datatble configuration -- end

  submitted = false;
  newpass: string;
  filter = {
    status: "",
    role: null,
    fullname: "",
    email: "",
  };
  departmentList = [];
  locationList = [];
  confirmpass: string;
  private subadmnID: number;
  private changeStatusType: string;
  private changedStatus: string;
  roleList: any[] = [];
  permissions: any[];
  momentDateTime24Format: string;
  conf_pass_match: String = "";
  loadingIndicator = false;
  constructor(
    private subadminService: SubadminService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService,
    private manageuserService: ManageuserService
  ) {
    // For get filter, sorting, pagenumber and size in storage
    this.filter = this.filterService.getState("subadminFilter", this.filter);
    // this.sortParam = this.filterService.getSingleState('subadminSortParam', this.sortParam);
    // this.sortOrder = this.filterService.getSingleState('subadminSortOrder', this.sortOrder);
    // this.pageNumber = this.filterService.getSingleState('subadminPageNo', this.pageNumber);
    // this.size = this.filterService.getSingleState('subadminSize', this.size);
    this.filter.role = "ROLE_SUBADMIN";
  }

  ngOnInit() {
    this.momentDateTime24Format =
      CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
    // this.activeRoleList = this.subadminService
    //   .getActiveRoleList()
    //   .pipe(first())
    //   .subscribe(
    //     (response) => {
    //       this.roleList = response.data;
    //     },
    //     (error) => {
    //       console.log(error);
    //     }
    //   );
    let obj = {
      name: "ROLE_SUBADMIN",
      id: "ROLE_SUBADMIN",
    };
    this.roleList.push(obj);

    this.getAllSubadminList();
  }
  /**
   * Get status class from configuration
   * @param status Active/Inactive/Pending
   */
  public getStatusClass(status) {
    if (status) {
      return CONFIGCONSTANTS.statusClass[status];
    } else {
      return "badge-primary";
    }
  }
  /**
   * Sort datatable fields
   * @param event event was triggered, start sort sequence
   */
  public onSort(event) {
    if (event.sorts[0].prop == "firstName") {
      this.sortParam = "upr." + event.sorts[0].prop;
    }
    if (event.sorts[0].prop == "userProfile.email") {
      this.sortParam = "upr.email";
    }
    if (
      event.sorts[0].prop == "createdDate" ||
      event.sorts[0].prop == "userName" ||
      event.sorts[0].prop == "status"
    ) {
      this.sortParam = "u." + event.sorts[0].prop;
    }
    this.sortOrder = event.sorts[0].dir;
    this.rerender(false);
  }

  /**
   * Populate the table with new data based on the page number
   * @param page The page to select
   */
  public setPage(pageInfo) {
    this.pageNumber = pageInfo.offset;
    this.rerender(false);
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
    this.rerender(true);
  }
  /**
   * Get Sub Admin list data
   */
  private getAllSubadminList() {
    this.loadingIndicator = true;
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("subadminFilter", this.filter);
    this.filterService.saveSingleState("subadminSortParam", this.sortParam);
    this.filterService.saveSingleState("subadminSortOrder", this.sortOrder);
    this.filterService.saveSingleState("subadminPageNo", this.pageNumber);
    this.filterService.saveSingleState("subadminSize", this.size);
    this.subadminService
      .getAllSubadminList({
        fullName: this.filter.fullname,
        email: this.filter.email,
        status: this.filter.status,
        roleName: this.filter.role,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        columnName: this.sortParam,
        sortingOrder: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.subadminList = resp.responseData.users;
          for (var i in this.subadminList) {
            this.subadminList[i].firstName =
              this.subadminList[i].userProfile.firstName +
              " " +
              this.subadminList[i].userProfile.lastName;
          }
          this.totalReords = resp.responseData.count;
        },
        (err) => {
          this.loadingIndicator = false;
        }
      );
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.subadmnID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
    this.newpass = "";
    this.confirmpass = "";
    this.submitted = false;
  }
  /**
   * Change Sub Admin status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.manageuserService
      .changeManageUserStatus(this.subadmnID)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status === "SUCCESS") {
            this.modalRef.hide();
            this.toastr.success("Status changed successfully.");
            this.getAllSubadminList();
            this.rerender(false);
          }
        },
        (error) => {
          this.loader.hideLoader();
          if (error) {
            this.toastr.error(error.errorList);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  /**
   * Delete Sub Admin
   */
  public deleteSubadmin() {
    this.loader.showLoader();
    this.manageuserService
      .deleteManageUser(this.subadmnID)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status === "SUCCESS") {
            this.modalRef.hide();
            this.toastr.success("User Deleted successfully");
            this.rerender(true);
          }
        },
        (error) => {
          this.loader.hideLoader();
          if (error && error.errorList) {
            this.toastr.error(error.errorList);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  public searchApply() {
    this.rerender(true);
  }

  public resetSearch() {
    this.datatable.headerComponent.offsetX = 0;
    this.filter = {
      status: "",
      role: "ROLE_SUBADMIN",
      fullname: "",
      email: "",
    };
    this.rerender(true);
  }
  /**
   * Change Sub Admin Password
   * @param changePassFrm for password form validation
   */
  public changePassword(changePassFrm: NgForm) {
    this.submitted = true;
    if (changePassFrm.invalid) {
      return;
    }

    const data = {
      userId: this.subadmnID,
      newPassword: this.newpass,
      confirmPassword: this.confirmpass,
    };
    this.loader.showLoader();
    this.manageuserService
      .changeManageUserPassword(data)
      .pipe(first())
      .subscribe(
        (data_v) => {
          this.loader.hideLoader();
          if (data_v.status === "SUCCESS") {
            this.modalRef.hide();
            this.toastr.success("Password changed successfully");
            this.newpass = "";
            this.confirmpass = "";
            this.submitted = false;
            this.rerender(false);
          }
        },
        (error) => {
          this.loader.hideLoader();
          this.newpass = "";
          this.confirmpass = "";
          this.submitted = false;
          const statusError = error;
          if (statusError && statusError.errorList) {
            this.toastr.error(statusError.errorList);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  ngOnDestroy(): void {
    // this.activeRoleList.unsubscribe();
  }
  /**
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getAllSubadminList();
  }
  checkPass() {
    this.conf_pass_match = this.manageuserService.checkPassword(this.newpass);
  }
}
