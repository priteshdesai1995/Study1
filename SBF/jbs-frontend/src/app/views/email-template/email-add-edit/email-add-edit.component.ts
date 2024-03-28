import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { EmailService } from './../../../_services/email-service';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Email } from './../../../model/email';
import { LoaderService } from '../../../_services/loader.service';
import { ManageuserService } from '../../../_services/manageuser-service';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { EncrDecrService } from '../../../_services/encr-decr.service';
import { CONFIG } from '../../../config/app-config';
import { MultilingualService } from '../../../_services/multilingual.service';

@Component({
  selector: 'app-email-add-edit',
  templateUrl: './email-add-edit.component.html',
  styleUrls: ['./email-add-edit.component.scss'],
})
export class EmailAddEditComponent implements OnInit, OnDestroy {
  @ViewChild('emailFrm', { static: true }) form: any;

  private _id: number;
  editMode = false;
  private editCmsId: number;
  model: any = new Email('', '');
  emailSubject: string;
  emailBody: string;
  template_for: string;
  email_key: string;
  templateType: string;
  private routeSub: Subscription;
  private emailSub: Subscription;
  private emailSaveSub: Subscription;
  config: any;
  addEditCmsForm: FormGroup;
  languages = [];
  ck_error = false;

  constructor(
    private route: ActivatedRoute,
    private emailService: EmailService,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private EncrDecr: EncrDecrService,
    private manageuserService: ManageuserService,
    private multilingualService: MultilingualService
  ) {
    this.languages = this.multilingualService.getLanguage();
  }

  ngOnInit() {
    this.languages.forEach((e) => {
      this.model[e.locale] = new Email('', '');
    });
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 2000);
    });
  }
  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      this.languages.forEach((e) => {
        if (!this.model[e.locale] === undefined) {
          this.model[e.locale] = new Email('', '');
        }
      });
      this.emailSub = this.emailService
        .getEmailById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            this.editCmsId = response.responseData.templateId || null;
            this.template_for = response.responseData.templatePurpose || '';
            this.templateType = response.responseData.templateType || '';
            this.email_key = response.responseData.template[0].emailKey;
            if (this.templateType !== 'SMS') {
              this.config = CONFIGCONSTANTS['CK-Editor-config'];
            }
            
            response.responseData.template.forEach((element) => {
              this.model[element.locale]['emailSubject'] = element.emailSubject;
              this.model[element.locale]['emailBody'] = element.emailBody;
            });
            
          },
        );
    }
  }
  /**
   * Create/Update Email template data
   */
  public onEmailSave() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    if (this.templateType !== 'SMS') {
      let c_error = false;
      this.languages.forEach((element) => {
        if (this.model[element.locale].emailBody.replace(/<[^>]*>/gi, '').length === 7) {
          c_error = true;
        }
      });
      if (c_error) {
        this.toastr.error('Please enter content');
        return;
      }
    }

    // let tempObj: any={};
    // let data: any[] = [];
    let emailTemp: any[] = [];
    this.languages.forEach((e) => {
      const obj = {};
      obj['emailSubject'] = this.manageuserService.trimText(this.model[e.locale]['emailSubject']);
      obj['emailBody'] = this.manageuserService.trimText(this.model[e.locale]['emailBody']);
      obj['locale'] = e.locale;
      obj['emailKey'] = this.manageuserService.trimText(this.email_key);
      emailTemp.push(obj);
    });
    const data = {
      template: emailTemp,
      templatePurpose: this.manageuserService.trimText(this.template_for),
      templateType: this.templateType,
    };
    if (this.editCmsId) {
      this.updateEmail(data, this.editCmsId);
    } else {
      this.addEmail(data);
    }
  }
  private addEmail(formData) {
    this.loader.showLoader();
    this.emailSaveSub = this.emailService
      .addEmail(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/email/list']);
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
            this.router.navigate(['/email/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }


  private updateEmail(formData, id) {
    this.loader.showLoader();
    this.emailSaveSub = this.emailService
      .updateEmail(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/email/list']);
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
            this.router.navigate(['/email/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.emailSub.unsubscribe();
    }
  }

  getCKData(type, lan_direction) {
    if (lan_direction === 'LTR') {
      const a = this.model[type].email_body.substring(
        this.model[type].email_body.indexOf('<body>') + 6,
        this.model[type].email_body.indexOf('</body>')
        );
        this.ck_error[type] = a.length > 0 ? false : true;
    }
    if (lan_direction === 'RTL') {
      const a = this.model[type].email_body.substring(
        this.model[type].email_body.indexOf('<body dir="rtl">') + 16,
        this.model[type].email_body.indexOf('</body>')
      );
      this.ck_error[type] = a.length > 0 ? false : true;
    }
  }
}
