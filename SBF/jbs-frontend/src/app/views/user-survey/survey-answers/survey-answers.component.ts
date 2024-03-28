import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../../_services/loader.service';
import { SurveyService } from '../../../_services/survey.service';
import { first } from 'rxjs/operators';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-survey-answers',
  templateUrl: './survey-answers.component.html',
  styleUrls: ['./survey-answers.component.scss'],
})
export class SurveyAnswersComponent implements OnInit {
  // Question Object model
  questionList: any[] = [];
  _id: any;
  editMode: boolean;
  constructor(
    private route: ActivatedRoute,
    private toastr: ToastrService,
    private router: Router,
    private loader: LoaderService,
    private surveyService: SurveyService
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      if (this.editMode) {
        this.initForm();
      }
    });
  }
  // initailzation if Survey question has edit mode
  initForm() {
    // Get Survey Data by Id
    this.surveyService
      .getSurveyQuesAnsById(this._id)
      .pipe(first())
      .subscribe((Response: any) => {
        const resdata = Response.data;
        this.questionList = resdata || [];
      });
  }
  onSurveyAnswerDataSave(frm: NgForm) {
    if (frm.invalid) {
      return;
    }
    const formData: FormData = new FormData();
    formData.append('survey_uuid', this._id);
    this.questionList.forEach((element, key) => {
      formData.append('questions[' + key + '][question_id]', element.id);
      formData.append('questions[' + key + '][answer_type]', element.answer_type);
      if (element.answer_type === 'text') {
        formData.append('questions[' + key + '][answer]', element.text_answer);
      }
      if (element.answer_type === 'single_check') {
        formData.append('questions[' + key + '][answer]', element.single_answer);
      }
      if (element.answer_type === 'multi_check') {
        element.answer_options.forEach((val, ke) => {
          if (val.multi_answer) {
            formData.append('questions[' + key + '][answer][' + ke + ']', val.id);
          }
        });
      }
    });
    this.loader.showLoader();
    this.surveyService
      .updateUserSurveyAns(formData)
      .pipe(first())
      .subscribe(
        (response: any) => {
          this.loader.hideLoader();
          if (response.meta.status) {
            this.toastr.success(response.meta.message);
            this.router.navigate(['/user-survey/list']);
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
            this.router.navigate(['/user-survey/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
}
