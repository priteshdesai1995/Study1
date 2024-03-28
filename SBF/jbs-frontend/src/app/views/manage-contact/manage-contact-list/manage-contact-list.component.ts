import { Component, OnInit, ViewChild, TemplateRef } from "@angular/core";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { LoaderService } from "../../../_services/loader.service";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { ManagecontactService } from "../../../_services/managecontact.service";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-manage-contact-list",
  templateUrl: "./manage-contact-list.component.html",
  styleUrls: ["./manage-contact-list.component.scss"],
})
export class ManageContactListComponent implements OnInit {
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
  contactList: any[] = [];
  sortParam = "created_date";
  sortOrder = "desc";
  // Get datatble configuration -- end
  private changeStatusType: string;
  private userID: number;

  filter = {
    name: "",
    contactDetails: "",
    status: "",
  };

  subject = "";
  message = "";
  selectStatus = "Pending";
  submitted = false;
  momentDateTime24Format =
    CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
  constructor(
    private toastr: ToastrService,
    private modalService: BsModalService,
    private loader: LoaderService,
    private filterService: FilterStorageService,
    private contactService: ManagecontactService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState("enquiryFilter", this.filter);
    // this.sortParam = this.filterService.getSingleState('enquirySortParam', this.sortParam);
    // this.sortOrder = this.filterService.getSingleState('enquirySortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState(
      "enquiryPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("enquirySize", this.size);
    this.getAllManageContactList();
  }

  public onSort(event) {
    this.sortParam = event.sorts[0].prop;
    this.sortOrder = event.sorts[0].dir;
    this.rerender(false);
  }

  public setPage(pageInfo) {
    this.pageNumber = pageInfo.offset;
    this.rerender(false);
  }

  public changeLimit(value) {
    this.size = value;
    this.rerender(true);
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

  private getAllManageContactList(): void {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("enquiryFilter", this.filter);
    this.filterService.saveSingleState("enquirySortParam", this.sortParam);
    this.filterService.saveSingleState("enquirySortOrder", this.sortOrder);
    this.filterService.saveSingleState("enquiryPageNo", this.pageNumber);
    this.filterService.saveSingleState("enquirySize", this.size);
    this.loadingIndicator = true;
    this.contactService
      .getAllContactList({
        name: this.filter.name,
        contactDetails: this.filter.contactDetails,
        subject: this.subject,
        message: this.message,
        status: this.filter.status,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        columnName: this.sortParam,
        sortingOrder: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.contactList = resp.responseData;
          // this.totalReords = resp.data['original'].recordsTotal;
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
    this.selectStatus = status === "Pending" ? "" : status;
  }

  decline(): void {
    this.modalRef.hide();
  }

  public deleteManageUser() {
    this.loader.showLoader();
    this.contactService
      .deleteContact(this.userID)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.loader.hideLoader();
            this.toastr.success(data.meta.message);
            this.rerender(true);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const statusError = error;
          if (statusError && statusError.meta) {
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
      name: "",
      contactDetails: "",
      status: "",
    };
    this.subject = "";
    this.message = "";
    this.rerender(true);
  }

  private rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getAllManageContactList();
  }

  /**
   * Change contact status
   */
  public changeStatus(changeStatusFrm: NgForm) {
    this.submitted = true;
    if (changeStatusFrm.invalid) {
      return;
    }

    const formData = {
      id: this.userID,
      status: this.selectStatus,
    };
    this.loader.showLoader();
    this.contactService
      .changeStatus(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.loader.hideLoader();
            this.toastr.success(data.meta.message);
            this.rerender(true);
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
            this.toastr.error(errorData.meta.message);
          }
        }
      );
  }
}
