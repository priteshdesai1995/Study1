import { Component, OnInit, ViewChild, TemplateRef } from "@angular/core";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { ColumnMode, DatatableComponent } from "@swimlane/ngx-datatable";
import { LocationService } from "../../../_services/location.service";
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../../_services/loader.service";
import { first } from "rxjs/operators";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-manage-city",
  templateUrl: "./manage-city.component.html",
  styleUrls: ["./manage-city.component.scss"],
})
export class ManageCityComponent implements OnInit {
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
  cityList = [];
  stateList = [];
  countryList = [];
  sortParam = "created_date";
  sortOrder = "desc";
  // Get datatble configuration -- end
  momentDateTime24Format =
    CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
  filter = {
    cityname: "",
    statename: "",
    countryId: "",
    status: "",
  };
  modalID: number;
  changeStatusType: string;
  changedStatus: string;
  loadingIndicator = false;
  constructor(
    private locationService: LocationService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private filterService: FilterStorageService,
    private loader: LoaderService
  ) {}

  ngOnInit() {
    this.filter = this.filterService.getState("cityFilter", this.filter);
    // this.sortParam = this.filterService.getSingleState('citySortParam', this.sortParam);
    // this.sortOrder = this.filterService.getSingleState('citySortOrder', this.sortOrder);
    this.pageNumber = this.filterService.getSingleState(
      "cityPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("citySize", this.size);
    this.getCountryList();
    this.getStateList();
    this.getCityList();
  }
  getStateList() {
    if (!this.filter.countryId) {
      return;
    }
    this.locationService
      .getActiveState(this.filter.countryId)
      .pipe(first())
      .subscribe(
        (response) => {
          this.stateList = response.responseData;
        },
        (error) => {
          console.log(error);
        }
      );
  }
  getCountryList() {
    this.locationService
      .getActiveCountry()
      .pipe(first())
      .subscribe(
        (response) => {
          this.countryList = response.responseData;
        },
        (error) => {
          console.log(error);
        }
      );
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
  private getCityList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("cityFilter", this.filter);
    this.filterService.saveSingleState("citySortParam", this.sortParam);
    this.filterService.saveSingleState("citySortOrder", this.sortOrder);
    this.filterService.saveSingleState("cityPageNo", this.pageNumber);
    this.filterService.saveSingleState("citySize", this.size);
    this.loadingIndicator = true;
    this.locationService
      .getAllCityList({
        cityName: this.filter.cityname,
        stateName: this.filter.statename,
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
          this.cityList = resp.responseData;
          this.totalReords = resp.meta.count;
        },
        (err) => {
          this.loadingIndicator = false;
        }
      );
  }

  openModal(template: TemplateRef<any>, id, status) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.modalID = id;
    this.changeStatusType = status;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Change Sub Admin status Active or Inactive
   */
  public changeStatus() {
    this.changedStatus =
      this.changeStatusType === "Active" ? "Inactive" : "Active";
    this.loader.showLoader();
    this.locationService
      .changeCityStatus(this.changedStatus, this.modalID)
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
   * Delete Country
   */
  public deleteState() {
    this.loader.showLoader();
    this.locationService
      .deleteCity(this.modalID)
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
      cityname: "",
      statename: "",
      countryId: "",
      status: "",
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
    this.getCityList();
  }
}
