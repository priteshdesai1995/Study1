import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoaderService } from '../../../_services/loader.service';
import { RulesetService } from '../../../_services/ruleset.service';
import { first } from 'rxjs/operators';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-ruleset-add-edit',
  templateUrl: './ruleset-add-edit.component.html',
  styleUrls: ['./ruleset-add-edit.component.scss'],
})
export class RulesetAddEditComponent implements OnInit, OnDestroy {
  private _id: number;
  editMode = false;
  private editRuleId: number;
  private routeSub: Subscription;
  private ruleSub: Subscription;
  private ruleSaveSub: Subscription;
  model: any = {};
  filterList = [];
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toastr: ToastrService,
    private loader: LoaderService,
    private rulesetService: RulesetService
  ) {}

  ngOnInit() {
    this.model.priority = '1';
    this.model.on_action = '';
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 100);
    });
    this.addMoreFilter();
  }
  // Add more filter
  addMoreFilter(): void {
    let $filter_length = this.filterList.length;
    this.filterList.push({
      filter_no: `filter_${++$filter_length}`,
      item_name: 'email',
      verb_descritpion: 'CONTAINS',
      specified_input: '',
      time_frame: '',
      action: 'NOTHING',
      notification: 'NOTIFY',
    });
  }
  /**
   *
   * @param $f_no is an Filter no
   * it's an unique
   */
  removeFilter($f_no: string) {
    this.filterList.map((filt: any, i) => {
      if (filt.filter_no === $f_no) {
        this.filterList.splice(i, 1);
      }
    });
  }
  /**
   * Selected input value in edit mode
   */
  private initForm() {
    if (this.editMode) {
      this.ruleSub = this.rulesetService
        .getRulesetById({ id: this._id })
        .pipe(first())
        .subscribe(
          (response) => {
            const resData = response.responseData;
            this.editRuleId = resData.ruleId || null;
            this.model.rulename = resData.name || '';
            this.model.description = resData.description || '';
            this.model.priority = resData.priority;
            this.model.on_action = resData.on_action;
            this.filterList = resData.filters ? resData.filters : [];
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
  public onRulesetSave(frm: NgForm) {
    // stop here if form is invalid
    if (frm.invalid) {
      return;
    }
    const reqData = {
      name: this.model.rulename,
      description: this.model.description,
      priority: this.model.priority,
      on_action: this.model.on_action,
      filters: this.filterList,
    };
    if (this.editRuleId) {
      reqData['ruleId'] = this.editRuleId;
      this.updateRuleset(reqData);
    } else {
      this.createRuleset(reqData);
    }
  }

  private createRuleset(formData) {
    this.loader.showLoader();
    this.ruleSaveSub = this.rulesetService
      .createRuleset(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/rulesets/list']);
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
            this.router.navigate(['/rulesets/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }

  private updateRuleset(formData) {
    this.loader.showLoader();
    this.ruleSaveSub = this.rulesetService
      .updateRuleset(formData)
      .pipe(first())
      .subscribe(
        (data) => {
          this.loader.hideLoader();
          if (data.meta.status) {
            this.toastr.success(data.meta.message);
            this.router.navigate(['/rulesets/list']);
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
            this.router.navigate(['/rulesets/list']);
            this.toastr.error('Something went wrong please try again.');
          }
        }
      );
  }
  ngOnDestroy() {
    this.routeSub.unsubscribe();
    if (this.editMode) {
      this.ruleSub.unsubscribe();
    }
  }
}
