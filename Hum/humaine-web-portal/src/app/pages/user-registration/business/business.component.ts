import { IState } from './../../../core/_model/country';
import { Component, Host, OnInit, Optional } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import Validator from '../../../core/_constant/common.validator';
import { UserRegistrationComponent } from '../user-registration.component';
import { CONFIGCONSTANTS } from '../../../core/_constant/app-constant';
import { UserRegistrationService } from '../user-registration.service';
import { ICity } from '../../../core/_model/country';
import { CommonService } from '../../../services/common.service';

@Component({
  selector: 'app-business',
  templateUrl: './business.component.html',
  styleUrls: ['./business.component.scss']
})
export class BusinessComponent implements OnInit {
  onboardingForm: FormGroup;
  isSubmitted: boolean = false;
  eshop = CONFIGCONSTANTS.eshop;
  analytics = CONFIGCONSTANTS.currentAnalytics;
  products = CONFIGCONSTANTS.products;
  employee = CONFIGCONSTANTS.employee;
  consumer = CONFIGCONSTANTS.majorityConsumer;
  industryList = CONFIGCONSTANTS.industryList;
  trackRadio = CONFIGCONSTANTS.trackingFlag;
  trackingData = CONFIGCONSTANTS.trackingProvider;
  businessData = CONFIGCONSTANTS.businessData;
  cities: ICity[] = [];
  states: IState[] = [];
  industryCheckData: FormArray;
  providerCheckData: FormArray;

  constructor(private formBuilder: FormBuilder, 
    @Optional() @Host() private registrationComponent: UserRegistrationComponent,
    private registrationService: UserRegistrationService,
    private commonService:CommonService) { }

  ngOnInit(): void {
    this.onboardingForm = this.formBuilder.group({
      name: ['', Validators.required],
      phone: ['', [Validators.required, Validator.numericValidator, Validators.maxLength(10)]],
      address: ['', Validators.required],
      city: ['', Validators.required],
      state: ['', Validators.required],
      country: ['USA'],
      other: [''],
      industry: this.formBuilder.array([]),
      url: ['', [Validators.required, Validator.URLValidator]],
      eshopHosted: ['', Validators.required],
      headQuarted: ['', Validators.required],
      consumers: [''],
      employee: [''],
      productPrice: [''],
      product: [''],
      analytic: [''],
      homePageUrl: [''],
      isUserDataTrack: [false],
      provider: [],
      trackerDataType: [''],
      expectationComments: ['']
    });
    this.registrationComponent.forms.BUSSINESS_FORM = this.onboardingForm;
    this.industryCheckData = this.formBuilder.array([]);
    this.providerCheckData = this.formBuilder.array([]);
    this.cities = [];
    this.getStates();
  }

  get errorControl() {
    return this.onboardingForm.controls;
  }
  getStates() {
   this.commonService.getStates().subscribe((res: any) => {
     this.states = res;
   });
  }
  onStateChange($event: IState) {
    this.cities = [];
    this.onboardingForm.controls.city.setValue("");
    this.commonService.getCities().subscribe((response: any) => {
      response = response.filter((data) => data.stateCode === $event.isoCode );
      this.cities = response;
    });
  }

  addOnboarding() {
    this.isSubmitted = true;
    if (this.onboardingForm.valid) {
      if (this.onboardingForm.value.other) {
        this.industryCheckData.push(new FormControl(this.onboardingForm.value.other));
      }
      this.onboardingForm.controls['industry'] = this.industryCheckData;
      this.isSubmitted = false;
      this.registrationComponent.forms.BUSSINESS_FORM = this.onboardingForm;
      this.registrationComponent.ngWizardService.next();
    }
  }

  onIndustryChange(e) {
    this.industryCheckData = this.onboardingForm.get('industry') as FormArray;
    let checkedData = [];
    if (e.target.checked) {
      checkedData.push(e.target.value);
      this.industryCheckData.push(new FormControl(e.target.value));
    } else {
      const index = this.industryCheckData.controls.findIndex(x => x.value === e.target.value);
      this.industryCheckData.removeAt(index);
    }
  }

  radioChange(e) {
    this.onboardingForm.value.isUserDataTrack = e.target.value;
    if (e.target.value === "true") {
      this.onboardingForm.controls.provider.enable();
    }
    else {
      this.onboardingForm.controls.provider.disable();
      this.onboardingForm.controls.provider.setValue(false);
    }
  }
}
