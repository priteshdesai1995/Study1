import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators, NgForm } from '@angular/forms';
import { AuthenticationService } from './../../_services/authentication.service';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ManageuserService } from '../../_services/manageuser-service';
import { LoaderService } from '../../_services/loader.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './reset-password.component.html',
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  token = '';
  model: any = {};
  isDisplayForm: Boolean = true;
  subscription: Subscription;

  constructor(
    private authenticationService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private manageuserService: ManageuserService,
    private loader: LoaderService
  ) {
    this.token = this.route.snapshot.queryParams['t'] || this.route.snapshot.queryParams['token'];
  }

  ngOnInit() {
    this.isDisplayForm = false;
    this.model.conf_pass_match = '';
    this.validateResetPass();
  }

  validateResetPass() {
    this.authenticationService
      .validateResetPass(this.token)
      .pipe()
      .subscribe(
        (data: any) => {
          if (data.status) {
            this.isDisplayForm = true;
          } else {
            this.isDisplayForm = false;
            this.toastr.error('there are some problem, please try again later.');
          }
        },
      );
  }

  onSubmit(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    this.loader.showLoader();
    this.authenticationService
      .resetPass(this.token, this.model.password, this.model.cpassword)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.router.navigate(['login']);
            this.toastr.success("Password Changed successfully");
            frm.resetForm();
          }
        },
        (error) => {
          this.loader.hideLoader();
          if (error && error.meta) {
            this.toastr.error(error.message);
          } else {
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
  checkPass() {
    this.model.conf_pass_match = this.manageuserService.checkPassword(this.model.password);
  }
}
