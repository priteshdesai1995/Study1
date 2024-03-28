import { Component, OnInit, ViewChild, TemplateRef } from "@angular/core";
import { first } from "rxjs/operators";
import { BsModalService } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { SuggestionService } from "../../../_services/suggestion.service";
import { NgForm } from "@angular/forms";
import { CONFIG } from "../../../config/app-config";
import { LoaderService } from "../../../_services/loader.service";
import { FilterStorageService } from "../../../_services/filter-storage.service";
import { saveAs } from "file-saver";

@Component({
  selector: "app-admin-suggestion-list",
  templateUrl: "./admin-suggestion-list.component.html",
  styleUrls: ["./admin-suggestion-list.component.scss"],
})
export class AdminSuggestionListComponent implements OnInit {
  private modalRef: BsModalRef;
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
  suggestionList = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end

  // filter
  filter = {
    category_name: "",
    suggestion: "",
    posted_name: "",
    status: "",
  };
  categoryList = [];
  private sugID: number;
  private notes: string;
  private changedStatus: string;
  momentDateTime24Format: string;
  constructor(
    private suggestionService: SuggestionService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState("adSuggFilter", this.filter);
    this.sortParam = this.filterService.getSingleState(
      "adSuggSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "adSuggSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "adSuggPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("adSuggSize", this.size);
    this.getActiveCategoryList();
    this.getAllSuggestionList();
    this.momentDateTime24Format =
      CONFIGCONSTANTS.momentDateTime12Format || "MM/DD/YYYY hh:mm:ss";
  }
  private getActiveCategoryList(): void {
    this.suggestionService
      .getActiveCategoryList(CONFIGCONSTANTS.suggestionId)
      .pipe(first())
      .subscribe((data) => {
        if (!data.responseData) {
          this.categoryList = [];
        } else {
          this.categoryList = data.responseData;
        }
      });
  }
  /**
   * Get status class from configuration
   * @param status Accepted/Rejected/Pending
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
   * Get Suggestion list data
   */
  private getAllSuggestionList(): void {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("adSuggFilter", this.filter);
    this.filterService.saveSingleState("adSuggSortParam", this.sortParam);
    this.filterService.saveSingleState("adSuggSortOrder", this.sortOrder);
    this.filterService.saveSingleState("adSuggPageNo", this.pageNumber);
    this.filterService.saveSingleState("adSuggSize", this.size);
    this.loadingIndicator = true;
    this.suggestionService
      .getAdminSuggestionList({
        category_name: this.filter.category_name,
        suggestion: this.filter.suggestion,
        posted_name: this.filter.posted_name,
        status: this.filter.status,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        length: this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.suggestionList = resp.responseData;
          this.totalReords = resp.responseData.length;
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  /* Open any Modal Popup */
  openModal(template: TemplateRef<any>, id, status, notes) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.sugID = id;
    this.changedStatus = status === "Pending" ? "" : status;
    this.notes = notes ? notes : "";
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change Suggestion status
   */
  public changeStatus(frm: NgForm) {
    if (frm.invalid) {
      return;
    }
    this.loader.showLoader();
    this.suggestionService
      .changeSuggestionStatus(this.changedStatus, this.notes, this.sugID)
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
   * Delete Suggestion
   */
  public deleteSuggestion() {
    this.loader.showLoader();
    this.suggestionService
      .deleteSuggestion(this.sugID)
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
      category_name: "",
      suggestion: "",
      posted_name: "",
      status: "",
    };
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
    this.getAllSuggestionList();
  }
  downloadData(type) {
    const exportData = {
      category_name: this.filter.category_name,
      suggestion: this.filter.suggestion,
      posted_name: this.filter.posted_name,
      status: this.filter.status,
      sort_param: this.sortParam,
      sort_type: this.sortOrder,
    };
    this.suggestionService.getExportSuggestionList(exportData, type).subscribe(
      (data) => {
        if (type == "csv") {
          if (data) {
            saveAs(data, "suggestions.csv");
          } else {
            this.toastr.error("No data available");
          }
        } else if (type == "pdf") {
          if (data) {
            saveAs(data, "suggestions.pdf");
          } else {
            this.toastr.error("No data available");
          }
        } else if (type == "excel") {
          if (data) {
            saveAs(data, "suggestions.xlsx");
          } else {
            this.toastr.error("No data available");
          }
        }

        // if (data.data.file) {
        //   window.open(CONFIG.fileDownloadURL + '?file=' + data.data.file);
        // } else {
        //   this.toastr.error('No data available');
        // }
      },
      (error) => {
        this.toastr.error("Something went wrong please try again.");
      },
      () => console.log("Exported Successfully.")
    );
  }
}
