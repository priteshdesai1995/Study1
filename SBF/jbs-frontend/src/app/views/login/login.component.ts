import { Component, OnInit, AfterViewInit, ViewChild, HostListener } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { ModalDirective } from 'ngx-bootstrap/modal';
import { AuthenticationService } from './../../_services/authentication.service';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../_services/loader.service';
import { EncrDecrService } from '../../_services/encr-decr.service';
import { CONFIG } from '../../config/app-config';
import { Location } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: 'login.component.html',
})
export class LoginComponent implements OnInit, AfterViewInit {
  @ViewChild('forgotPassModal', { static: true }) public forgotPassModal: ModalDirective;
  loginForm: FormGroup;
  forgotPassWordForm: FormGroup;
  returnUrl: string;
  loading = false;
  forgotPassLoading = false;
  isForgotModalShown: Boolean = false;
  deferredPrompt: any;
  showButton = false;
  @HostListener('window:beforeinstallprompt', ['$event'])
  onbeforeinstallprompt(e) {
    // console.log(e);
    // Prevent Chrome 67 and earlier from automatically showing the prompt
    e.preventDefault();
    // Stash the event so it can be triggered later.
    this.deferredPrompt = e;
    this.showButton = true;
  }
  constructor(
    private authenticationService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private EncrDecr: EncrDecrService,
    private _location: Location
  ) {}

  ngOnInit() {
    // login form
    this.loginForm = new FormGroup({
      email: new FormControl(null, [Validators.required]),
      password: new FormControl(null, [Validators.required]),
      rememberMe: new FormControl(),
    });
    const encrypted = localStorage.getItem('remData');
    const getRemData = this.EncrDecr.get(CONFIG.EncrDecrKey, encrypted);
    if (getRemData && getRemData !== 'null') {
      const parseData = JSON.parse(getRemData);
      this.loginForm.controls['email'].setValue(parseData.email);
      this.loginForm.controls['password'].setValue(parseData.pass);
      this.loginForm.controls['rememberMe'].setValue(parseData.rem);
    }
    // for auto sign up (please remove after start api integration)
    // author : Harsha Prajapati
    // date : 28-07-2020
    // this.loginForm.controls['email'].setValue('rahul.trivedi@brainvire.com');
    // this.loginForm.controls['password'].setValue('admin123');
    // this.onSubmit();
    // end

    // forgot password form
    this.forgotPassWordForm = new FormGroup({
      forgotPassemail: new FormControl(null, [
        Validators.required,
        Validators.pattern(
          /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        ),
      ]),
    });

    // reset login status
    const decrypted = localStorage.getItem('currentUser');
    const currentUser = this.EncrDecr.get(CONFIG.EncrDecrKey, decrypted);
    if (currentUser) {
      this._location.back();
    } else {
      this.authenticationService.logout();

      // get return url from route parameters or default to '/'
      this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }
  }

  onSubmit() {
    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }
    this.loading = true;
    this.loader.showLoader();
    const email = this.loginForm.value.email;
    const password = this.loginForm.value.password;
    const rememberMe = this.loginForm.value.rememberMe;
    this.authenticationService
      .login(email, password)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          // if (data.meta.status) {
            if (rememberMe) {
              const rememberData = {
                email: email,
                pass: password,
                rem: rememberMe,
              };
              const encrypted = this.EncrDecr.set(CONFIG.EncrDecrKey, rememberData);
              localStorage.setItem('remData', encrypted);
            } else {
              localStorage.removeItem('remData');
            }
            this.authenticationService.setLoggedIn(true);
            // this.toastr.success(data.meta.message);
            this.router.navigate([this.returnUrl]);
          // }
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
            this.toastr.error('Something went wrong please try again.');
          }
          this.loading = false;
        }
      );
  }

  ngAfterViewInit() {
    // Remove fixed sidebar and header classes from body
    document.querySelector('body').classList.remove('sidebar-fixed', 'aside-menu-fixed', 'sidebar-lg-show', 'header-fixed');
  }
  showForgotModal(): void {
    this.forgotPassWordForm.reset();
    this.isForgotModalShown = true;
  }
  hideForgotModal(): void {
    this.isForgotModalShown = false;
  }
  onForgotHidden(): void {
    this.isForgotModalShown = false;
  }
  forgotPassword() {
    // stop here if form is invalid
    if (this.forgotPassWordForm.invalid) {
      return;
    }
    this.forgotPassLoading = true;
    const forgotPassemail = this.forgotPassWordForm.value.forgotPassemail;
    this.authenticationService
      .forgotPassword(forgotPassemail)
      .pipe(first())
      .subscribe(
        (data) => {
          if (data.status) {
            this.forgotPassWordForm.reset();
            this.isForgotModalShown = false;
            this.forgotPassLoading = false;
            this.toastr.success('Email Sent!.');
          }
        },
        (error) => {
          if (error && error.errorList) {
            this.toastr.error(error.errorList[0]);
          } else {
            this.toastr.error('Something went wrong please try again.');
          }
          this.forgotPassLoading = false;
        }
      );
  }
  addToHomeScreen() {
    // hide our user interface that shows our A2HS button
    this.showButton = false;
    // Show the prompt
    this.deferredPrompt.prompt();
    // Wait for the user to respond to the prompt
    this.deferredPrompt.userChoice.then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('User accepted the A2HS prompt');
      } else {
        console.log('User dismissed the A2HS prompt');
      }
      this.deferredPrompt = null;
    });
  }
}
