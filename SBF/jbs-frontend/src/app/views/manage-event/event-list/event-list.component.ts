import { Component, OnInit, ViewChild, TemplateRef } from "@angular/core";
import { BsModalRef, BsModalService } from "ngx-bootstrap/modal";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { ColumnMode, DatatableComponent } from "@swimlane/ngx-datatable";
import { ManageEventService } from "../../../_services/manage-event.service";
import { SubadminService } from "./../../../_services/subadmin-service";
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../../_services/loader.service";
import { FilterStorageService } from "../../../_services/filter-storage.service";
import { first } from "rxjs/operators";
import * as moment from "moment";

@Component({
  selector: "app-event-list",
  templateUrl: "./event-list.component.html",
  styleUrls: ["./event-list.component.scss"],
})
export class EventListComponent implements OnInit {
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
  eventList: any = [];
  sortParam = "createdDate";
  sortOrder = "desc";
  // Get datatble configuration -- end
  filter: any = {
    name: "",
    schedule_date: "",
    created_by: "",
  };
  private eventId: number;
  momentDateTime24Format = "MM/DD/YYYY hh:mm:ss A";
  momentDateFormat = CONFIGCONSTANTS.momentDateFormat || "MM/DD/YYYY";
  accessToken: string;
  authInstance: gapi.auth2.GoogleAuth;
  error: string;
  user: gapi.auth2.GoogleUser;
  isAuthenticate = false;
  repeated = {
    never: "Never",
    daily: "Daily",
  };
  subAdminList = [];
  constructor(
    private eventService: ManageEventService,
    private subadminService: SubadminService,
    private toastr: ToastrService,
    private modalService: BsModalService,
    private loader: LoaderService,
    private filterService: FilterStorageService
  ) {}

  async ngOnInit() {
    this.filter = this.filterService.getState("eventFilter", this.filter);
    if (this.filter.schedule_date) {
      this.filter.schedule_date = [
        new Date(this.filter.schedule_date[0]),
        new Date(this.filter.schedule_date[1]),
      ];
    }
    this.sortParam = this.filterService.getSingleState(
      "eventSortParam",
      this.sortParam
    );
    this.sortOrder = this.filterService.getSingleState(
      "eventSortOrder",
      this.sortOrder
    );
    this.pageNumber = this.filterService.getSingleState(
      "eventPageNo",
      this.pageNumber
    );
    this.size = this.filterService.getSingleState("eventSize", this.size);
    this.getAllEventList();
    this.getSubAdminList();
    if (await this.checkIfUserAuthenticated()) {
      this.user = this.authInstance.currentUser.get();
      this.accessToken = this.user.getAuthResponse(true).access_token;
    }
  }
  async initGoogleAuth(): Promise<void> {
    //  Create a new Promise where the resolve
    // function is the callback passed to gapi.load
    const pload = new Promise((resolve) => {
      gapi.load("auth2", resolve);
    });

    // When the first promise resolves, it means we have gapi
    // loaded and that we can call gapi.init
    return pload.then(async () => {
      await gapi.auth2
        .init({
          client_id: CONFIGCONSTANTS.clientId,
          scope: CONFIGCONSTANTS.calendarScope,
        })
        .then((auth) => {
          this.isAuthenticate = true;
          this.authInstance = auth;
        });
    });
  }
  async checkIfUserAuthenticated(): Promise<boolean> {
    // Initialize gapi if not done yet
    if (!this.isAuthenticate) {
      await this.initGoogleAuth();
    }

    return this.authInstance.isSignedIn.get();
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
   * Used to fetch sub admin list for created_by filter
   *
   */
  getSubAdminList() {
    this.subadminService
      .getAllSubadminListForDropdown()
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.meta.status === true) {
            this.subAdminList = data.responseData;

            for (var i in this.subAdminList) {
              this.subAdminList[i].fullname =
                this.subAdminList[i].userProfile.firstName +
                " " +
                this.subAdminList[i].userProfile.lastName;
            }
          }
        },
        (error) => {
          console.log(error);
        }
      );
  }

  /**
   * Get Offer list data
   */
  private getAllEventList() {
    // For save filter, sorting, pagenumber and size in storage
    this.filterService.saveState("eventFilter", this.filter);
    this.filterService.saveSingleState("eventSortParam", this.sortParam);
    this.filterService.saveSingleState("eventSortOrder", this.sortOrder);
    this.filterService.saveSingleState("eventPageNo", this.pageNumber);
    this.filterService.saveSingleState("eventSize", this.size);
    this.loadingIndicator = true;
    this.eventService
      .getAllEventList({
        event_name: this.filter.name,
        created_by: this.filter.created_by,
        sort_param: this.sortParam,
        sort_type: this.sortOrder,
        endRec: this.size,
        // page_no: (this.pageNumber + 1),
        startRec: this.pageNumber * this.size,
        from_date: this.filter.schedule_date
          ? moment(this.filter.schedule_date["0"]).format("YYYY-MM-DD")
          : "",
        to_date: this.filter.schedule_date
          ? moment(this.filter.schedule_date["1"]).format("YYYY-MM-DD")
          : "",
      })
      .pipe(first())
      .subscribe(
        (resp) => {
          this.loadingIndicator = false;
          this.eventList = resp.responseData;
          this.totalReords = resp.responseData.length;

          for (var i in this.eventList) {
            this.eventList[i].created_by_name =
              this.eventList[i].createdBy.userProfile.firstName +
              " " +
              this.eventList[i].createdBy.userProfile.lastName;
          }
        },
        (error) => {
          this.loadingIndicator = false;
        }
      );
  }

  /* Open any Modal Popup */
  openModal(template: TemplateRef<any>, id) {
    this.modalRef = this.modalService.show(template, { class: "modal-md" });
    this.eventId = id;
  }

  decline(): void {
    this.modalRef.hide();
  }
  /**
   * Delete Event
   */
  public deleteEvent() {
    this.loader.showLoader();
    this.eventService
      .deleteEvent({ eventId: this.eventId, access_token: this.accessToken })
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
    this.filter.created_by = "";
    this.filter.schedule_date = "";
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
    this.getAllEventList();
  }
}
