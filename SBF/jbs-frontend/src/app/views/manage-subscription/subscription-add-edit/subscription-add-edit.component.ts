import {
  Component,
  OnInit,
  ViewChild,
  OnDestroy,
  ElementRef,
  NgModule,
} from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { first } from "rxjs/operators";
import { ToastrService } from "ngx-toastr";
import { NgForm } from "@angular/forms";
import { NgbDateParserFormatter } from "@ng-bootstrap/ng-bootstrap";
import { CookieService } from "ngx-cookie-service";
import * as moment from "moment";
import { LoaderService } from "../../../_services/loader.service";
import { ManageSubscriptionService } from "../../../_services/manage-subscription.service";
import { CONFIGCONSTANTS } from "../../../config/app-constants";

@Component({
  selector: "app-subscription-add-edit",
  templateUrl: "./subscription-add-edit.component.html",
  styleUrls: ["./subscription-add-edit.component.scss"],
})
export class SubscriptionAddEditComponent implements OnInit, OnDestroy {
  plan_name: string;
  description: string;
  selected_days: any;
  validity = "";
  plan_price = "";
  plan_discount = "";
  is_trial_plan = 0;
  plan_type: any = {
    "1": "Yes",
    "0": "No",
  };
  private _id: number;
  editMode = false;
  private editSubscriptionId: number;
  submitted: Boolean = false;
  private routeSub: Subscription;
  private subscriptionData: Subscription;
  private subscriptionDataSave: Subscription;
  model: any = [];
  minDate: any;
  momentDateTime24Format: string;
  momentDateFormat: string;

  constructor(
    private route: ActivatedRoute,
    private manageSubscriptionService: ManageSubscriptionService,
    private router: Router,
    private toastr: ToastrService,
    private _eref: ElementRef,
    private ngbDateParserFormatter: NgbDateParserFormatter,
    private cookieService: CookieService,
    private loader: LoaderService
  ) {
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate());
  }

  ngOnInit() {
    this.momentDateTime24Format =
      CONFIGCONSTANTS.momentDateTime24Format || "MM/DD/YYYY hh:mm:ss";
    this.momentDateFormat = CONFIGCONSTANTS.momentDateFormat || "MM/DD/YYYY";
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params["id"];
      this.editMode = params["id"] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }

  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      /* Set the value to Cookies with Edit Mode */
      this.subscriptionData = this.manageSubscriptionService
        .getManagerSubscriptionById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            this.editSubscriptionId =
              response.responseData.subscriptionId || null;
            this.model.plan_name = response.responseData.name || "";
            this.model.description = response.responseData.description || "";
            this.selected_days =
              [
                new Date(response.responseData.startDate),
                new Date(response.responseData.endDate),
              ] || "";
            this.minDate = new Date(response.responseData.start_date);
            this.validity = response.responseData.validity || "";
            this.plan_price = response.responseData.price || "";
            this.plan_discount = response.responseData.discount || "";
            this.model.is_trial_plan =
              response.responseData.planType === "Trial" ? "1" : "0" || "";
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }

  /**
   * Create/Update subscription data
   * @param frm for validate form
   */
  public onSubscriptionDataSave(frm: NgForm) {
    this.submitted = true;
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    const reqData = {
      name: this.model.plan_name,
      validity: this.validity,
      description: this.model.description,
      start_date: this.selected_days
        ? moment(this.selected_days[0]).format("YYYY-MM-DD")
        : "",
      end_date: this.selected_days
        ? moment(this.selected_days[1]).format("YYYY-MM-DD")
        : "",
      is_trial_plan: this.model.is_trial_plan === "1" ? true : false,
      price: this.model.is_trial_plan === "1" ? "0" : this.plan_price,
      discount: this.model.is_trial_plan === "1" ? "0" : this.plan_discount,
    };

    console.log(this.model.is_trial_plan);

    if (this.editSubscriptionId) {
      this.updateManageSubscription(reqData, this.editSubscriptionId);
    } else {
      this.createManageSubscription(reqData);
    }
  }

  private createManageSubscription(formData) {
    this.loader.showLoader();
    this.subscriptionDataSave = this.manageSubscriptionService
      .createManageSubscription(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/manage-subscription/list"]);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.messageCode === "FORBIDDEN") {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/manage-subscription/list"]);
            this.toastr.error("Subscription Already Exists");
          }
          this.submitted = false;
        }
      );
  }

  private updateManageSubscription(formData, id) {
    this.loader.showLoader();
    this.subscriptionDataSave = this.manageSubscriptionService
      .updateManageSubscription(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/manage-subscription/list"]);
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
            this.router.navigate(["/manage-subscription/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
          this.submitted = false;
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.subscriptionData.unsubscribe();
    }
  }
}
