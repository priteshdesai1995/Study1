import { Component, OnInit, ViewChild } from "@angular/core";
import { Subscription } from "rxjs";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../../_services/loader.service";
import { ManageEventService } from "../../../_services/manage-event.service";
import { first } from "rxjs/operators";
import { ColumnMode, DatatableComponent } from "@swimlane/ngx-datatable";

@Component({
  selector: "app-event-view",
  templateUrl: "./event-view.component.html",
  styleUrls: ["./event-view.component.scss"],
})
export class EventViewComponent implements OnInit {
  private _id: number;
  editMode = false;
  private editEventId: number;
  private routeSub: Subscription;
  private eventSub: Subscription;
  model: any = {};
  momentDateTime24Format = "MM/DD/YYYY hh:mm:ss A";
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
  participantsList = [];
  global_search = "";
  private filteredData = [];
  // Get datatble configuration -- end
  loadingIndicator = false;
  repeated = {
    never: "Never",
    daily: "Daily",
  };
  accessToken: string;
  authInstance: gapi.auth2.GoogleAuth;
  error: string;
  user: gapi.auth2.GoogleUser;
  isAuthenticate = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private eventService: ManageEventService
  ) {}

  async ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params["id"];
      this.editMode = params["id"] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
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
   * Show number of records in datatable
   * @param value show total entries value 10,25,50,100
   */
  public changeLimit(value) {
    this.size = value;
  }
  private initForm() {
    if (this.editMode) {
      this.eventSub = this.eventService
        .getEventById(this._id, { access_token: this.accessToken })
        .pipe(first())
        .subscribe(
          (response) => {
            const resData = response.responseData.event;
            this.editEventId = resData.id || null;
            this.model.eventname = resData.name || "";
            this.model.description = resData.description || "";
            this.model.repeated = resData.recurrence || "";
            this.model.send_notification = resData.sendNotifications;
            this.model.start_datetime = resData.startDate;
            this.model.end_datetime = resData.endDate;
            this.model.google_meet_link = resData.google_event_link;
            this.model.calendar_link = resData.google_calendar_link;
            this.model.organizer_email_id = resData.emailId;
            this.participantsList = response.responseData.participants;
            this.filteredData = response.responseData.participants;
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
  /**
   * Datatabe global search
   * @param event get search input value
   */
  public filterDatatable() {
    // const val = event.target.value.toLowerCase();
    const val = this.global_search.toLowerCase();
    // get the key names of each column in the dataset
    const keys = ["email", "status"];
    // assign filtered matches to the active datatable
    this.participantsList = this.filteredData.filter(function (item) {
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
    this.datatable.offset = 0;
  }
}
