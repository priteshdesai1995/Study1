import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  TemplateRef,
} from "@angular/core";
import { ManageuserService } from "./../../../_services/manageuser-service";
import { Manageuser } from "./../../../model/manageuser";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { LoaderService } from "../../../_services/loader.service";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { CONFIG } from "../../../config/app-config";
import { FilterStorageService } from "../../../_services/filter-storage.service";
import { saveAs } from "file-saver";
@Component({
  selector: "app-manage-user-list",
  templateUrl: "./manage-user-list.component.html",
  styleUrls: ["./manage-user-list.component.scss"],
})
export class ManageUserListComponent implements OnInit {
  fileName = "";
  file = "";
  modalRef: BsModalRef;
  // Get datatble configuration -- start
  loadingIndicator = false;
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
  ManageuserList: any[] = [];
  sortParam = "";
  sortOrder = "";
  // Get datatble configuration -- end

  submitted = false;
  newpass: string;
  filter = {
    status: "",
    gender: "",
    phone_number: "",
    fullname: "",
    email: "",
  };

  confirmpass: string;
  private userID: number;
  private changeStatusType: string;
  private changedStatus: string;
  momentDateTime24Format: string;
  momentDateFormat: string;
  conf_pass_match = "";
  constructor(
    private manageuserService: ManageuserService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    // For get filter, sorting, pagenumber and size in storage
    this.filter = this.filterService.getState("userFilter", this.filter);
    // this.sortParam = this.filterService.getSingleState('userSortParam', this.sortParam);
    // this.sortOrder = this.filterService.getSingleState('userSortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState(
      "userPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("userSize", this.size);
    this.getAllManageUserListURL();
    this.momentDateTime24Format =
      CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
    this.momentDateFormat = CONFIGCONSTANTS.momentDateFormat || "MM/DD/YYYY";
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
    if (event.sorts[0].prop == "fullName") {
      this.sortParam = "upr.firstName";
    }
    if (
      event.sorts[0].prop == "userProfile.email" ||
      event.sorts[0].prop == "userProfile.gender" ||
      event.sorts[0].prop == "userProfile.dateOfBirth"
    ) {
      var re = /userProfile/gi;
      this.sortParam = event.sorts[0].prop;
      this.sortParam = this.sortParam.replace(re, "upr");
    }
    if (
      event.sorts[0].prop == "createDate" ||
      event.sorts[0].prop == "userName" ||
      event.sorts[0].prop == "status"
    ) {
      this.sortParam = "u." + event.sorts[0].prop;
    }
    // this.sortParam = event.sorts[0].prop;
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
   * Get Users list data
   */
  private getAllManageUserListURL() {
    this.loadingIndicator = true;
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("userFilter", this.filter);
    this.filterService.saveSingleState("userSortParam", this.sortParam);
    this.filterService.saveSingleState("userSortOrder", this.sortOrder);
    this.filterService.saveSingleState("userPageNo", this.pageNumber);
    this.filterService.saveSingleState("userSize", this.size);
    this.manageuserService
      .getAllManageUserListURL({
        fullName: this.filter.fullname,
        email: this.filter.email,
        status: this.filter.status,
        gender: this.filter.gender,
        cellPhone: this.filter.phone_number,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        columnName: this.sortParam,
        sortingOrder: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.ManageuserList = resp.responseData.users;
          console.log(JSON.stringify(this.ManageuserList));
          for (var i in this.ManageuserList) {
            this.ManageuserList[i].fullName =
              this.ManageuserList[i].userProfile.firstName +
              " " +
              this.ManageuserList[i].userProfile.lastName;
          }
          this.totalReords = resp.responseData.count;
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  /* Open any Modal Popup */
  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.userID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
    this.newpass = "";
    this.confirmpass = "";
    this.submitted = false;
  }
  /**
   * Change user status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.manageuserService
      .changeManageUserStatus(this.userID)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data["status"]) {
            this.modalRef.hide();
            this.toastr.success("Status changed successfully.");
            this.getAllManageUserListURL();
            this.rerender(false);
          }
        },
        (error) => {
          this.loader.hideLoader();
          // const statusError = error;
          if (error) {
            this.toastr.error(error.error);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  /**
   * Delete user
   */
  public deleteManageUser() {
    this.loader.showLoader();
    this.manageuserService.deleteManageUser(this.userID).subscribe(
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
          this.toastr.error(error.errorList[0]);
        } else {
          this.toastr.error("Something went wrong please try again.");
        }
      }
    );
  }

  /**
   * Used to lock user for edit.
   *
   */
  public lockManageUser(id) {
    this.loader.showLoader();
    this.manageuserService
      .lockManageUser(id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.rerender(true);
          }
        },
        (error) => {
          this.loader.hideLoader();
          this.modalRef.hide();
          const statusError = error;
          if (statusError && statusError.meta) {
            this.toastr.error(statusError.meta.message);
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  /**
   * Unlock user for edit.
   *
   */
  public unlockManageUser() {
    this.loader.showLoader();
    this.manageuserService
      .unlockManageUser(this.userID, 0)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(true);
          }
        },
        (error) => {
          this.loader.hideLoader();
          this.modalRef.hide();
          const statusError = error;
          if (statusError && statusError.meta) {
            this.getAllManageUserListURL();
            this.toastr.error(statusError.meta.message);
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
    this.filter = {
      status: "",
      gender: "",
      phone_number: "",
      fullname: "",
      email: "",
    };
    this.rerender(true);
  }
  /**
   * Change User Password
   * @param changePassFrm for password form validation
   */
  public changePassword(changePassFrm: NgForm) {
    this.submitted = true;
    if (changePassFrm.invalid) {
      return;
    }
    const data = {
      newPassword: this.newpass,
      confirmPassword: this.confirmpass,
      userId: this.userID,
    };
    this.loader.showLoader();
    this.manageuserService
      .changeManageUserPassword(data)
      .pipe(first())
      .subscribe(
        (d) => {
          this.loader.hideLoader();
          console.log(JSON.stringify(d));
          if (d.status === "SUCCESS") {
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
  checkPass() {
    this.conf_pass_match = this.manageuserService.checkPassword(this.newpass);
  }
  /**
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  private rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getAllManageUserListURL();
  }
  downloadData(type) {
    const exportData = {
      name: this.filter.fullname,
      email: this.filter.email,
      status: this.filter.status,
      gender: this.filter.gender,
      phone_number: this.filter.phone_number,
      start: this.pageNumber * this.size,
      length: this.size,
      sort_param: this.sortParam,
      sort_type: this.sortOrder,
    };
    this.manageuserService.getExportUserList(type).subscribe((data) => {
      if (type == "csv") {
        if (data) {
          saveAs(data, "users.csv");
        } else {
          this.toastr.error("No data available");
        }
      } else if (type == "pdf") {
        if (data) {
          saveAs(data, "users.pdf");
        } else {
          this.toastr.error("No data available");
        }
      } else if (type == "excel") {
        if (data) {
          saveAs(data, "users.xlsx");
        } else {
          this.toastr.error("No data available");
        }
      }
    });
  }

  /**
   * Validate file
   * @param event get file array
   */
  public changeFile(event: any) {
    if (event.target.files[0] !== undefined) {
      const fileTypes = ["application/vnd.ms-excel", "text/csv"];
      if (fileTypes.includes(event.target.files[0].type) === false) {
        this.toastr.error("Please upload CSV file.");
      } else {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.file = event.target.files[0];
          this.fileName = event.target.files[0].name;
        };
        reader.readAsDataURL(event.target.files[0]);
      }
    }
  }
  public openFile() {
    document.getElementById("importCSV").click();
  }
  importCSVFile() {
    const formData: FormData = new FormData();
    formData.append("file", this.file);
    this.loader.showLoader();
    this.manageuserService
      .importCSVFile(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.toastr.success("Users saved successfully.");
            this.rerender(false);
            this.cancelImport();
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  cancelImport() {
    this.file = "";
    this.fileName = "";
  }
}
