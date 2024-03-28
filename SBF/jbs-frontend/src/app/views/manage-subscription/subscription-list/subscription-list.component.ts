import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  TemplateRef,
} from "@angular/core";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { LoaderService } from "../../../_services/loader.service";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { CONFIG } from "../../../config/app-config";
import { ManageSubscriptionService } from "../../../_services/manage-subscription.service";
import * as moment from "moment";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-subscription-list",
  templateUrl: "./subscription-list.component.html",
  styleUrls: ["./subscription-list.component.scss"],
})
export class SubscriptionListComponent implements OnInit {
  modalRef: BsModalRef;
  // Get datatble configuration -- start
  private loadingIndicator = false;
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
  manageSubscriptionList = [];
  sortParam = "created_date";
  sortOrder = "desc";
  // Get datatble configuration -- end

  submitted = false;
  filter: any = {
    name: "",
    validity: "",
    price: "",
    select_date: "",
    is_trial_plan: "",
  };

  plan_validity = {
    month: "Monthly",
    // 'quarter': 'Quarterly',
    year: "Yearly",
  };

  plan_type = {
    "1": "Trial",
    "0": "Paid",
  };

  private subscriptionID: number;
  private changeStatusType: string;
  private changedStatus: string;
  // roleList: any;
  momentDateTime24Format: string;
  momentDateFormat: string;

  constructor(
    private manageSubscriptionService: ManageSubscriptionService,
    private toastr: ToastrService,
    private filterService: FilterStorageService,
    private modalService: BsModalService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState(
      "subscriptionFilter",
      this.filter
    );
    if (this.filter.select_date) {
      this.filter.select_date = [
        new Date(this.filter.select_date[0]),
        new Date(this.filter.select_date[1]),
      ];
    }
    this.sortParam = this.filterService.getSingleState(
      "subscriptionSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "subscriptionSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "subscriptionPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState(
      "subscriptionSize",
      this.size
    );
    this.getAllManageSubscriptionList();
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
    this.sortParam = event.sorts[0].prop;
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
   * Get subscription list data
   */
  private getAllManageSubscriptionList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("subscriptionFilter", this.filter);
    this.filterService.saveSingleState("subscriptionSortParam", this.sortParam);
    this.filterService.saveSingleState("subscriptionSortOrder", this.sortOrder);
    this.filterService.saveSingleState("subscriptionPageNo", this.pageNumber);
    this.filterService.saveSingleState("subscriptionSize", this.size);
    this.loadingIndicator = true;
    this.manageSubscriptionService
      .getAllManageSubscriptionList({
        name: this.filter.name,
        validity: this.filter.validity,
        price: this.filter.price,
        start_date: this.filter.select_date
          ? moment(this.filter.select_date["0"]).format("YYYY-MM-DD")
          : "",
        end_date: this.filter.select_date
          ? moment(this.filter.select_date["1"]).format("YYYY-MM-DD")
          : "",
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
        endRec: this.size,
        startRec: this.pageNumber * this.size,
        is_trial_plan: this.filter.is_trial_plan,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.manageSubscriptionList = resp.responseData;
          this.totalReords = resp.responseData.length;
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  /* Open any Modal Popup */
  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.subscriptionID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
    this.submitted = false;
  }
  /**
   * Change subscription status Active or Inactive
   */
  public changeStatus() {
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.manageSubscriptionService
      .changeManageSubscriptionStatus(this.changedStatus, this.subscriptionID)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(false);
          }
        },
        (error) => {
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
   * Delete Subscription
   */
  public deleteManageSubscription() {
    this.manageSubscriptionService
      .deleteManageSubscription(this.subscriptionID)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(true);
          }
        },
        (error) => {
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
    this.filter.name = "";
    this.filter.validity = "";
    this.filter.price = "";
    this.filter.select_date = "";
    this.filter.is_trial_plan = "";
    this.rerender(true);
  }

  /**
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  private rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getAllManageSubscriptionList();
  }
}
