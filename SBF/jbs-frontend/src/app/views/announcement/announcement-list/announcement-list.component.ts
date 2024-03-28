import { Component, OnInit, ViewChild } from "@angular/core";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { DatatableComponent, ColumnMode } from "@swimlane/ngx-datatable";
import { BsModalRef } from "ngx-bootstrap/modal/bs-modal-ref.service";
import { AnnouncementService } from "../../../_services/announcement.service";
import { first } from "rxjs/operators";
import * as moment from "moment";
import { FilterStorageService } from "../../../_services/filter-storage.service";

@Component({
  selector: "app-announcement-list",
  templateUrl: "./announcement-list.component.html",
  styleUrls: ["./announcement-list.component.scss"],
})
export class AnnouncementListComponent implements OnInit {
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
  ManageAnnouncementList = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end

  constructor(
    private announcementService: AnnouncementService,
    private filterService: FilterStorageService
  ) {}
  data: any[];
  types: any = {
    push: "Push",
    email: "Email",
    sms: "SMS",
  };
  platforms: any = {
    all: "All",
    android: "Android",
    ios: "IOS",
    web: "Web",
  };
  status: any = {
    "-1": "Pending",
    "0": "In-Progress",
    "1": "Announced",
  };
  filter: any = {
    status: "",
    type: "",
    user_type: "",
    keyword: "",
    select_date: "",
  };
  momentDateTime24Format: string;
  momentDateFormat: string;

  ngOnInit() {
    this.filter = this.filterService.getState("announceFilter", this.filter);
    if (this.filter.select_date) {
      this.filter.select_date = [
        new Date(this.filter.select_date[0]),
        new Date(this.filter.select_date[1]),
      ];
    }
    this.sortParam = this.filterService.getSingleState(
      "announceSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "announceSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "announcePageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("announceSize", this.size);
    this.data = [];
    this.getAllAnnouncementListURL();
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
   * Get All Announcement List
   */
  private getAllAnnouncementListURL() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("announceFilter", this.filter);
    this.filterService.saveSingleState("announceSortParam", this.sortParam);
    this.filterService.saveSingleState("announceSortOrder", this.sortOrder);
    this.filterService.saveSingleState("announcePageNo", this.pageNumber);
    this.filterService.saveSingleState("announceSize", this.size);
    this.loadingIndicator = true;
    this.announcementService
      .getAllAnnouncementList({
        title: this.filter.keyword,
        start_date: this.filter.select_date
          ? moment(this.filter.select_date[0]).format("YYYY-MM-DD")
          : "",
        end_date: this.filter.select_date
          ? moment(this.filter.select_date[1]).format("YYYY-MM-DD")
          : "",
        type: this.filter.type,
        user_type: this.filter.user_type,
        status: this.filter.status,
        startRec: this.pageNumber * this.size,
        endRec: this.size,
        sort_param: "createdDate",
        sort_type: this.sortOrder,
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.ManageAnnouncementList = resp.responseData;

          console.log(JSON.stringify(this.ManageAnnouncementList));
          this.totalReords = resp.responseData.length;
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  public searchApply() {
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
    this.getAllAnnouncementListURL();
  }

  /**
   * Reset Searching
   */
  public resetSearch() {
    this.filter.type = "";
    this.filter.user_type = "";
    this.filter.keyword = "";
    this.filter.status = "";
    this.filter.select_date = "";
    this.rerender(true);
  }
}
