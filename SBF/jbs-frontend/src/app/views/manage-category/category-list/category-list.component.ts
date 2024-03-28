import {
  Component,
  OnDestroy,
  OnInit,
  ViewChild,
  TemplateRef,
} from "@angular/core";
import { CategoryService } from "./../../../_services/category-service";
import { first } from "rxjs/operators";
import { BsModalService, ModalDirective } from "ngx-bootstrap/modal";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { ToastrService } from "ngx-toastr";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { LoaderService } from "../../../_services/loader.service";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-category-list",
  templateUrl: "./category-list.component.html",
  styleUrls: ["./category-list.component.scss"],
})
export class CategoryListComponent implements OnInit {
  private modalRef: BsModalRef;
  // Get datatble configuration -- start
  reorderable = CONFIGCONSTANTS.datatableConfig.reorderable;
  scrollbarH = CONFIGCONSTANTS.datatableConfig.scrollbarH;
  piningRight = CONFIGCONSTANTS.datatableConfig.piningRight;
  headerHeight = CONFIGCONSTANTS.datatableConfig.headerHeight;
  footerHeight = CONFIGCONSTANTS.datatableConfig.footerHeight;
  rowHeight = CONFIGCONSTANTS.datatableConfig.rowHeight;
  limitList: any[] = CONFIGCONSTANTS.datatableConfig.limitList;
  ColumnMode = ColumnMode;
  size = CONFIGCONSTANTS.datatableConfig.page.size;
  dtMessages = CONFIGCONSTANTS.datatableConfig.dtMessages;
  @ViewChild(DatatableComponent, { static: false })
  datatable: DatatableComponent;
  categoryList: any[] = [];
  global_search = "";
  private filteredData = [];
  // Get datatble configuration -- end
  private changeStatusId: number;
  private deleteCategoryId: number;
  private changeStatusType: string;
  private changedStatus: string;
  loadingIndicator = false;

  sortParam = "";
  sortOrder = "";
  pageNumber = 0;
  constructor(
    private categoryService: CategoryService,
    private toastr: ToastrService,
    private loader: LoaderService,
    private filterService: FilterStorageService,
    private modalService: BsModalService
  ) {}

  ngOnInit() {
    this.global_search = this.filterService.getSingleState(
      "categoryFilter",
      this.global_search
    );
    this.size = this.filterService.getSingleState("categorySize", this.size);
    this.sortParam = this.filterService.getSingleState(
      "categorySortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "categorySortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "categoryPageNo",
      this.pageNumber
    );
    this.getAllCategoryList();
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
    this.filterService.saveSingleState("categorySortParam", this.sortParam);
    this.filterService.saveSingleState("categorySortOrder", this.sortOrder);
  }
  /**
   * Populate the table with new data based on the page number
   * @param page The page to select
   */
  public setPage(pageInfo) {
    this.pageNumber = pageInfo.offset;
    this.filterService.saveSingleState("categoryPageNo", this.pageNumber);
  }
  /**
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
    this.filterService.saveSingleState("categorySize", this.size);
  }
  /**
   * Get Category list data
   */
  private getAllCategoryList(): void {
    this.loadingIndicator = true;
    this.categoryService
      .getAllCategoryList()
      .pipe(first())
      .subscribe(
        (data) => {
          this.loadingIndicator = false;
          if (!data.responseData) {
            this.categoryList = [];
          } else {
            this.categoryList = data.responseData;
            this.filteredData = data.responseData;
            this.filterDatatable();
          }
        },
        (err) => {
          this.loadingIndicator = false;
        }
      );
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.changeStatusId = id;
    this.deleteCategoryId = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change Category status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.categoryService
      .changeCategoryStatus(this.changedStatus, this.changeStatusId)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status === "SUCCESS") {
            this.modalRef.hide();
            this.toastr.success(data.responseData);
            this.rerender();
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
   * Delete Category
   */
  public deleteCategory() {
    this.loader.showLoader();
    this.categoryService
      .deleteCategory(this.deleteCategoryId)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status === true) {
            this.modalRef.hide();
            this.toastr.success(data.meta.message);
            this.rerender();
          } else if (!data.meta.status) {
            this.toastr.error(data.meta.message);
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

  private rerender(): void {
    this.getAllCategoryList();
  }
  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable() {
    // const val = event.target.value.toLowerCase();
    const val = this.global_search.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ["name", "parent_name", "status"];
    // assign filtered matches to the active datatable
    this.categoryList = this.filteredData.filter(function (item) {
      // iterate through each row's column data
      for (let i = 0; i < keys.length; i++) {
        // check for a match
        if (
          (item[keys[i]] &&
            item[keys[i]].toString().toLowerCase().indexOf(val) !== -1) ||
          !val
        ) {
          // found match, return true to add to result set
          return true;
        }
      }
    });
    // whenever the filter changes, always go back to the first page
    // this.datatable.offset = 0;
    this.filterService.saveSingleState("categoryFilter", this.global_search);
  }
}
