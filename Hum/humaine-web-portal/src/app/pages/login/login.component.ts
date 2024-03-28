import { CONFIGCONSTANTS } from './../../core/_constant/app-constant';
import { getValueByKey } from './../../core/_utility/common';
import { UserRegistrationService } from './../user-registration/user-registration.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Validator from '../../core/_constant/common.validator';
import { AuthenticationService } from '../../services/authentication.service';
import { ToasterService } from '../../core/_utility/notify.service';
import { IdleServiceService } from '../../services/idle-service.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  signInForm: FormGroup;
  isSubmitted: boolean = false;
  isLoginSubmit = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthenticationService,
    private toaster: ToasterService,
    private registerService: UserRegistrationService,
    private idleService : IdleServiceService
  ) { }

  ngOnInit() {

    this.authService.checkIfRegistrationPending();
    this.isLoginSubmit = false;
    this.signInForm = this.formBuilder.group({
      email: [
        '',
        [
          Validators.required,
          Validators.maxLength(80),
          Validator.emailValidator,
        ],
      ],
      password: ['',
        [
          Validators.required,
          Validator.passwordValidator
        ],
      ],
      rememberMe: ['']
    });
    this.getRemeberData();
  }

  getRemeberData() {
    if (this.authService.isRemeber()) {
      let data = JSON.parse(this.authService.getDecryptedString(localStorage.getItem('rememberData')));
      this.signInForm.patchValue({
        'email': data.username,
        'password': data.password
      });
    }
  }

  get errorControl() {
    return this.signInForm.controls;
  }

  addSignIn() {
    if (this.signInForm.valid) {
      if(this.isLoginSubmit){return true};
      this.isSubmitted = true;
      this.isLoginSubmit = true;
      this.authService
        .authenticateCongnito({
          Username: this.signInForm.value.email,
          Password: this.signInForm.value.password,
        })
        .subscribe(
          (result) => {
            if (result && result.idToken) {
              this.authService.setLoginToken(result.idToken.jwtToken);
              this.idleService.stopIdleService();
              if (this.signInForm.value.rememberMe) {
                const loginData = this.authService.getEncryptedString(JSON.stringify({
                  username: this.signInForm.value.email,
                  password: this.signInForm.value.password
                }));
                this.authService.setRememberData(loginData)
              }
              else {
                if (this.authService.isRemeber()) {
                  localStorage.removeItem('rememberData');
                }
              }
              this.registerService.getAccountDetails().subscribe(
                (data) => {
                  this.isLoginSubmit = false;
                  this.authService.setAccountDetails(
                    getValueByKey(data, 'responseData', {})
                  );
                  const confirmed = getValueByKey(
                    data,
                    'responseData.status',
                    false
                  );
                  if (confirmed) {
                    this.idleService.startIdleSErvice();
                    this.router.navigate(['/dashboard']);
                    this.toaster.successMsg('Login Succesfully', 'success');
                  } else {
                    this.toaster.successMsg('Login Succesfully', 'success');
                    this.router.navigate(['/registration']);
                  }
                },
                (error) => {
                  this.authService.removeLoginToken();
                  this.toaster.errorMsg(error.message, 'error');
                  this.isLoginSubmit = false;
                }
              );
            }
          },
          (error) => {
            this.isLoginSubmit = false;
            if (error.code == "UserNotConfirmedException") {
              this.router.navigate(['/registration'], {
                state: {
                  'emailConfirmationPending': true,
                  'email': this.signInForm.value.email,
                  'password': this.signInForm.value.password
                }
              });
            } else {
              this.toaster.errorMsg(error.message, 'error');
            }
          }
        );
    }
  }
}
