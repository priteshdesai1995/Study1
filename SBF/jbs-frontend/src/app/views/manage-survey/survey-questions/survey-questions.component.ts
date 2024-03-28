import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { SurveyService } from '../../../_services/survey.service';
import { first } from 'rxjs/operators';
import { NgForm } from '@angular/forms';
import { LoaderService } from '../../../_services/loader.service';

@Component({
  selector: 'app-survey-questions',
  templateUrl: './survey-questions.component.html',
  styleUrls: ['./survey-questions.component.scss'],
})
export class SurveyQuestionsComponent implements OnInit {
  // Question Object model
  questionList: any[] = [];
  // Get Total Answer
  totalAnswer = 0;
  _id: any;
  editMode: boolean;
  surveyTitle: string;
  isUpdateQues = false;
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
    // On page render initialize by default one question & answer
    this.addMoreQuestions();
  }
  // initailzation if Survey question has edit mode
  initForm() {
    // Get Survey Data by Id
    this.surveyService
      .getSurveyQuesAnsById(this._id)
      .pipe(first())
      .subscribe((data) => {
        console.log('Response :: '+data.meta.message);
        this.questionList = data.responseData || [];
        if (this.questionList.length > 0) {
          this.isUpdateQues = true;
          this.questionList.map((que: any, i) => {
//            que.question.questionId = `que-${i + 1}`;
            const answerData: any[] = que.answers || [];
            this.questionList[i].answers = [];
            this.questionList[i].answers = answerData.map((ans: any, j) => {
              return {
                answer: ans.answer,
                answerId: ans.answerId,
                answerType: ans.answerType,
                question: ans.question
              };
            });
          });
        } else {
          this.addMoreQuestions();
        }
      });
  }

  // Add Questions & Answer on API
  onSurveyQuestionDataSave(frm: NgForm) {
    if (frm.invalid) {
      return;
    }
    const formData:{}={};
    formData['survey_id'] = this._id;
    this.questionList.forEach((element, key) => {
      if (this.isUpdateQues && element.id) {
        formData['questions[' + key + '][id]'] = element.id;
      }
      formData['questions[' + key + '][questionId]'] = element.question.questionId;
      formData['questions[' + key + '][question]'] = element.question.question;
      formData['questions[' + key + '][answer_type]'] = element.answerType;
      if (element.answerType !== 'text') {
        console.log(element.answers);
        element.answers.forEach((val, ke) => {
          formData['questions[' + key + '][answers][' + ke + '][answer]'] = val.answer;
          formData['questions[' + key + '][answers][' + ke + '][answerId]'] = val.answerId;
        });
      }
    });
    if (this.isUpdateQues) {
      this.updateSurveyQuesAns(this.questionList);
    } else {
      this.createSurveyQuesAns(this.questionList);
    }
  }
  createSurveyQuesAns(formData) {
    this.loader.showLoader();
    this.surveyService
      .createSurveyQuesAns(this._id, formData)
      .pipe(first())
      .subscribe(
        (response: any) => {
          this.loader.hideLoader();
          if (response.meta.status) {
            this.toastr.success(response.meta.message);
            this.router.navigate(['/manage-survey/list']);
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
            this.router.navigate(['/manage-survey/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
  updateSurveyQuesAns(formData) {
    this.loader.showLoader();
    this.surveyService
      .updateSurveyQuesAns(this._id,formData)
      .pipe(first())
      .subscribe(
        (response: any) => {
          this.loader.hideLoader();
          if (response.meta.status) {
            this.toastr.success(response.meta.message);
            this.router.navigate(['/manage-survey/list']);
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
            this.router.navigate(['/manage-survey/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
  // Add question when user trigger Add more question
  addMoreQuestions() {
    this.addQuestion();
  }

  /**
   * @$Q_no : question number generated by length
   * Add more answer in question
   * max 10 answer allow
   */
  addMoreAnswer($Q_no: string) {
    // use loop for Add answer
    this.questionList.map((que: any, i) => {
      if (que.question_no === $Q_no) {
        // get length from current answer
        const $and_length = que.answers.length;
        // if answer length more than 10, then answer will be not add
        if ($and_length < 10) {
          que.answers.push({
            answer: '',
          });
        } else {
          // An error, if answer more then 10, then display error message
          this.toastr.error('You can add max 10 answer per questions');
        }
      }
    });
  }

  /**
   *
   * @param $Q_no is an Question no
   * it's an unique
   */
  removeQuestions($Q_no: string) {
    this.questionList.map((que: any, i) => {
      if (que.question_no === $Q_no) {
        this.questionList.splice(i, 1);
      }
    });
  }

  /**
   * Remove Answer in Question
   * @param $Q_no is Question no
   * @param $ans_index is index of Question answer
   */

  removeExistAnswer($Q_no, $ans_index) {
    this.questionList.map((que: any, i) => {
      // if question is match to $Q_no
      if (que.question_no === $Q_no) {
        // get All Answer into temp variable
        const $Answer: any[] = que.answers;
        // remove answer
        $Answer.splice($ans_index, 1);
        // assign freash variable value
        que.answers = $Answer;
        // display remove answer message
      }
    });
  }
  // Add more question
  addQuestion(): void {
    let $question_length = this.questionList.length;
    this.questionList.push({
      answerType: '',
      question: { question: '' },
      answers: [{ answer: '' }],
    });
  }
}
