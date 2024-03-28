import {
  Component,
  OnInit,
  ViewChild,
  TemplateRef,
  OnDestroy,
} from "@angular/core";
import { CONFIGCONSTANTS } from "../../config/app-constants";
import { ColumnMode, DatatableComponent } from "@swimlane/ngx-datatable";
import { first } from "rxjs/operators";
import { ReviewService } from "../../_services/review.service";
import { BsModalService, BsModalRef } from "ngx-bootstrap/modal";
import { ToastrService } from "ngx-toastr";
import { Subscription } from "rxjs";
import { LanguageService } from "../../_services/language.service";
import { TranslateService } from "@ngx-translate/core";
import { LoaderService } from "../../_services/loader.service";
import { FilterStorageService } from "../../_services/filter-storage.service";

@Component({
  selector: "app-review",
  templateUrl: "./review.component.html",
  styleUrls: ["./review.component.scss"],
})
export class ReviewComponent implements OnInit, OnDestroy {
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
  reviewList = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end
  momentDateTime24Format = "MM/DD/YYYY hh:mm:ss A";
  name = "";
  private reviewID: number;
  private subscription: Subscription;
  private Language = localStorage.getItem("lan") || "en";
  loadingIndicator = false;
  private changeStatusType: string;
  private changedStatus: string;
  constructor(
    private reviewService: ReviewService,
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
    this.name = this.filterService.getSingleState("reviewFilter", this.name);
    this.sortParam = this.filterService.getSingleState(
      "reviewSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "reviewSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "reviewPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("reviewSize", this.size);
    this.getReviewList();
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
   * Get Sub Admin list data
   */
  private getReviewList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveSingleState("reviewFilter", this.name);
    this.filterService.saveSingleState("reviewSortParam", this.sortParam);
    this.filterService.saveSingleState("reviewSortOrder", this.sortOrder);
    this.filterService.saveSingleState("reviewPageNo", this.pageNumber);
    this.filterService.saveSingleState("reviewSize", this.size);
    this.loadingIndicator = true;
    this.reviewService
      .getReviewList({
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
          this.reviewList = resp.responseData;
          this.totalReords = resp.responseData.length;

          for (var i in this.reviewList) {
            this.reviewList[i].fromName =
              this.reviewList[i].fromName.userProfile.firstName +
              " " +
              this.reviewList[i].fromName.userProfile.lastName;
            this.reviewList[i].toName =
              this.reviewList[i].toName.userProfile.firstName +
              " " +
              this.reviewList[i].toName.userProfile.lastName;
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
   * API call and refresh datatable value
   * @param goFirstPage set first page when param value true
   */
  rerender(goFirstPage): void {
    if (goFirstPage) {
      this.pageNumber = 0;
    }
    this.getReviewList();
  }
  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.reviewID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  public deleteReview() {
    this.loader.showLoader();
    this.reviewService
      .deleteReview(this.reviewID)
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
  /**
   * Change status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.reviewService
      .changeReviewStatus(this.changedStatus, this.reviewID)
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
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
