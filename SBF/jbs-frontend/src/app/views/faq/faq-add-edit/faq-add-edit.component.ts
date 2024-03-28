import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';
import { FaqService } from './../../../_services/faq-service';
import { Subscription } from 'rxjs';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { Faq } from './../../../model/faq';
import { LoaderService } from '../../../_services/loader.service';
import { ManageuserService } from '../../../_services/manageuser-service';

@Component({
  selector: 'app-faq-add-edit',
  templateUrl: './faq-add-edit.component.html',
  styleUrls: ['./faq-add-edit.component.scss'],
})
export class FaqAddEditComponent implements OnInit, OnDestroy {
  @ViewChild('faqFrm', { static: true }) form: any;

  private _id: number;
  editMode = false;
  private editFaqId: number;
  faq_subject: string;
  faq_body: string;
  topicListData: any;
  private routeSub: Subscription;
  private faqSub: Subscription;
  private faqTopicList: Subscription;
  private faqSaveSub: Subscription;
  model: any = new Faq('', '', '', '', 'Active');
  addEditCmsForm: FormGroup;

  constructor(
    private route: ActivatedRoute,
    private faqService: FaqService,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private manageuserService: ManageuserService
  ) {}

  ngOnInit() {
    this.faqTopicList = this.faqService
      .getFaqTopicList()
      .pipe(first())
      .subscribe(
        (response) => {
          this.topicListData = response.responseData;
        },
        (error) => {
          console.log(error);
        }
      );

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
      this.faqSub = this.faqService
        .getFaqById(this._id)
        .pipe(first())
        .subscribe(
          (response) => {
            this.editFaqId = response.responseData.id || null;
            this.model.faq_topic = response.responseData.faq_topic_id || '';
            this.model.question = response.responseData.question || '';
            this.model.answer = response.responseData.answer || '';
            this.model.status = response.responseData.status || 'Active';
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
  /**
   * Create/Update FAQ data
   */
  public onFaqSave() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    const data = {
      faq_topic_id: this.model.faq_topic,
      question: this.manageuserService.trimText(this.model.question),
      answer: this.manageuserService.trimText(this.model.answer),
      status: this.model.status,
    };
    if (this.editFaqId) {
      this.updateFaq(data, this.editFaqId);
    } else {
      this.createFaq(data);
    }
  }

  private createFaq(formData) {
    this.loader.showLoader();
    this.faqSaveSub = this.faqService
      .createFaq(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/faq/list']);
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
            this.router.navigate(['/faq/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  private updateFaq(formData, id) {
    this.loader.showLoader();
    this.faqSaveSub = this.faqService
      .updateFaq(formData, id)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/faq/list']);
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
            this.router.navigate(['/faq/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
    this.faqTopicList.unsubscribe();
    if (this.editMode) {
      this.faqSub.unsubscribe();
    }
  }
}
