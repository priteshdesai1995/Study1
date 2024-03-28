import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../../_services/loader.service';
import { SuggestionService } from '../../../_services/suggestion.service';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
@Component({
  selector: 'app-user-suggestion-add',
  templateUrl: './user-suggestion-add.component.html',
  styleUrls: ['./user-suggestion-add.component.scss'],
})
export class UserSuggestionAddComponent implements OnInit {
  @ViewChild('suggestionFrm', { static: true }) form: any;

  model = {
    category: '',
    information: '',
    anonymously: false,
  };
  categoryList = [];
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private suggestionService: SuggestionService
  ) {}

  ngOnInit() {
    this.getActiveCategoryList();
  }
  private getActiveCategoryList(): void {
    this.suggestionService
      .getActiveCategoryList(CONFIGCONSTANTS.suggestionId)
      .pipe(first())
      .subscribe((data) => {
        if (!data.data) {
          this.categoryList = [];
        } else {
          this.categoryList = data.data;
        }
      });
  }
  /**
   * Create/Update Suggestion data
   */
  public onSuggestionSave() {
    // stop here if form is invalid
    if (this.form.invalid) {
      return;
    }
    const data = {
      category: this.model.category,
      information: this.model.information,
      post_anonymously: this.model.anonymously === true ? 0 : 1,
    };
    this.loader.showLoader();
    this.suggestionService
      .createUserSuggestion(data)
      .pipe(first())
      .subscribe(
        (resData) => {
          this.loader.hideLoader();
          if (resData.meta.status) {
            this.toastr.success(resData.meta.message);
            this.router.navigate(['/user-suggestion/list']);
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
            this.router.navigate(['/user-suggestion/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
}
