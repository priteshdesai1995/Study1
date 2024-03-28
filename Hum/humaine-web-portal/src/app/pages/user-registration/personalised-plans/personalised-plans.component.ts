import { CONFIGCONSTANTS } from './../../../core/_constant/app-constant';
import { Component, Host, OnInit, Optional } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Account, DetailedPage } from '../../../core/_model/Account';
import { BusinessComponent } from '../business/business.component';
import { UserRegistrationComponent } from '../user-registration.component';
import { UserRegistrationService } from '../user-registration.service';
import { ToasterService } from '../../../core/_utility/notify.service';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../../services/authentication.service';
import { isEmpty } from '../../../core/_utility/common';
import Validator from '../../../core/_constant/common.validator';

@Component({
  selector: 'app-personalised-plans',
  templateUrl: './personalised-plans.component.html',
  styleUrls: ['./personalised-plans.component.scss']
})
export class PersonalisedPlansComponent implements OnInit {
  pageDetailFlag: boolean = false;
  addPageForm: FormGroup;
  urlForm: FormGroup;
  generatedFlag: boolean = false;
  trackerDetailFlag: boolean = true;
  isWebsite: boolean = false;
  isPageError: boolean = false;
  public newPagesArray: FormArray;
  isPageDetailFlag: boolean = false;
  homePageUrl: string;
  trackingData = CONFIGCONSTANTS.trackingProvider;
  accountData: any = {};
  isTrackerSubmit = false;
  generateTrackerSubmit = false;
  generateTrackerForUrlSubmit = false;
  trackingEvent = CONFIGCONSTANTS.trackingEvents;

  constructor(private formBuilder: FormBuilder,
    private fb: FormBuilder,
    @Optional() @Host() public registration: UserRegistrationComponent,
    private registerService: UserRegistrationService,
    private toaster: ToasterService,
    private router: Router,
    private authService: AuthenticationService) { }

  ngOnInit(): void {
    this.urlForm = this.formBuilder.group({
      websiteUrl: ['',[ Validators.required,Validator.URLValidator]],
      pageDetailCheck: [false]
    });

    this.addPageForm = this.formBuilder.group({
      landingPgae: ['', [ Validators.required,Validator.URLValidator]],
      collectionPage: ['', [ Validators.required,Validator.URLValidator]],
      productDetail: ['',[ Validators.required,Validator.URLValidator]],
      cartPage: ['', [ Validators.required,Validator.URLValidator]],
      checkoutPage: ['', [ Validators.required,Validator.URLValidator]],
      newPagesArray: this.formBuilder.array([this.createPage()])
    })
  }

  get newPageControl() {
    return this.addPageForm.get('newPagesArray')['controls'];
  }


  createPage(): FormGroup {
    return this.fb.group({
      pageName: '',
      pageURL: ''
    });
  }

  addNewPage(): void {
    this.newPagesArray = this.addPageForm.get('newPagesArray') as FormArray;
    this.newPagesArray.push(this.createPage());
  }

  removePage(i: number) {
    this.newPagesArray.removeAt(i);
  }

  logValue() {
  }


  get pageDetailErrorControl() {
    return this.addPageForm.controls;
  }

  get urlErrorControl() {
    return this.urlForm.controls;
  }

  setAccountDetails() {
    this.authService.setAccountDetails({ ...this.getAccountData(), status: true });
  }

  saveRow() {
  }

  onDetailChange(data) {
    if (!this.urlForm.valid) {
      this.generateTrackerForUrlSubmit = true;
      this.urlForm.controls.pageDetailCheck.setValue(!this.urlForm.controls.pageDetailCheck.value);
      return;
    }
  }
  backBtn() {
    if (this.isPageDetailFlag) {
      this.isPageDetailFlag = false;
      return;
    }
    this.registration.ngWizardService.show(CONFIGCONSTANTS.registrationSteps.BUSINESS);
  }

