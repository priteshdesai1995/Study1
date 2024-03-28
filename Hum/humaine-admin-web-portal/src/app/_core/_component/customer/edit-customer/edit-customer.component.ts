import { COMPILER_OPTIONS, Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonService } from 'src/app/services/common.service';
import { UserRegistrationService } from 'src/app/services/user-registration.service';
import { ToasterService } from 'src/app/utility/notify.service';
import { CONFIGCONSTANTS } from 'src/app/_constant/app.constant';
import Validator from 'src/app/_constant/common.validator';
import { IResponse } from 'src/app/_model/response';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.scss']
})
export class EditCustomerComponent implements OnInit {

  onboardingForm: FormGroup;
  isLoading = false;
  isSubmitted: boolean = false;
  industryCheckData: FormArray;
  providerCheckData: FormArray;
  cities: [] = [];
  states: [] = [];
  industryList = CONFIGCONSTANTS.industryList;
  eshop = CONFIGCONSTANTS.eshop;
  employee = CONFIGCONSTANTS.employee;
  products = CONFIGCONSTANTS.products;
  analytics = CONFIGCONSTANTS.currentAnalytics;
  trackRadio = CONFIGCONSTANTS.trackingFlag;
  businessData = CONFIGCONSTANTS.businessData;
  trackingData = CONFIGCONSTANTS.trackingProvider;
  accountId: number;
  accountDetails: any;
  customerControls = CONFIGCONSTANTS.customerControls;
  trackingEvent = CONFIGCONSTANTS.trackingEvents;
  accountEvents: any = {};
  isView: boolean = false;
  isEdit: boolean = false;
  isPageError: boolean = false;
  editRoute: string;
  viewRoute: string;
  usageReview: {} = {};
  frontView = environment.frontUrl;
  public event = '';
  constructor(private formBuilder: FormBuilder, private commonService: CommonService,
    private registerService: UserRegistrationService, private route: ActivatedRoute,
    private router: Router, private toaster: ToasterService,
  ) { }

