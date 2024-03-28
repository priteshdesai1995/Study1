import { Component, OnInit, OnDestroy } from "@angular/core";
import { Subscription } from "rxjs";
import { ActivatedRoute, Router } from "@angular/router";
import { LocationService } from "../../../../_services/location.service";
import { ToastrService } from "ngx-toastr";
import { LoaderService } from "../../../../_services/loader.service";
import { first } from "rxjs/operators";
import { NgForm } from "@angular/forms";
import { MultilingualService } from "../../../../_services/multilingual.service";

@Component({
  selector: "app-city-add-edit",
  templateUrl: "./city-add-edit.component.html",
  styleUrls: ["./city-add-edit.component.scss"],
})
export class CityAddEditComponent implements OnInit, OnDestroy {
  stateList = [];
  private _id: number;
  editMode = false;
  editCityId: number;
  routeSub: Subscription;
  citySub: Subscription;
  citySaveSub: Subscription;
  model: any = {
    countryId: "",
    stateId: "",
    cityName: {},
    status: "Active",
    uuid: {},
  };
  countryList = [];
  languages = [];
  constructor(
    private route: ActivatedRoute,
    private locationService: LocationService,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private multilingualService: MultilingualService
  ) {
    this.languages = this.multilingualService.getLanguage();
  }

  ngOnInit() {
    this.getCountryList();
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params["id"];
      this.editMode = params["id"] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }
  getStateList() {
    this.locationService
      .getActiveState(this.model.countryId)
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
  initForm() {
    if (this.editMode) {
      this.citySub = this.locationService
        .getCityById(this._id)
        .pipe(first())
        .subscribe((response) => {
          const resData = response.responseData;
          this.editCityId = this._id;
          this.model.countryId = resData.countryId || "";
          this.model.stateId = resData.stateId || "";
          this.model.status = resData.status || "Active";
          resData.cityTranslableDtos.forEach((element) => {
            this.model.cityName[element.locale] = element.name;
            this.model.uuid[element.locale] = element.uuid;
          });
          this.getStateList();
        });
    }
  }
  onCitySave(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    const data = {
      countryUuid: this.model.countryId,
      stateUuid: this.model.stateId,
      locales: this.model.cityName,
      status: this.model.status,
      cityTranslableDtos: [],
    };
    for (var e in this.model.cityName) {
      data.cityTranslableDtos.push({ locale: e, name: this.model.cityName[e] });
    }
    if (this.editCityId) {
      for (var e in this.model.uuid) {
        var index = data.cityTranslableDtos.findIndex(
          (element) => element.locale === e
        );
        data.cityTranslableDtos[index]["uuid"] = this.model.uuid[e];
      }
      this.updateCity(data, this.editCityId);
    } else {
      this.createCity(data);
    }
  }

  createCity(formData) {
    this.loader.showLoader();
    this.citySaveSub = this.locationService
      .createCity(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/location/city"]);
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
            this.router.navigate(["/location/city"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  updateCity(formData, id) {
    this.loader.showLoader();
    this.citySaveSub = this.locationService
      .updateCity(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/location/city"]);
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
            this.router.navigate(["/location/city"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.citySub.unsubscribe();
    }
  }
}
