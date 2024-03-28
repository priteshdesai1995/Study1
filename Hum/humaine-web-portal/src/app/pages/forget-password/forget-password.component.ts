import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Validator from '../../core/_constant/common.validator';
import { ToasterService } from '../../core/_utility/notify.service';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.scss']
})
export class ForgetPasswordComponent implements OnInit {
  isResest: boolean = true;
  isSubmitted: boolean = false;
  resetForm: FormGroup;
  isOtpFlag: boolean = false;
  passwordForm: FormGroup;
  isPassword: boolean = false;
  isPasswordChanged: boolean = false;
  isOtpVefificationSubmit = false;
  isInvalidCode: boolean = false;
  otpCode: string = '';
  isEmailSubmit: boolean = false;
  isPasswordSubmit: boolean = false;

  constructor(private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private toaster: ToasterService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.resetForm = this.formBuilder.group({
      email: ['',
        [
          Validators.required,
          Validators.maxLength(80),
          Validator.emailValidator
        ]
      ],
    });

    this.passwordForm = this.formBuilder.group({
      password: ['',
        [
          Validators.required,
          Validator.passwordValidator
        ]
      ],
      confirmPassword: ['',
        [
          Validators.required,
          Validator.passwordValidator
        ]
      ],
    }, {
      validators: [
        this.MustMatch('password', 'confirmPassword')
      ]
    });
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

  get errorControl() {
    return this.resetForm.controls;
  }

  get passErrorControl() {
    return this.passwordForm.controls;
  }

  emailVerify() {
    this.isSubmitted = true;
    if (this.resetForm.valid) {
      this.isSubmitted = false;
      this.isResest = true;
      this.isEmailSubmit = true;
      this.authService
        .forgetPasswordCongnito({
          Username: this.resetForm.value.email,
        })
        .subscribe(result => {
          this.isEmailSubmit = false;
          if (result.CodeDeliveryDetails) {
            this.isOtpFlag = true;
            this.isResest = false;
            this.isPasswordChanged = false;
          }

        }, (error) => {
          this.isEmailSubmit = false;
          this.toaster.errorMsg(error.message, "error");
        });
    }
  }

  codeVerifying(code) {
    this.isOtpVefificationSubmit = true;
    this.isPasswordChanged = true;
    this.isResest = false;
    this.isOtpFlag = false;
    this.otpCode = code;
  }

  passReset() {
    this.isPassword = true;
    if (this.passwordForm.invalid) { return true; }
    this.isPassword = false;
    this.isPasswordSubmit = true;
    let data = {
      verificationCode: this.otpCode,
      Username: this.resetForm.value.email,
      newPassword: this.passwordForm.value.password
    }
    this.authService.passwordReset(data).subscribe(result => {
      this.isPasswordSubmit = false;
      this.passwordForm.reset();
      this.updateRememberMePassword(data.newPassword);
      this.toaster.successMsg(result, "Success");
      this.router.navigate(['/login']);

    }, (error) => {
      this.passwordForm.reset();
      this.toaster.errorMsg(error.message, "error");
      this.isPasswordSubmit = false;
      this.isOtpFlag = true;
      this.isResest = false;
      this.isPasswordChanged = false;
    });
  }

  updateRememberMePassword(newPassword) {
    if (this.authService.isRemeber()) {
      let data = JSON.parse(this.authService.getDecryptedString(localStorage.getItem('rememberData')));
      data = {
        'username': data.username,
        'password': newPassword
      };
      this.authService.setRememberData(this.authService.getEncryptedString(JSON.stringify(data)));
    }
  }
}
