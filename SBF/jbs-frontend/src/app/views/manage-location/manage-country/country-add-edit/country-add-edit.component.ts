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
  selector: "app-country-add-edit",
  templateUrl: "./country-add-edit.component.html",
  styleUrls: ["./country-add-edit.component.scss"],
})
export class CountryAddEditComponent implements OnInit, OnDestroy {
  private _id: number;
  editMode = false;
  editCountryId: number;
  routeSub: Subscription;
  countrySub: Subscription;
  countrySaveSub: Subscription;
  model: any = {
    countryName: {},
    countryCode: "",
    status: "Active",
    uuid: {},
  };
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
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params["id"];
      this.editMode = params["id"] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }
  initForm() {
    if (this.editMode) {
      this.countrySub = this.locationService
        .getCountryById(this._id)
        .pipe(first())
        .subscribe((response) => {
          const resData = response.responseData;
          this.editCountryId = this._id;
          this.model.countryCode = response.responseData.countryCode || "";
          this.model.status = response.responseData.status || "Active";
          resData.locationtranslableDtos.forEach((element) => {
            this.model.countryName[element.locale] = element.name;
            this.model.uuid[element.locale] = element.transId;
          });
        });
    }
  }

  onCountrySave(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    var locationtranslableDtos: any[] = [];
    var temp = [this.model.countryName];
    temp.forEach(function (item) {
      Object.keys(item).forEach(function (key) {
        let obj = {
          locale: key,
          name: item[key],
        };
        locationtranslableDtos.push(obj);
      });
    });

    if (this.editCountryId) {
      let temp = [this.model.uuid];
      temp.forEach(function (item) {
        Object.keys(item).forEach(function (key) {
          var index = locationtranslableDtos.findIndex(
            (element) => element.locale === key
          );
          locationtranslableDtos[index]["transId"] = item[key];
        });
      });
    }
    const data = {
      locationtranslableDtos: locationtranslableDtos,
      countryCode: this.model.countryCode,
      status: this.model.status,
    };
    if (this.editCountryId) {
      this.updateCountry(data, this.editCountryId);
    } else {
      this.createCountry(data);
    }
  }

  createCountry(formData) {
    this.loader.showLoader();
    this.countrySaveSub = this.locationService
      .createCountry(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/location/country"]);
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
            this.router.navigate(["/location/country"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }

  updateCountry(formData, id) {
    this.loader.showLoader();
    this.countrySaveSub = this.locationService
      .updateCountry(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(["/location/country"]);
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
            this.router.navigate(["/location/country"]);
            this.toastr.error("Something went wrong please try again.");
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.countrySub.unsubscribe();
    }
  }
}
