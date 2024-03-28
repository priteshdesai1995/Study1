import { environment } from './../../../../environments/environment';
import { Component, Host, OnInit, Optional } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Validator from '../../../core/_constant/common.validator';
import { ToasterService } from '../../../core/_utility/notify.service';
import { AuthenticationService } from '../../../services/authentication.service';
import { UserRegistrationComponent } from '../user-registration.component';
import { UserRegistrationService } from '../user-registration.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss'],
  providers: [UserRegistrationService, ToasterService]
})

export class SignUpComponent implements OnInit {
  signUpForm: FormGroup;
  message: string = "Hola Ankit!"
  verifiedEmailFlag: boolean = false;
  verifyEmailFlag: boolean = false;
  signUpFlag: boolean = true;
  aFormGroup: FormGroup;
  siteKey: string = environment.reCaptchaSiteKey;
  isSubmitted: boolean;
  isCodeSubmitted: boolean;
  codeForm: FormGroup;
  isInvalidCode: boolean = false;
  isSignupSubmit = false;
  isOtpSubmit = false;
  isOnBoardingSubmit = false;
  isOtpVefificationSubmit = false;
  constructor(
    private formBuilder: FormBuilder,
    @Optional() @Host() private registrationComponent: UserRegistrationComponent,
    private authService: AuthenticationService,
    private userRegistrationService: UserRegistrationService,
    private toaster: ToasterService,
  ) {
  }



  ngOnInit(): void {
    this.isOtpVefificationSubmit = false;
    this.isSignupSubmit = false;
    this.isOtpSubmit = false;
    this.isOnBoardingSubmit = false;
    this.signUpForm = this.formBuilder.group({
      name: ['', [Validators.required, Validator.userNameValidator]],
      email: ['', [Validators.required, Validators.maxLength(80), Validator.emailValidator]],
      confirmEmail: ['', [Validators.required, Validators.maxLength(80), Validator.emailValidator]],
      password: ['', [Validators.required, Validator.passwordValidator]],
      confirmPassword: ['', [Validators.required, Validator.passwordValidator]],
      recaptcha: ['', Validators.required]
    }, {
      validators: [this.MustMatch('password', 'confirmPassword'), this.MustMatch('email', 'confirmEmail')]
    });
    this.codeForm = this.formBuilder.group({
      code1: [null, Validators.required],
      code2: [null, Validators.required],
      code3: [null, Validators.required],
      code4: [null, Validators.required],
      code5: [null, Validators.required],
      code6: [null, Validators.required],
    });
    if (this.registrationComponent.isVerificationPending == true) {
          this.signUpForm.controls.email.setValue(this.registrationComponent.signUpData.email);
          this.signUpForm.controls.password.setValue(this.registrationComponent.signUpData.password);
          this.registrationComponent.title = "Let's get you started";
          this.registrationComponent.subTitle = '';
          this.verifyEmailFlag = true;
          this.signUpFlag = false;
          this.verifiedEmailFlag = false;
          this.isSubmitted = false;
    }
  }

  get errorControl() {
    return this.signUpForm.controls;
  }

  get codeErrorControl() {
    return this.codeForm.controls;

  }

  MustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) {
        // return if another validator has already found an error on the matchingControl
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    }
  }

  addSignUp() {
    this.isSubmitted = true;
    if (this.signUpForm.invalid) { return; }
    this.isSignupSubmit = true;
    const encrytedPass = this.authService.getEncryptedString(this.signUpForm.value.password);

    // signUp params
    const params = {
      "email": this.signUpForm.value.email,
      "password": encrytedPass,
      "username": this.signUpForm.value.name
    }
    this.userRegistrationService.signUp(params)
      .subscribe((data) => {
        this.isSignupSubmit = false;
        if (data.statusCode === 200) {
          this.verifyEmailFlag = true;
          this.signUpFlag = false;
          this.verifiedEmailFlag = false;
          this.isSubmitted = false;
          this.registrationComponent.title = "Let's get you started";
          this.registrationComponent.subTitle = '';
          this.registrationComponent.signUpData.accountID = data.responseData.accountID;
          this.registrationComponent.signUpData.email = this.signUpForm.value.email;
          this.registrationComponent.signUpData.password = encrytedPass;
        }
        else {
          const err = data.errorList[0].message;
          this.toaster.errorMsg(err, 'error')
        }

      }, (error) => {
        this.isSignupSubmit = false;
        const err = error.error.errorList[0].message;
        this.toaster.errorMsg(err, 'error')
      });
  }

  codeVerifying(code) {
    this.isCodeSubmitted = true;
    this.isInvalidCode = false;
    this.isOtpVefificationSubmit = true;
    this.isOtpSubmit = true;
    const param = {
      "code": code,
      "email": this.registrationComponent.signUpData.email
    }
    this.userRegistrationService.verifyEmail(param)
      .subscribe((data) => {
        this.isCodeSubmitted = false;
        this.isOtpSubmit = false;
        if (data.statusCode !== 200) {
          this.isInvalidCode = true;
          this.isCodeSubmitted = false;
          return;
        }
        else if (data.statusCode === 200) {
          this.registrationComponent.signUpData.accountID = data.responseData.accountID;
          this.verifyEmailFlag = false;
          this.signUpFlag = false;
          this.verifiedEmailFlag = true;
          this.isInvalidCode = false;
        }

      }, (error) => {
        this.isInvalidCode = true;
        this.isOtpSubmit = false;
        const err = error.error.errorList[0].message;
        this.toaster.errorMsg(err, 'error')
      });
  }

  onboarding() {
    const decryptedPassword = this.authService.getDecryptedString(
      this.registrationComponent.signUpData.password
    );
    this.isOnBoardingSubmit = true;
    this.authService.authenticateCongnito(
      {
        Username: this.registrationComponent.signUpData.email,
        Password: this.signUpForm.value.password
      }
    ).subscribe(result => {
      this.isOnBoardingSubmit = false;
      if (result && result.idToken) {
        this.authService.setLoginToken(result.idToken.jwtToken);
        this.authService.setAccountDetails(
          {
            "accountID": this.registrationComponent.signUpData.accountID
          }
        );
        this.registrationComponent.ngWizardService.next();
        this.registrationComponent.titleFlag = false;
      }
    }, (error) => {
      this.isOnBoardingSubmit = false;
      const err = error.error.errorList[0].message;
      this.toaster.errorMsg(err, 'error')
    });

  }
}
