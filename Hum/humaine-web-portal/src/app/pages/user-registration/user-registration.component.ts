import { CONFIGCONSTANTS } from './../../core/_constant/app-constant';
import { AuthenticationService } from './../../services/authentication.service';
import { getValueByKey, isEmpty } from './../../core/_utility/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Location } from '@angular/common';
import { of } from 'rxjs';
import { NgWizardComponent, NgWizardConfig, NgWizardService, StepChangedArgs, StepValidationArgs, STEP_STATE, THEME } from 'ng-wizard';
import { FormGroup } from '@angular/forms';
import { BusinessComponent } from './business/business.component';
import { signUpData } from '../../core/_model/SignUp';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.scss']
})
export class UserRegistrationComponent implements OnInit {
  @ViewChild(NgWizardComponent, { static: true }) child: NgWizardComponent;
  forms:{
    BUSSINESS_FORM: FormGroup;
  } = {
    BUSSINESS_FORM:null
  }
  public userReturnFromLoginPage = false;
  public signUpData  : signUpData = {
    accountID : null,
    email :'',
    password: ''

  };
  titleFlag: boolean = true;
  title: string = "You're one step closer to better understanding your customers.";
  subTitle:string = "Glad to see you taking your first steps in making your brand and your connection with your customers better!";
  stepStates = {
    normal: STEP_STATE.normal,
    disabled: STEP_STATE.disabled,
    error: STEP_STATE.error,
    hidden: STEP_STATE.hidden
  };
  config: NgWizardConfig = {
    selected: 0,
    theme: THEME.circles,
    toolbarSettings: {
      showNextButton: false, showPreviousButton: false,
    },
    anchorSettings: {
      anchorClickable: false
    }
  };
  isValidTypeBoolean: boolean = true;

  constructor(public ngWizardService: NgWizardService, private location: Location, private authService: AuthenticationService) {
    this.userReturnFromLoginPage = false;
  }

  getLocationState() {
    const state = this.location.getState();
    const emailConfirmationPending = getValueByKey(state, 'emailConfirmationPending', false);
    const email = getValueByKey(state, 'email', null);
    const password = getValueByKey(state, 'password', null);
    return  {
      emailConfirmationPending,
      email,
      password
    }
  }
  ngAfterViewInit() {
    this.authService.checkIfRegistrationPending(false, (val) => {
      const accountDetail = this.authService.getAccountDetails();
      this.signUpData.accountID = getValueByKey(accountDetail, 'accountID', null)
      this.ngWizardService.show(CONFIGCONSTANTS.registrationSteps.BUSINESS);
      this.titleFlag = false;
    })
  }

  ngOnInit() {
    if (this.isVerificationPending === true) {
      const state = this.getLocationState();
      this.signUpData.email = state.email;
      this.signUpData.password = state.password;
    }
  }

  resetWizard(event?: Event) {
    this.ngWizardService.reset();
  }

  setTheme(theme: THEME) {
    this.ngWizardService.theme(theme);
  }

  isValidFunctionReturnsBoolean(args: StepValidationArgs) {
    return true;
  }

  isValidFunctionReturnsObservable(args: StepValidationArgs) {
    return of(true);
  }
  public get isVerificationPending() {
    const obj = this.getLocationState();
    return obj.emailConfirmationPending === true && !isEmpty(obj.email) && !isEmpty(obj.password);
  }
}
