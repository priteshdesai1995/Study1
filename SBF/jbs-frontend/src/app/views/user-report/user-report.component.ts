import {
  Component,
  OnInit,
  ViewChild,
  TemplateRef,
  OnDestroy,
} from "@angular/core";
import { Subscription } from "rxjs";
import { CONFIGCONSTANTS } from "../../config/app-constants";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { ColumnMode, DatatableComponent } from "@swimlane/ngx-datatable";
import { ReportService } from "../../_services/report.service";
import { ToastrService } from "ngx-toastr";
import { LanguageService } from "../../_services/language.service";
import { TranslateService } from "@ngx-translate/core";
import { first } from "rxjs/operators";
import { LoaderService } from "../../_services/loader.service";
import { FilterStorageService } from "../../_services/filter-storage.service";

@Component({
  selector: "app-user-report",
  templateUrl: "./user-report.component.html",
  styleUrls: ["./user-report.component.scss"],
})
export class UserReportComponent implements OnInit, OnDestroy {
  modalRef: BsModalRef;
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
  reportList = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end
  momentDateTime24Format = "MM/DD/YYYY hh:mm:ss A";
  name = "";
  private reportID: number;
  private subscription: Subscription;
  private Language = localStorage.getItem("lan") || "en";
  loadingIndicator = false;
  private changeStatusType: string;
  private changedStatus: string;
  constructor(
    private reportService: ReportService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private languageSwitcher: LanguageService,
    private translate: TranslateService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {
    this.subscription = this.languageSwitcher.getLanguage().subscribe((lan) => {
      this.translate.setDefaultLang(lan);
      this.translate.use(lan);
    });
    this.languageSwitcher.changeLanguage(this.Language);
  }

  ngOnInit() {
    this.name = this.filterService.getSingleState("reportFilter", this.name);
    this.sortParam = this.filterService.getSingleState(
      "reportSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "reportSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "reportPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("reportSize", this.size);
    this.getReportList();
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
   * Get Sub Admin list data
   */
  private getReportList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveSingleState("reportFilter", this.name);
    this.filterService.saveSingleState("reportSortParam", this.sortParam);
    this.filterService.saveSingleState("reportSortOrder", this.sortOrder);
    this.filterService.saveSingleState("reportPageNo", this.pageNumber);
    this.filterService.saveSingleState("reportSize", this.size);
    this.loadingIndicator = true;
    this.reportService
      .getUserReportList({
        search_keyword: this.name,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.reportList = resp.responseData;
          this.totalReords = resp.responseData.length;

          for (var i in this.reportList) {
            this.reportList[i].username =
              this.reportList[i].username.userProfile.firstName +
              " " +
              this.reportList[i].username.userProfile.lastName;
            this.reportList[i].reportedBy =
              this.reportList[i].reportedBy.userProfile.firstName +
              " " +
              this.reportList[i].reportedBy.userProfile.lastName;
          }
        },
        (err) => {
          this.loadingIndicator = false;
        }
      );
  }
  public searchApply() {
    this.rerender(true);
  }

  public resetSearch() {
    this.name = "";
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
  /**
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getReportList();
  }
  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.reportID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.reportService
      .changeReportStatus(this.changedStatus, this.reportID)
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
  public deleteReport() {
    this.loader.showLoader();
    this.reportService
      .deleteUserReport(this.reportID)
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
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
