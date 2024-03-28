import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { SubadminService } from './../../../_services/subadmin-service';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Subadmin } from './../../../model/subadmin';
import { NgForm } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { ManageuserService } from '../../../_services/manageuser-service';
import { LoaderService } from '../../../_services/loader.service';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import * as moment from 'moment';

@Component({
  selector: 'app-subadmin-add-edit',
  templateUrl: './subadmin-add-edit.component.html',
  styleUrls: ['./subadmin-add-edit.component.scss'],
})
export class SubadminAddEditComponent implements OnInit, OnDestroy {
  @ViewChild('f', { static: true }) form: any;

  private _id: number;
  editMode = false;
  private editSubadminId: number;
  submitted: Boolean = false;
  subadmin_subject: string;
  subadmin_body: string;
  roleList: any[] = [];
  private routeSub: Subscription;
  private subadminSub: Subscription;
  private activeRoleList: Subscription;
  private subadminTopicList: Subscription;
  private subadminSaveSub: Subscription;
  model: any = [];
  role = '';
  roleValidation: String = 'Please select role';
  roleStatus: String = '';
  departmentList = [];
  locationList = [];
  constructor(
    private route: ActivatedRoute,
    private subadminService: SubadminService,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private manageuserService: ManageuserService
  ) { }

  ngOnInit() {
    this.model.department_id = '';
    this.model.location_id = '';
    // this.activeRoleList = this.subadminService
    //   .getActiveRoleList()
    //   .pipe(first())
    //   .subscribe(
    //     (response) => {
    //       this.roleList = response.data;
    //     },
    //     (error) => {
    //       console.log(error);
    //     }
    //   );
    let obj = {
      name: "ROLE_SUBADMIN",
      id: "ROLE_SUBADMIN"
    };
    this.roleList.push(obj);
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }
  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      this.subadminSub = this.manageuserService
        .getManagerUserById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            this.editSubadminId = this._id || null;
            this.model.first_name = response.responseData.firstName || '';
            this.model.last_name = response.responseData.lastName || '';
            this.model.username = response.responseData.username || '';
            this.model.email = response.responseData.email || '';
            this.model.date_of_birth =response.responseData.dateOfBirth;
            this.model.gender=response.responseData.gender;
            this.model.phone_number=response.responseData.cellPhone;
            this.role = response.responseData.roles.roleName || '';
            // this.roleStatus = response.responseData.roles.status || '';
            if (this.roleStatus && this.roleStatus === 'Inactive') {
              this.role = '';
              this.roleValidation = 'Your previous role is inactive, so select new role';
            }
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
  /**
   * Create/Update Sub admin data
   * @param frm for validate form
   */
  public onSubadminSave(frm: NgForm) {
    this.submitted = true;
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    delete frm.value['confirm_pass'];
    let signUpRequest: {} = {};
    signUpRequest['firstName'] = this.model.first_name;
    signUpRequest['lastName'] = this.model.last_name;
    signUpRequest['userName'] = this.model.username;
    signUpRequest['dateOfBirth'] = this.model.date_of_birth
      ? moment(new Date(this.model.date_of_birth)).format('YYYY-MM-DD') : '';
    signUpRequest['gender'] = this.model.gender;
    signUpRequest['cellPhone'] = this.model.phone_number;
    signUpRequest['email'] = this.model.email;
    signUpRequest['roleName']=this.role;
    if (this.editSubadminId) {
      // delete frm.value['username'];
      this.updateSubadmin(signUpRequest, this.editSubadminId);
    } else {
      signUpRequest['password'] = this.model.password;
      this.createSubadmin(signUpRequest);
    }
  }

  private createSubadmin(formData) {
    this.loader.showLoader();
    this.subadminSaveSub = this.subadminService
      .createSubadmin(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.toastr.success("Sub Admin created successfully");
            this.router.navigate(['/subadmin/list']);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          if (errorData && errorData.errorList) {
            this.toastr.error(errorData.errorList);
          } else {
            this.router.navigate(['/subadmin/list']);
            this.toastr.error('Something went wrong please try again.');
          }
          this.submitted = false;
        }
      );
  }

  private updateSubadmin(formData, id) {
    this.loader.showLoader();
    this.subadminSaveSub = this.manageuserService
      .updateManageUser(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.toastr.success("Sub Admin updated successfully");
            this.router.navigate(['/subadmin/list']);
          }
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
            this.router.navigate(['/subadmin/list']);
            this.toastr.error('Something went wrong please try again.');
          }
          this.submitted = false;
        }
      );
  }
  checkPass() {
    this.model.conf_pass_match = this.manageuserService.checkPassword(this.model.password);
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    // this.activeRoleList.unsubscribe();
    if (this.editMode) {
      this.subadminSub.unsubscribe();
    }
  }
}
