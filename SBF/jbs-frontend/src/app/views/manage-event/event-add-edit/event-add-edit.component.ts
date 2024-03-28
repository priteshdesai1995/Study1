import { Component, OnInit, OnDestroy } from "@angular/core";
import { Subscription, Observable } from "rxjs";
import { ManageEventService } from "../../../_services/manage-event.service";
import { ActivatedRoute, Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../../_services/loader.service";
import { first, map } from "rxjs/operators";
import { NgForm, FormControl } from "@angular/forms";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import * as moment from "moment";
import { CONFIG } from "../../../config/app-config";
import { HttpClient } from "@angular/common/http";

@Component({
  selector: "app-event-add-edit",
  templateUrl: "./event-add-edit.component.html",
  styleUrls: ["./event-add-edit.component.scss"],
})
export class EventAddEditComponent implements OnInit, OnDestroy {
  private _id: number;
  editMode = false;
  private editEventId: number;
  private routeSub: Subscription;
  private eventSub: Subscription;
  private eventSaveSub: Subscription;
  model: any = {};
  momentDateTime24Format =
    CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
  isAuthenticate = false;
  authInstance: gapi.auth2.GoogleAuth;
  error: string;
  user: gapi.auth2.GoogleUser;
  accessToken: string;
  authEmail: string;
  minDate: any;
  public emailValidators = [this.isEmail];

  public emailErrorMessages = {
    isEmail: "Please enter valid email(email@domain.com)",
  };
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private eventService: ManageEventService,
    private http: HttpClient
  ) {
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate());
  }

  async ngOnInit() {
    this.model.send_notification = false;
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
      this.authEmail = this.user.getBasicProfile().getEmail();
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
  async authenticate(): Promise<gapi.auth2.GoogleUser> {
    // Initialize gapi if not done yet
    if (!this.isAuthenticate) {
      await this.initGoogleAuth();
    }

    // Resolve or reject signin Promise
    return new Promise(async () => {
      await this.authInstance.signIn().then(
        (user) => {
          this.user = user;
          this.accessToken = this.user.getAuthResponse(true).access_token;
          this.authEmail = this.user.getBasicProfile().getEmail();
        },
        (error) => (this.error = error)
      );
    });
  }
  // type = 'date'
  maxEndDate(date) {
    if (date) {
      const dt = new Date(date);
      dt.setDate(dt.getDate() + 1);
      return dt;
    }
  }

  /**
   * Used to set min date for Recurrence date picker
   *
   * @param startDate
   */
  setRecurrenceMinDate(startDate) {
    if (startDate) {
      const recMinDate = new Date(startDate);
      recMinDate.setDate(recMinDate.getDate() + 1);
      return recMinDate;
    }
  }

  /**
   * USed to set max date for Recurrence date picker
   *
   * @param endDate
   */
  setRecurrenceMaxDate(endDate) {
    if (endDate) {
      const recMaxDate = new Date(endDate);
      recMaxDate.setDate(recMaxDate.getDate() - 1);
      return recMaxDate;
    }
  }

  private isEmail(control: FormControl) {
    const EMAIL_REGEXP = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (
      control.value !== "" &&
      (control.value.length <= 5 || !EMAIL_REGEXP.test(control.value))
    ) {
      return { isEmail: true };
    }
    return null;
  }
  public requestAutocompleteItems = (text: string): Observable<String> => {
    const url = CONFIG.getEventActiveUserURL + `?search=${text}`;
    return this.http
      .get(url)
      .pipe(
        map((data: any) =>
          data.responseData.map((item) => item.userProfileEntity.email)
        )
      );
  };
  /**
   * Selected input value in edit mode
   */
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
            this.model.address = resData.address;
            this.model.send_notification =
              resData.sendNotifications === "true" ? true : false;
            this.model.repeated = resData.recurrence || "";
            this.model.attendees =
              response.responseData.emails != ""
                ? response.responseData.emails.split(",")
                : "";
            if (
              !resData.recurrenceUpto ||
              resData.recurrenceUpto === "0000-00-00 00:00:00"
            ) {
              this.model.recurrence_upto = "";
            } else {
              this.model.recurrence_upto = moment(
                resData.recurrenceUpto
              ).format("MM/DD/YYYY");
            }
            if (!resData.startDate || resData.startDate === "0000-00-00") {
              this.model.start_datetime = "";
            } else {
              this.model.start_datetime = new Date(resData.startDate);
            }
            if (!resData.endDate || resData.endDate === "0000-00-00") {
              this.model.end_datetime = "";
            } else {
              this.model.end_datetime = new Date(resData.endDate);
            }
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
  /**
   * Create/Update event data
   * @param frm for validate form
   */
  public onEventSave(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    const reqData = {
      eventName: this.model.eventname,
      eventDescription: this.model.description,
      eventAddress: this.model.address ? this.model.address : "",
      eventStartDateTime: this.model.start_datetime
        ? moment(this.model.start_datetime).format("YYYY-MM-DD, HH:mm:ss")
        : "",
      eventEndDateTime: this.model.end_datetime
        ? moment(this.model.end_datetime).format("YYYY-MM-DD, HH:mm:ss")
        : "",
      recurrence: this.model.repeated,
      recurrence_upto:
        this.model.repeated === "daily"
          ? moment(this.model.recurrence_upto).format("YYYY-MM-DD")
          : "",
      access_token: this.accessToken,
      email_id: this.authEmail,
      sendNotifications: this.model.send_notification ? "true" : "false",
      userId: JSON.parse(localStorage.getItem("user")).userId,
      // sendUpdates:'all'
    };
    if (this.editEventId) {
      var attendees = "";
      this.model.attendees.forEach((val: any, key: any) => {
        attendees += val.display ? val.display : val + ",";
      });
      reqData["add_attendee"] =
        attendees.length > 0 ? attendees.toString() : "";
      reqData["eventId"] = this.editEventId;
      this.updateEvent(reqData);
    } else {
      this.createEvent(reqData);
    }
  }

  private createEvent(formData) {
    this.loader.showLoader();
    this.eventSaveSub = this.eventService
      .createEvent(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/event/list"]);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/event/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  private updateEvent(formData) {
    this.loader.showLoader();
    this.eventSaveSub = this.eventService
      .updateEvent(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/event/list"]);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/event/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.eventSub.unsubscribe();
    }
  }
}
