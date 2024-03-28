import { HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserRegistrationService } from 'src/app/services/user-registration.service';
import { errorHandler, getValueByKey } from 'src/app/utility/common';
import { ToasterService } from 'src/app/utility/notify.service';
import Validator from 'src/app/_constant/common.validator';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  signInForm: FormGroup;
  isSubmitted: boolean = false;
  isLoginSubmit = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private router: Router,
    private toaster: ToasterService
  ) { }

  ngOnInit() {

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
    this.getRememberData();

  }


  get errorControl() {
    return this.signInForm.controls;
  }

  addSignIn() {
    if (this.signInForm.valid) {
    if(this.isLoginSubmit){return };
      this.isLoginSubmit = true;
      this.authService.login(this.signInForm.controls.email.value, this.signInForm.controls.password.value).subscribe(data => {
        this.authService.setLoginToken(data);
        if (this.signInForm.value.rememberMe) {
          let loginData = {
            email: this.signInForm.value.email,
            password: this.signInForm.value.password
          };
          this.authService.setRememberData(JSON.stringify(loginData));
        }
          else {
            if (this.authService.isRemember()) {
              localStorage.removeItem('rememberData');
            }
          }
        if (data) {
          this.router.navigate(['/home']);
          this.toaster.successMsg('Login Succesfully', 'success');
          this.isLoginSubmit = false;
        } 
      }, (error) => {
        this.authService.removeLoginToken();
        console.log(error);
        this.toaster.errorMsg(error.error.error, 'error');
        this.isLoginSubmit = false;
      });
    }
  }

  getRememberData() {
    if (this.authService.isRemember()) {
      let data = JSON.parse(localStorage.getItem('rememberData'));
      this.signInForm.patchValue({
        'email': data.email,
        'password': data.password
      });
    }}
}