  getAccountData() {
    const businessData = this.registration.forms.BUSSINESS_FORM.value;
    let detailPage: DetailedPage[];
    if (this.isPageDetailFlag) {
      detailPage = [
        {
          pageName: 'Home/Landing Page',
          pageURL: this.addPageForm.value.landingPgae
        },
        {
          pageName: 'Product List/ Collection Page',
          pageURL: this.addPageForm.value.collectionPage
        },
        {
          pageName: "Cart Page",
          pageURL: this.addPageForm.value.cartPage
        },
        {
          pageName: "Buy / Checkout Page",
          pageURL: this.addPageForm.value.checkoutPage
        },
        {
          pageName: "Product Details Page",
          pageURL: this.addPageForm.value.productDetail
        }
      ];
      this.addPageForm.value.newPagesArray.forEach(element => {
        if (element.pageURL !== '' && element.pageTitle !== '') {
          detailPage.push(element);
        }
      });
    }
    else {
      detailPage = [];
    }
    this.accountData.accountID = this.registration.signUpData.accountID;
    this.accountData.address = businessData.address;
    this.accountData.businessURL = businessData.url;
    this.accountData.city = businessData.city;
    this.accountData.cosumersFrom = businessData.consumers;
    this.accountData.country = businessData.country;
    this.accountData.curAnalyticsTool = businessData.analytic;
    this.accountData.eshopHostedOn = businessData.eshopHosted;
    this.accountData.expectationComments = businessData.expectationComments;
    this.accountData.fullName = businessData.name;
    this.accountData.headquarterLocation = businessData.headQuarted;
    this.accountData.highProductPrice = businessData.productPrice;
    this.accountData.homePageUrl = this.homePageUrl || this.urlForm.value.websiteUrl;
    this.accountData.industry = businessData.industry;
    this.accountData.isUserDataTrack = businessData.isUserDataTrack;
    this.accountData.noOfEmployees = businessData.employee;
    this.accountData.noOfProducts = businessData.product;
    this.accountData.phoneNumber = businessData.phone;
    this.accountData.state = businessData.state;
    this.accountData.trackerDataType = businessData.trackerDataType;
    this.accountData.trackingProviders = this.getProviderData(businessData.provider);
    this.accountData.detailedPage = detailPage;
    return this.accountData;
  }

  getProviderData(data) {
    const values = [];
    if (isEmpty(data)) return values;
    if (data === "Both") {
      this.trackingData.forEach(t => {
        if (t.value !== 'Both') {
          values.push(t.value)
        }
      });
    } else {
      values.push(data);
    }
    return values;
  }
  get isDetailPageChecked() {
    return this.urlForm.controls.pageDetailCheck.value === true;
  }
  changePersnolizedPage() {
    this.generateTrackerForUrlSubmit = true;
    if (this.urlForm.invalid) {
      return false;
    }
    const value = this.urlForm.controls.pageDetailCheck.value === true;
    if (value) {
      this.isPageDetailFlag = true;      
    }
  }
  generateTracker() {
    this.generateTrackerSubmit = true;
    this.isPageError = true;
    if (this.addPageForm.invalid) return false;
    if (this.isDetailPageChecked) {
      this.submitTracker();
    }
  }
  generateTrackerForUrl() {
    this.generateTrackerForUrlSubmit = true;
    if (this.urlForm.invalid) return false;
    if (!this.isDetailPageChecked) {
      this.submitTracker();
    }
  }
  submitTracker() {
    if (this.isTrackerSubmit) return false;
    this.isTrackerSubmit = true;
    this.registerService.registerAccount(this.getAccountData()).subscribe((res: any) => {
      this.isTrackerSubmit = false;
      if (res.statusCode === 200) {
        this.generatedFlag = true;
        this.isPageError = false
        this.isPageDetailFlag = false;
        this.setAccountDetails();
        setTimeout(() => {
          this.router.navigate(['/dashboard']);
        }, 2200);
      }
      else {
        const err = res.errorList[0].message;
        this.toaster.errorMsg(err, 'error')
      }
    }, (error: any) => {
      this.isTrackerSubmit = false;
      const err = error.error.errorList[0].message;
      this.toaster.errorMsg(err, 'error')
    })
  }
}
