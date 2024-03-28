import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { CmsService } from './../../../_services/cms.service';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Cms } from './../../../model/cms';
import { LoaderService } from '../../../_services/loader.service';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { MultilingualService } from '../../../_services/multilingual.service';
@Component({
  selector: 'app-cms-add-edit',
  templateUrl: './cms-add-edit.component.html',
  styleUrls: ['./cms-add-edit.component.scss'],
})
export class CmsAddEditComponent implements OnInit, OnDestroy {
  @ViewChild('cmsFrm', { static: true }) form: any;

  private _id: number;
  editMode: Boolean = false;
  private editCmsId: number;
  model: any = new Cms({}, {}, {}, {},{});
  cmsStatus = 'Active';
  ck_error = {};
  private routeSub: Subscription;
  private cmsSub: Subscription;
  private cmsSaveSub: Subscription;
  config = CONFIGCONSTANTS['CK-Editor-config'];
  addEditCmsForm: FormGroup;
  languages = [];
  buttonDisable = true;
  constructor(
    private route: ActivatedRoute,
    private cmsService: CmsService,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private multilingualService: MultilingualService
  ) {
    this.languages = this.multilingualService.getLanguage();
  }

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        // <<<---    using ()=> syntax
        this.initForm();
      }, 100);
    });
  }

  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      this.cmsSub = this.cmsService
        .getCmsById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            const resData = response.responseData;
            this.editCmsId = resData.cmsId || null;
            this.cmsStatus = resData.status || 'Active';
            resData.translations.forEach((element) => {
              this.model.translationId[element.locale] = element.translationId;
              this.model.page_title[element.locale] = element.pageTitle;
              this.model.description[element.locale] = element.description;
              this.model.meta_keywords[element.locale] = element.metaKeywords;
              this.model.meta_description[element.locale] = element.metaDescription;
            });
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
  /**
   * Create/Update CMS data
   */
  public onCmsSave() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }

    this.languages.forEach((element) => {
      if (this.ck_error[element.locale] === true) {
        return;
      }
    });
    this.getHtmlWithOutBodyTag();
    let obj = {
      "pageTitle": this.model.page_title.ar,
      "description": this.model.description.ar,
      "metaKeywords": this.model.meta_keywords.ar,
      "metaDescription": this.model.meta_description.ar,
      "locale":"ar"
    };
    let translations: any[] = [];
    let obj2 = {
      "pageTitle": this.model.page_title.en,
      "description": this.model.description.en,
      "metaKeywords": this.model.meta_keywords.en,
      "metaDescription": this.model.meta_description.en,
      "locale":"en"
    };
    if(this.editCmsId){
      obj["id"] =this.model.translationId.en;
      obj2["id"]= this.model.translationId.ar;
    }
    translations = [...translations, obj, obj2];
    let contentData = {
      translations: translations,
      status:this.cmsStatus
    }

    if (this.editCmsId) {
      this.updateCms(contentData, this.editCmsId);
    } else {
      this.createCms(contentData);
    }
  }

  private createCms(formData) {
    this.loader.showLoader();
    this.cmsSaveSub = this.cmsService
      .createCms(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/cms/list']);
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
            this.router.navigate(['/cms/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  private updateCms(formData, id) {
    this.loader.showLoader();
    this.cmsSaveSub = this.cmsService
      .updateCms(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/cms/list']);
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
            this.router.navigate(['/cms/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.cmsSub.unsubscribe();
    }
  }
  getCKData(type, lan_direction) {
    if (lan_direction === 'LTR') {
      const a = this.model.description[type].substring(
        this.model.description[type].indexOf('<body>') + 6,
        this.model.description[type].indexOf('</body>')
      );
      this.ck_error[type] = a.length > 0 ? false : true;
    }
    if (lan_direction === 'RTL') {
      const a = this.model.description[type].substring(
        this.model.description[type].indexOf('<body dir="rtl">') + 16,
        this.model.description[type].indexOf('</body>')
      );
      this.ck_error[type] = a.length > 0 ? false : true;
    }
  }
  getHtmlWithOutBodyTag() {
    for (const key in this.model.description) {
      if (this.model.description[key].indexOf('<body') !== -1 && this.model.description[key].indexOf('</body>') !== -1) {
        const text = this.model.description[key].substr(this.model.description[key].indexOf('<body') + 5);
        const text2 = text.substr(text.indexOf('>') + 1);
        const text3 = text2.substr(0, text2.indexOf('</body>'));
        this.model.description[key] = text3;
      }
    }
  }
}