  ngOnInit(): void {
    this.onboardingForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', Validators.required],
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
      provider: this.formBuilder.array([]),
      trackerDataType: [''],
      expectationComments: [''],
      pageDetailCheck: [''],
      pageUrl: ['']
    });
    this.industryCheckData = this.formBuilder.array([]);
    this.providerCheckData = this.formBuilder.array([]);
    this.cities = [];
    this.getStates();
    this.getAccountDetails();
    this.getUsageReviewData();
  }

  addOnboarding() {
    this.isSubmitted = true;
    if (this.onboardingForm.valid) {
      if (this.onboardingForm.value.other) {
        this.getControlFormArray('industry').push(new FormControl(this.onboardingForm.value.other));
      }
      this.isSubmitted = false;
      let obj = this.onboardingForm.value;
      obj['accountID'] = this.accountId;
      obj['detailedPage'] = this.accountDetails['detailedPage'];
      this.registerService.editCustomer(obj).subscribe((res: any) => {
        if (res.statusCode === 200) {
          this.toaster.successMsg(res.responseData.message)
          setTimeout(() => {
            this.router.navigate(['/customers']);
          }, 2200);
        }
        else {
          const err = res.errorList[0].message;
          this.toaster.errorMsg(err, 'error')
        }
      }, (error: any) => {
        const err = error.error.errorList[0].message;
        this.toaster.errorMsg(err, 'error')
      });
    } else {
      this.toaster.errorMsg("Please fill required fields");
    }
  }

  get errorControl() {
    return this.onboardingForm.controls;
  }

  onStateChange($event: any) {
    this.cities = [];
    this.onboardingForm.controls.city.setValue("");
    this.commonService.getCities().subscribe((response: any) => {
      response = response.filter((data) => data.stateCode === $event.isoCode);
      this.cities = response;
    });
  }
  getStates() {
    this.commonService.getStates().subscribe((res: any) => {
      this.states = res;
    });
  }

  getAccountDetails() {
    this.isLoading = true;
    if (this.route.snapshot.params['id']) {
      this.accountId = this.route.snapshot.params['id'];
    }
    if (this.route.snapshot.data.isView) {
      this.isView = this.route.snapshot.data.isView;
      this.onboardingForm.disable();
    }
    if (this.route.snapshot.data.isEdit) {
      this.isEdit = this.route.snapshot.data.isEdit;
      this.onboardingForm.enable();
    }

    if (this.route.snapshot.data.editPageURL) {
      this.editRoute = this.route.snapshot.data.editPageURL;
    }
    if (this.route.snapshot.data.viewPageURL) {
      this.viewRoute = this.route.snapshot.data.viewPageURL;
    }
    this.registerService.getAccountDetails(this.accountId).subscribe((res: IResponse<Object>) => {
      this.accountDetails = res.responseData;
      this.initializeForm(this.accountDetails);
      this.getControl('email').setValue(this.accountDetails['email']);
      this.isLoading = false;
    });
  }


  initializeForm(accountDetails: {}) {
    for (const key in this.customerControls) {
      let obj = {};
      if (this.customerControls[key] == 'industry' || this.customerControls[key] == 'provider') {
        this.accountDetails[this.customerControls[key]]?.forEach(element => {
          obj['forceInsert'] = true;
          obj['val'] = element;
          this.onCheckboxChange(null, obj, this.customerControls[key]);
        });
      } else if (this.customerControls[key] == 'pageUrl') {
        this.onboardingForm.get(this.customerControls[key]).setValue(this.accountDetails['detailedPage'][0]?.pageURL);
      }
      else {
        this.onboardingForm.get(this.customerControls[key]).setValue(this.accountDetails[this.customerControls[key]]);
      }
    }
    const indus = this.industryList.map((data) => {
      return data.value.trim();
    });
    const other = this.accountDetails['industry'].map(e => e.trim()).filter(e => !indus.includes(e));
    if (other.length > 0) {
      this.onboardingForm.get('other').setValue(other);
    }
  }

  getControlFormArray(controlName): FormArray {
    return this.onboardingForm.get(controlName) as FormArray;
  }
  getControl(controlName): FormControl {
    return this.onboardingForm.get(controlName) as FormControl;
  }

  onCheckboxChange(e, obj = null, control) {
    const checkArray: FormArray = this.onboardingForm.get(control) as FormArray;
    if ((e != null && e.target.checked) || obj?.forceInsert) {
      if (e != null && !obj?.forceInsert)
        checkArray.push(new FormControl(e.target.value));
      else if (e == null && obj.val != null)
        checkArray.push(new FormControl(obj.val));
    } else {
      let i: number = 0;
      checkArray.controls.forEach((item: FormControl) => {
        if (item.value == e.target.value) {
          checkArray.removeAt(i);
          return;
        }
        i++;
      });
    }
  }

  radioChange(e) {
    if (e.target.value == false || e.target.value == 'false') {
      this.getControlFormArray('provider').clear();
    }
    this.onboardingForm.get('isUserDataTrack').setValue(e.target.value);
  }

  navigateByUrl(flag, url = null) {
    if (flag == 'isEdit')
      url = this.editRoute;
    else if (flag == 'isView')
      url = this.viewRoute;
    if (this.accountId)
      url += this.accountId;
    this.router.navigateByUrl(url);
  }

  navigateToFront(){
    window.open(this.frontView, "_blank");
  }

  get isProviderDisabled() {
    const provider = this.onboardingForm.get('isUserDataTrack').value;
    return provider == false || provider == 'false';
  }

  getUsageReviewData() {
    this.registerService.getAccountEventsInfo(this.accountId).subscribe((res: IResponse<any>) => {
      if (res.statusCode === 200) {
        this.accountEvents = res.responseData;
      }
      else {
        const err = res.errorList[0].message;
        this.toaster.errorMsg(err, 'error')
      }
    }, (error: any) => {
      let err;
      if (error.error) {
        err = error?.error?.errorList[0].message;
      } else {
        err = error;
      }
      this.toaster.errorMsg(err, 'error')
    });
  }
}
