import { Component, OnInit, ViewChild, ElementRef, NgModule, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { ManageuserService } from './../../../_services/manageuser-service';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { NgForm } from '@angular/forms';
import { NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { CookieService } from 'ngx-cookie-service';
import * as moment from 'moment';
import { LoaderService } from '../../../_services/loader.service';
import { ImageCroppedEvent } from 'ngx-image-cropper';

@Component({
  selector: 'app-manage-user-add-edit',
  templateUrl: './manage-user-add-edit.component.html',
  styleUrls: ['./manage-user-add-edit.component.scss'],
})
export class ManageUserAddEditComponent implements OnInit, OnDestroy {
  first_name: string;
  last_name: string;
  username: string;
  email: string;
  gender: string;
  phone_number: number;
  date_of_birth: string;
  maxDate: any;
  url: String = 'assets/default-user-image.png';
  profile_image: string;
  noPicture: Boolean = false;

  @ViewChild('f', { static: true }) form: any;

  private _id: number;
  editMode = false;
  private editUserId: number;
  submitted: Boolean = false;
  private routeSub: Subscription;
  private userData: Subscription;
  private userDataSave: Subscription;
  model: any = [];
  imageChangedEvent: any = '';
  isRemoveAPI: Boolean = false;
  fileName: string;
  fileType: string;
  constructor(
    private route: ActivatedRoute,
    private manageuserservice: ManageuserService,
    private router: Router,
    private toastr: ToastrService,
    private _eref: ElementRef,
    private ngbDateParserFormatter: NgbDateParserFormatter,
    private cookieService: CookieService,
    private loader: LoaderService
  ) {
    /* Set Maximum Date */
    this.maxDate = new Date();
    this.maxDate.setDate(this.maxDate.getDate());
  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
  }

  /* Change and Upload Image in User Module */
  changeImage(event: any) {
    const img = event;
    if (event.target.files[0] !== undefined) {
      const imgSize = 2;
      let temp;
      const fileTypes = ['image/jpeg', 'image/jpg', 'image/png'];
      if (fileTypes.includes(event.target.files[0].type) === false) {
        this.toastr.error('Please upload a valid image.');
      } else {
        if ((temp = imgSize) === void 0 || temp === '' || event.target.files[0].size / 1024 / 1024 < imgSize) {
          const reader = new FileReader();
          reader.onload = (e: any) => {
            this.noPicture = true;
            this.isRemoveAPI = false;
            this.fileName = img.target.files[0].name;
            this.fileType = img.target.files[0].type;
            this.imageChangedEvent = img;
          };
          reader.readAsDataURL(event.target.files[0]);
        } else {
          this.toastr.error('File Size should be smaller than ' + imgSize + 'MB');
        }
      }
    }
  }
  imageCropped(event: ImageCroppedEvent) {
    this.url = event.base64;
    this.model.cropped_image = event.file;
  }
  openImageUpload() {
    document.getElementById('profile_picture').click();
  }
  /* Remove Image in User Module */
  removePicture(upld_photo) {
    if (this.isRemoveAPI) {
      this.manageuserservice
        .deleteUserProfile(this.cookieService.get('get_id'))
        .pipe(first())
        .subscribe(
          (data) => {
            if (data.meta.status === true) {
              this.noPicture = false;
              upld_photo.value = null;
              this.url = 'assets/default-user-image.png';
            }
          },
          (error) => {
            const statusError = error;
            if (statusError && statusError.meta) {
              this.toastr.error(statusError.meta.message);
            } else {
              this.toastr.error('Something went wrong please try again.');
            }
          }
        );
    } else {
      this.noPicture = false;
      upld_photo.value = null;
      this.imageChangedEvent = '';
      this.url = 'assets/default-user-image.png';
    }
  }
  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      /* Set the value to Cookies with Edit Mode */
      /* error TS2345:
       Argument of type 'number' is not assignable to parameter of type 'string'.
      */
      this.cookieService.set('get_id', '' + this._id + '');
      /* Set the value to Cookies with Edit Mode */
      this.userData = this.manageuserservice
        .getManagerUserById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            this.editUserId = this._id || null;
            this.model.first_name = response.responseData.firstName || '';
            this.model.last_name = response.responseData.lastName || '';
            this.model.username = response.responseData.username || '';
            this.model.email = response.responseData.email || '';
            this.model.phone_number = response.responseData.cellPhone || '';
            this.model.gender = response.responseData.gender || '';
            if (!response.responseData.dateOfBirth || response.responseData.dateOfBirth === '0000-00-00') {
              this.model.date_of_birth = '';
            } else {
              this.model.date_of_birth = moment(response.responseData.dateOfBirth).format('MM/DD/YYYY');
            }
            /* error - InvalidStateError: Failed to set the 'value' property on 'HTMLInputElement' */
            if (response.responseData.profile_image) {
              this.url = response.responseData.profile_photo;
              this.noPicture = true;
            } else {
              this.isRemoveAPI = true;
              this.url = 'assets/default-user-image.png';
            }
            /* error */
          },
          (error) => {
            console.log(error);
          }
        );
    } else {
      /* Set the value to Cookies without Edit Mode */
      this.cookieService.set('get_id', undefined);
      /* Set the value to Cookies without Edit Mode */
    }
  }
  /**
   * Create/Update user data
   * @param frm for validate form
   */
  public onUserDataSave(frm: NgForm) {
    console.log(this.userData);
    
    this.submitted = true;
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    delete frm.value['confirm_pass'];
    /* FormData for pushing the code with Image URL */
    let signUpRequest:{}={};
    signUpRequest['firstName'] = this.model.first_name;
    signUpRequest['lastName'] = this.model.last_name;
    signUpRequest['userName'] = this.model.username;
    signUpRequest['dateOfBirth'] = this.model.date_of_birth
     ? moment(new Date(this.model.date_of_birth)).format('YYYY-MM-DD') : '';
    signUpRequest['gender'] = this.model.gender;
    signUpRequest['cellPhone'] = this.model.phone_number;
    signUpRequest['email']= this.model.email;
    
    // const file = (<HTMLInputElement>document.getElementById('profile_picture')).files[0];

    // if (file) {
    //   const convertfile = new File([this.model.cropped_image], this.fileName, { type: this.fileType });
    //   formData.append('profile_image', convertfile);
    // }
    if (this.editUserId) {
      this.updateManageUser(signUpRequest, this.editUserId);
    } else {
      signUpRequest['email'] = this.model.email;
      signUpRequest['password'] = this.model.password;
      this.createManageUser(signUpRequest);
    }
  }

  private createManageUser(formData) {
    this.loader.showLoader();
    this.userDataSave = this.manageuserservice
      .createManageUser(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.toastr.success("User added successfully.");
            this.router.navigate(['/manage-user/list']);
          }
        },
        (error) => {
          this.loader.hideLoader();
          // const errorData = error;
          // const self = this;
          // if (errorData && errorData.meta) {
          //   if (errorData.meta.message_code === 'VALIDATION_ERROR') {
          //     for (const key in errorData.errors) {
          //       if (key) {
          //         self.toastr.error(errorData.errors[key][0]);
          //       }
          //     }
          //   } else {
          //     self.toastr.error(errorData.meta.message);
          //   }
          // } else {
            // this.router.navigate(['/manage-user/list']);
            // this.toastr.error('Something went wrong please try again.');
            this.toastr.error(error.errorList);
          // }
          this.submitted = false;
        }
      );
  }

  private updateManageUser(formData, id) {
    this.loader.showLoader();
    this.userDataSave = this.manageuserservice
      .updateManageUser(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.status) {
            this.toastr.success("User updated sucessfully");
            this.router.navigate(['/manage-user/list']);
          }
        },
        (error) => {
          this.loader.hideLoader();
          const errorData = error;
          const self = this;
          if (errorData && errorData) {
            if (errorData) {
              console.log(errorData.errorList);
            } else {
              if (errorData.meta.message_code === 'RECORD_IS_ALREADY_LOCKED') {
                this.router.navigate(['/manage-user/list']);
              }
              self.toastr.error(errorData.meta.message);
            }
          } else {
            this.router.navigate(['/manage-user/list']);
            this.toastr.error('Something went wrong please try again.');
          }
          this.submitted = false;
        }
      );
  }

  private unlockManageUser(id, is_self_locked) {
    this.userDataSave = this.manageuserservice
      .unlockManageUser(id, is_self_locked)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.router.navigate(['/manage-user/list']);
          }
        },
        (error) => {
          const errorData = error;
          const self = this;
          if (errorData && errorData.meta) {
            if (errorData.meta.message_code === 'VALIDATION_ERROR') {
              for (const key in errorData.errors) {
                if (key) {
                  self.toastr.error(errorData.errors[key][0]);
                }
              }
            }
          } else {
            this.router.navigate(['/manage-user/list']);
            this.toastr.error('Something went wrong please try again.');
          }
          this.submitted = false;
        }
      );
  }

  checkPass() {
    this.model.conf_pass_match = this.manageuserservice.checkPassword(this.model.password);
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      // this.unlockManageUser(this._id, 1);
      this.userData.unsubscribe();
    }
  }
}
