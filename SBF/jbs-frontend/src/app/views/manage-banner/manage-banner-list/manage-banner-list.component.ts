import { Component, OnInit, ViewChild, TemplateRef } from "@angular/core";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { LoaderService } from "../../../_services/loader.service";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { CONFIG } from "../../../config/app-config";
import * as moment from "moment";
import { ManageBannerService } from "../../../_services/manage-banner.service";
import { PopupImageOpenComponent } from "../../announcement/announcement-details/popup-image-open.component";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-manage-banner-list",
  templateUrl: "./manage-banner-list.component.html",
  styleUrls: ["./manage-banner-list.component.scss"],
})
export class ManageBannerListComponent implements OnInit {
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
  manageBannerList = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end

  filter = {
    banner_title: "",
    created_by: "",
    status: "",
  };
  private bannerID: String;
  private changeStatusType: string;
  private changedStatus: string;
  submitted = false;
  momentDateTime24Format: string;
  momentDateFormat: string;

  constructor(
    private manageBannerService: ManageBannerService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState("bannerFilter", this.filter);
    this.sortParam = this.filterService.getSingleState(
      "bannerSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "bannerSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "bannerPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("bannerSize", this.size);
    this.getAllBannerList();
    this.momentDateTime24Format = "MM/DD/YYYY hh:mm:ss";
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
   * get All Banner List
   */
  private getAllBannerList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("bannerFilter", this.filter);
    this.filterService.saveSingleState("bannerSortParam", this.sortParam);
    this.filterService.saveSingleState("bannerSortOrder", this.sortOrder);
    this.filterService.saveSingleState("bannerPageNo", this.pageNumber);
    this.filterService.saveSingleState("bannerSize", this.size);
    this.loadingIndicator = true;
    this.manageBannerService
      .getAllBannerList({
        banner_title: this.filter.banner_title,
        created_by: this.filter.created_by,
        status: this.filter.status,
        endRec: this.size,
        startRec: this.pageNumber * this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.manageBannerList = resp.responseData;
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
    this.bannerID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
    this.submitted = false;
  }

  /**
   * Change banner status Active or Inactive
   */
  public changeStatus() {
    this.changedStatus =
      this.changeStatusType === "ACTIVE" ? "INACTIVE" : "ACTIVE";
    this.manageBannerService
      .changeManageBannerStatus(this.changedStatus, this.bannerID)
      .pipe(first())
      .subscribe(
        (data) => {
          console.log("status => " + this.changeStatus);
          console.log("banner if -> " + this.bannerID);

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
   * Delete Banner
   */
  public deleteManageBanner() {
    this.manageBannerService
      .deleteManageBanner(this.bannerID)
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
    this.filter.banner_title = "";
    this.filter.created_by = "";
    this.filter.status = "";
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
    this.getAllBannerList();
  }

  openModalForImage(img) {
    const initialState = {
      image: img,
    };
    this.modalRef = this.modalService.show(PopupImageOpenComponent, {
      class: "modal-md",
      initialState,
    });
  }
}
