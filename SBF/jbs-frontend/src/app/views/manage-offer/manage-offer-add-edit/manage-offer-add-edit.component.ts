import { Component, OnInit, ViewChild, OnDestroy } from "@angular/core";
import { ActivatedRoute, Params, Router } from "@angular/router";
import { Subscription } from "rxjs";
import { first } from "rxjs/operators";
import { ToastrService } from "ngx-toastr";
import { CONFIGCONSTANTS } from "../../../config/app-constants";
import * as moment from "moment";
import { LoaderService } from "../../../_services/loader.service";
import { ManageOfferService } from "../../../_services/manage-offer.service";
import { IDropdownSettings } from "ng-multiselect-dropdown/multiselect.model";

@Component({
  selector: "app-manage-offer-add-edit",
  templateUrl: "./manage-offer-add-edit.component.html",
  styleUrls: ["./manage-offer-add-edit.component.scss"],
})
export class ManageOfferAddEditComponent implements OnInit, OnDestroy {
  @ViewChild("offerFrm", { static: true }) form: any;

  momentDateTime24Format: string;
  momentDateFormat: string;
  private offerData: Subscription;
  routeSub: Subscription;
  private _id: number;
  editMode = false;
  model: any = [];
  minDate: any;
  private editOfferId: number;
  private offerDataSave: Subscription;

  userList = [];
  readonly userSettings: IDropdownSettings = {
    singleSelection: false,
    idField: "id",
    textField: "userName",
    selectAllText: "Select All",
    unSelectAllText: "UnSelect All",
    itemsShowLimit: 4,
    allowSearchFilter: true,
    noDataAvailablePlaceholderText: "No User Found",
    searchPlaceholderText: "Search User",
  };
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private manageOfferService: ManageOfferService,
    private loader: LoaderService
  ) {
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate());
  }

  ngOnInit() {
    this.model.offer_type = "0";
    this.model.applicable = "0";
    this.model.offer_usage = "1";
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
    this.getActiveUser();
  }

  private initForm() {
    if (this.editMode) {
      this.offerData = this.manageOfferService
        .getManageOfferById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            this.editOfferId = response.responseData.offer.id || null;
            const resData = response.responseData.offer;
            this.model.offer_name = resData.name || "";
            this.model.offer_code = resData.code || "";
            this.model.offer_type = resData.type || "";
            this.model.offer_value = resData.value || "";
            this.model.offer_usage = resData.usage || "";
            if (response.responseData.users.length > 0) {
              this.model.user_ids = response.responseData.users;
            }
            if (!resData.startDate || resData.startDate === "0000-00-00") {
              this.model.start_date = "";
            } else {
              this.model.start_date = moment(resData.startDate).format(
                "MM/DD/YYYY"
              );
            }
            if (!resData.endDate || resData.endDate === "0000-00-00") {
              this.model.end_date = "";
            } else {
              this.model.end_date = moment(resData.endDate).format(
                "MM/DD/YYYY"
              );
            }
            this.model.applicable = resData.applicable || "";
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
  maxEndDate(date) {
    if (date) {
      const dt = new Date(date);
      dt.setDate(dt.getDate() + 1);
      return dt;
    }
  }
  /**
   * For User List
   */
  getActiveUser() {
    this.manageOfferService
      .getActiveUser()
      .pipe(first())
      .subscribe(
        (resp) => {
          this.userList = resp.responseData;
        },
        (error) => {
          this.userList = [];
        }
      );
  }
  /**
   * For add and Edit Offer Save Click
   */
  onOfferSave() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    if (Date.parse(this.model.start_date) >= Date.parse(this.model.end_date)) {
      this.toastr.error("End date should be greater than Start date");
      return;
    }
    const reqData = {
      name: this.model.offer_name,
      code: this.model.offer_code,
      type: this.model.offer_type,
      value: this.model.offer_value ? parseInt(this.model.offer_value, 10) : "",
      start_date: this.model.start_date
        ? moment(new Date(this.model.start_date)).format("YYYY-MM-DD")
        : "",
      end_date: this.model.end_date
        ? moment(new Date(this.model.end_date)).format("YYYY-MM-DD")
        : "",
      applicable: this.model.applicable,
      usage: this.model.offer_usage,
      users:
        this.model.applicable === "1"
          ? this.model.user_ids.map((ele) => ele.id).toString()
          : this.userList.map((ele) => ele.id).toString(),
    };
    if (this.editOfferId) {
      this.updateManageOffer(reqData, this.editOfferId);
    } else {
      this.createOffer(reqData);
    }
  }

  /**
   * for Create Api call with Parameters
   * @param reqData
   */
  createOffer(reqData) {
    this.loader.showLoader();
    this.manageOfferService
      .createManageOffer(reqData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/manage-offer/list"]);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === "VALIDATION_ERROR") {
              for (const key in errorData.errors) {
                if (key) {
                  this.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              this.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(["/manage-offer/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  /**
   * for Update Api call with Parameters
   * @param reqData
   * @param id
   */
  updateManageOffer(reqData, id) {
    this.loader.showLoader();
    this.offerDataSave = this.manageOfferService
      .updateManageOffer(reqData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/manage-offer/list"]);
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
            this.router.navigate(["/manage-offer/list"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
