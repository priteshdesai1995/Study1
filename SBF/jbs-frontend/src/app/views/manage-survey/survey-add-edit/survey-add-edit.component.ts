import { Component, OnInit, OnDestroy } from "@angular/core";
import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import { SurveyService } from "../../../_services/survey.service";
import { LoaderService } from "../../../_services/loader.service";
import { ToastrService } from "ngx-toastr";
import { first } from "rxjs/operators";
import { NgForm } from "@angular/forms";
import * as moment from "moment";
import { ManageuserService } from "../../../_services/manageuser-service";
import { IDropdownSettings } from "ng-multiselect-dropdown/multiselect.model";
import { CONFIGCONSTANTS } from "../../../config/app-constants";

@Component({
  selector: "app-survey-add-edit",
  templateUrl: "./survey-add-edit.component.html",
  styleUrls: ["./survey-add-edit.component.scss"],
})
export class SurveyAddEditComponent implements OnInit, OnDestroy {
  private _id: number;
  editMode = false;
  editSurveyId: number;
  private routeSub: Subscription;
  private surveySub: Subscription;
  private surveySaveSub: Subscription;
  model: any = {};
  minDate: any;

  locationList = [];
  departmentList = [];
  readonly locationSettings: IDropdownSettings = {
    singleSelection: false,
    idField: "id",
    textField: "name",
    selectAllText: "Select All",
    unSelectAllText: "UnSelect All",
    itemsShowLimit: 3,
    allowSearchFilter: true,
    noDataAvailablePlaceholderText: "No Location Found",
    searchPlaceholderText: "Search Location",
  };
  readonly departmentSettings: IDropdownSettings = {
    singleSelection: false,
    idField: "id",
    textField: "name",
    selectAllText: "Select All",
    unSelectAllText: "UnSelect All",
    itemsShowLimit: 3,
    allowSearchFilter: true,
    noDataAvailablePlaceholderText: "No Department Found",
    searchPlaceholderText: "Search Department",
  };
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private surveyService: SurveyService,
    private manageuserService: ManageuserService
  ) {
    this.minDate = new Date();
    this.minDate.setDate(this.minDate.getDate());
  }

  ngOnInit() {
    this.getDepartmentList();
    this.getLocationList();
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params["id"];
      this.editMode = params["id"] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }
  getDepartmentList() {
    this.manageuserService
      .getActiveCategoryList(CONFIGCONSTANTS.departmentId)
      .pipe(first())
      .subscribe((data) => {
        if (!data.responseData) {
          this.departmentList = [];
        } else {
          this.departmentList = data.responseData;
        }
      });
  }
  getLocationList() {
    this.manageuserService
      .getActiveCategoryList(CONFIGCONSTANTS.locationId)
      .pipe(first())
      .subscribe((data) => {
        if (!data.responseData) {
          this.locationList = [];
        } else {
          this.locationList = data.responseData;
        }
      });
  }
  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      this.surveySub = this.surveyService
        .getSurveyById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            console.log(response.responseData);
            const resData = response.responseData;
            this.editSurveyId = resData.id || null;
            this.model.title = resData.surveyTitle || "";
            this.model.description = resData.description || "";
            if (resData.surveyStatus === "Inprogress") {
              this.model.start_now = true;
            } else {
              this.model.start_now = false;
            }
            this.model.is_started =
              resData.is_started && resData.is_started === "yes";
            //            this.model.department_ids = this.departmentList.filter((ele) => resData.department_ids.includes(ele.id));
            //            this.model.location_ids = this.locationList.filter((ele) => resData.location_ids.includes(ele.id));

            if (!resData.startDate || resData.startDate === "0000-00-00") {
              this.model.survey_start_date = "";
            } else {
              this.model.survey_start_date = moment(resData.startDate).format(
                "MM/DD/YYYY"
              );
            }
            if (!resData.endDate || resData.endDate === "0000-00-00") {
              this.model.survey_end_date = "";
            } else {
              this.model.survey_end_date = moment(resData.endDate).format(
                "MM/DD/YYYY"
              );
            }
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
      dt.setDate(dt.getDate());
      return dt;
    }
  }
  public surveyStartNow(isStart) {
    if (isStart) {
      this.model.survey_start_date = new Date();
    } else {
      this.model.survey_start_date = "";
      this.model.survey_end_date = "";
    }
  }
  /**
   * Create/Update Survey data
   * @param frm for validate form
   */
  public onSurveySave(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    if (
      Date.parse(this.model.survey_start_date) >=
      Date.parse(this.model.survey_end_date)
    ) {
      this.toastr.error("End date should be greater than Start date");
      return;
    }
    const formData: {} = {};
    formData["title"] = this.model.title;
    formData["description"] = this.model.description;
    formData["survey_status"] = this.model.start_now ? "Inprogress" : "Pending";
    formData["status"] = "Active";
    formData["department_ids"] = this.model.department_ids.map((ele) => ele.id);
    formData["location_ids"] = this.model.location_ids.map((ele) => ele.id);
    formData["survey_start_date"] = this.model.survey_start_date
      ? moment(new Date(this.model.survey_start_date)).format("YYYY-MM-DD")
      : "";
    formData["survey_end_date"] = this.model.survey_end_date
      ? moment(new Date(this.model.survey_end_date)).format("YYYY-MM-DD")
      : "";
    if (this.editSurveyId) {
      formData["surveyId"] = this.editSurveyId.toString();
      this.updateSurvey(formData);
    } else {
      this.createSurvey(formData);
    }
  }

  private createSurvey(formData) {
    this.loader.showLoader();
    this.surveySaveSub = this.surveyService
      .createSurvey(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate([
              "/manage-survey/add-questions/" + data.responseData.id,
            ]);
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
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  private updateSurvey(formData) {
    this.loader.showLoader();
    this.surveySaveSub = this.surveyService
      .updateSurvey(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate([
              "/manage-survey/add-questions/" + this.editSurveyId,
            ]);
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
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.surveySub.unsubscribe();
    }
  }
}
