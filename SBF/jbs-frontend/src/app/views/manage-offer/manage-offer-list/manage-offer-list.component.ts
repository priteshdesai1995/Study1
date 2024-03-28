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
import { ManageOfferService } from "../../../_services/manage-offer.service";
import * as moment from "moment";
import { OfferReportListComponent } from "./offer-report-list.component";
import { FilterStorageService } from "../../../_services/filter-storage.service";
import { saveAs } from "file-saver";

@Component({
  selector: "app-manage-offer-list",
  templateUrl: "./manage-offer-list.component.html",
  styleUrls: ["./manage-offer-list.component.scss"],
})
export class ManageOfferListComponent implements OnInit {
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
  manageOfferList: any = [];
  sortParam = "created_date";
  sortOrder = "desc";
  // Get datatble configuration -- end

  submitted = false;
  status = "";
  name = "";
  filter: any = {
    status: "",
    name: "",
    select_date: "",
  };
  offer_type = {
    "0": "Percentage",
    "1": "Amount",
  };
  offer_usage = {
    "0": "Multiple Time",
    "1": "One Time",
  };
  private offerId: number;
  private changeStatusType: string;
  private changedStatus: string;
  momentDateTime24Format: string;
  momentDateFormat: string;
  constructor(
    private manageOfferService: ManageOfferService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private loader: LoaderService,
    private filterService: FilterStorageService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState("offerFilter", this.filter);
    if (this.filter.select_date) {
      this.filter.select_date = [
        new Date(this.filter.select_date[0]),
        new Date(this.filter.select_date[1]),
      ];
    }
    this.sortParam = this.filterService.getSingleState(
      "offerSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "offerSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "offerPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("offerSize", this.size);
    this.getAllManageOfferList();
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
   * Get Offer list data
   */
  private getAllManageOfferList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("offerFilter", this.filter);
    this.filterService.saveSingleState("offerSortParam", this.sortParam);
    this.filterService.saveSingleState("offerSortOrder", this.sortOrder);
    this.filterService.saveSingleState("offerPageNo", this.pageNumber);
    this.filterService.saveSingleState("offerSize", this.size);
    this.loadingIndicator = true;
    this.manageOfferService
      .getAllManageOfferList({
        name: this.filter.name,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
        status: this.filter.status,
        endRec: this.size,
        startRec: this.pageNumber * this.size,
        start_date: this.filter.select_date
          ? moment(this.filter.select_date["0"]).format("YYYY-MM-DD")
          : "",
        end_date: this.filter.select_date
          ? moment(this.filter.select_date["1"]).format("YYYY-MM-DD")
          : "",
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.manageOfferList = resp.responseData;
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
    this.offerId = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
    this.submitted = false;
  }
  /**
   * Change Offer status Active or Inactive
   */
  public changeStatus() {
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.loader.showLoader();
    this.manageOfferService
      .changeManageOfferStatus(this.changedStatus, this.offerId)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender(false);
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
  /**
   * Delete Offer
   */
  public deleteOffer() {
    this.loader.showLoader();
    this.manageOfferService
      .deleteManageOffer(this.offerId)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.loader.hideLoader();
            this.modalRef.hide();
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
    this.filter.name = "";
    this.filter.status = "";
    this.filter.select_date = "";
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
    this.getAllManageOfferList();
  }

  /**
   * Offer Data Download
   * @param type
   */
  downloadData(type) {
    const exportData = {
      name: this.filter.name,
      sort_param: this.sortParam,
      sort_type: this.sortOrder,
      status: this.filter.status,
      start_date: this.filter.select_date
        ? moment(this.filter.select_date["0"]).format("YYYY-MM-DD")
        : "",
      end_date: this.filter.select_date
        ? moment(this.filter.select_date["1"]).format("YYYY-MM-DD")
        : "",
    };
    this.manageOfferService.getExportOfferList(exportData, type).subscribe(
      (data) => {
        if (type == "csv") {
          if (data) {
            saveAs(data, "offers.csv");
          } else {
            this.toastr.error("No data available");
          }
        } else if (type == "pdf") {
          if (data) {
            saveAs(data, "offers.pdf");
          } else {
            this.toastr.error("No data available");
          }
        } else if (type == "excel") {
          if (data) {
            saveAs(data, "offers.xlsx");
          } else {
            this.toastr.error("No data available");
          }
        }
        // (data) => {
        //   this.loader.hideLoader();
        //   if (data.data.file) {
        //     window.open(CONFIG.fileDownloadURL + '?file=' + data.data.file);
        //   } else {
        //     this.toastr.error('No data available');
        //   }
        // },
        // (error) => {
        //   this.loader.hideLoader();
        //   this.toastr.error('Something went wrong please try again.');
        // },
        // () => console.log('OK')
      },
      (error) => {
        this.toastr.error("Something went wrong please try again.");
      },
      () => console.log("Exported Successfully.")
    );
  }
  /* Open any Modal Popup */
  openModalReport(uuid, id, name) {
    const initialState = {
      offerUuid: uuid,
      offerId: id,
      offerName: name,
    };
    this.modalRef = this.modalService.show(OfferReportListComponent, {
      class: "modal-lg",
      initialState,
    });
  }
}
