import { Component, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { first } from "rxjs/operators";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { RulesetService } from "../../../_services/ruleset.service";
import { ToastrService } from "ngx-toastr";
import { BsModalService, BsModalRef } from "ngx-bootstrap/modal";
import { FilterStorageService } from "../../../_services/filter-storage.service";
import { LoaderService } from "../../../_services/loader.service";
import { ColumnMode, DatatableComponent } from "@swimlane/ngx-datatable";

@Component({
  selector: "app-ruleset-list",
  templateUrl: "./ruleset-list.component.html",
  styleUrls: ["./ruleset-list.component.scss"],
})
export class RulesetListComponent implements OnInit {
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
  ruleList = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end

  filter = {
    name: "",
    active: "",
  };
  private ruleId: number;
  private changeStatusType: string;
  private changedStatus: string;
  momentDateTime24Format: string;
  loadingIndicator = false;
  constructor(
    private rulesetService: RulesetService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {
    // For get filter, sorting, pagenumber and size in storage
    this.filter = this.filterService.getState("rulsetFilter", this.filter);
    this.sortParam = this.filterService.getSingleState(
      "rulsetSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "rulsetSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "rulsetPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("rulsetSize", this.size);
  }

  ngOnInit() {
    this.momentDateTime24Format =
      CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
    this.getRulesetList();
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
  private getRulesetList() {
    this.loadingIndicator = true;
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("rulsetFilter", this.filter);
    this.filterService.saveSingleState("rulsetSortParam", this.sortParam);
    this.filterService.saveSingleState("rulsetSortOrder", this.sortOrder);
    this.filterService.saveSingleState("rulsetPageNo", this.pageNumber);
    this.filterService.saveSingleState("rulsetSize", this.size);
    this.rulesetService
      .getRulesetList({
        name: this.filter.name,
        active: this.filter.active,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.ruleList = resp.responseData;
          this.totalReords = resp.responseData.length;
        },
        (err) => {
          this.loadingIndicator = false;
        }
      );
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.ruleId = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change Sub Admin status Active or Inactive
   */
  public changeStatus() {
    this.loader.showLoader();
    this.changedStatus = this.changeStatusType === "1" ? "0" : "1";
    this.rulesetService
      .changeRulesetStatus({ status: this.changedStatus, ruleId: this.ruleId })
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
   * Delete Rule set
   */
  public deleteRuleset() {
    this.loader.showLoader();
    this.rulesetService
      .deleteRuleset({ ruleId: this.ruleId })
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
    this.datatable.headerComponent.offsetX = 0;
    this.filter = {
      name: "",
      active: "",
    };
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
    this.getRulesetList();
  }
}
