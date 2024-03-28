import { Component, OnInit, OnDestroy } from '@angular/core';
import { LocationService } from '../../../../_services/location.service';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../../../_services/loader.service';
import { first } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { MultilingualService } from '../../../../_services/multilingual.service';

@Component({
  selector: 'app-state-add-edit',
  templateUrl: './state-add-edit.component.html',
  styleUrls: ['./state-add-edit.component.scss'],
})
export class StateAddEditComponent implements OnInit, OnDestroy {
  countryList = [];
  private _id: string;
  editMode = false;
  editStateId: number;
  routeSub: Subscription;
  stateSub: Subscription;
  stateSaveSub: Subscription;
  model: any = {
    countryId: '',
    stateName: {},
    stateCode: '',
    status: 'Active',
    uuid: {}
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
    this.getCountryList();
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
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
      this.stateSub = this.locationService
        .getStateById(this._id)
        .pipe(first())
        .subscribe((response) => {
          const resData = response.responseData;
          this.editStateId = resData.uuid || null;
          this.model.countryId = resData.countryId || '';
          this.model.stateCode = resData.stateCode || '';
          this.model.status = resData.status || 'Active';
          resData.stateDtos.forEach((element) => {
            this.model.stateName[element.locale] = element.name;
            this.model.uuid[element.locale] = element.uuid;
          });
        });
    }
  }
  onStateSave(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    const data = {
      countryId: this.model.countryId,
      stateDtos: [],
      stateCode: this.model.stateCode,
      status: this.model.status,
    };

    for (var e in this.model.stateName) {
      data.stateDtos.push({ locale: e, name: this.model.stateName[e] })
    }
    if (this.editStateId) {
      for (var e in this.model.uuid) {
        var index = data.stateDtos.findIndex(element => element.locale === e);
        data.stateDtos[index]['uuid'] = this.model.uuid[e];
      }
    }
    if (this.editStateId) {
      this.updateState(data, this.editStateId);
    } else {
      this.createState(data);
    }
  }

  createState(formData) {
    this.loader.showLoader();
    this.stateSaveSub = this.locationService
      .createState(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/location/state']);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === 'VALIDATION_ERROR') {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(['/location/state']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  updateState(formData, id) {
    this.loader.showLoader();
    this.stateSaveSub = this.locationService
      .updateState(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/location/state']);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === 'VALIDATION_ERROR') {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            } else {
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(['/location/state']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.stateSub.unsubscribe();
    }
  }
}
