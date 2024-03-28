import { Component, OnInit, ElementRef, TemplateRef, QueryList, ViewChildren } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subscription, Subject } from 'rxjs';
import { first } from 'rxjs/operators';
import { CONFIGCONSTANTS } from '../../../config/app-constants';
import { ManageSubscriptionService } from '../../../_services/manage-subscription.service';
import * as moment from 'moment';

@Component({
  selector: 'app-subscription-view',
  templateUrl: './subscription-view.component.html',
  styleUrls: ['./subscription-view.component.scss'],
})
export class SubscriptionViewComponent implements OnInit {
  model: any = {};
  private _id: number;
  editMode = false;
  private editSubscriptionId: number;
  submitted = false;
  private routeSub: Subscription;
  momentDateTime24Format: string;
  momentDateFormat = CONFIGCONSTANTS.momentDateFormat || 'MM/DD/YYYY';

  plan_validity = {
    month: 'Monthly',
    quarter: 'Quarterly',
    year: 'Yearly',
  };

  plan_type = {
    'Trial': 'Yes',
    'Paid': 'No',
  };

  constructor(private route: ActivatedRoute, private manageSubscriptionService: ManageSubscriptionService) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this._id = params['id'];
      this.editMode = params['id'] !== undefined;
      setTimeout(() => {
        this.initForm();
      }, 1000);
    });
    this.momentDateTime24Format = CONFIGCONSTANTS.momentDateTime24Format || 'MM/DD/YYYY hh:mm:ss';
  }

  /**
   * Selected input value in edit mode
   */
  initForm() {
    if (this.editMode) {
      this.manageSubscriptionService
        .getManagerSubscriptionById(this._id)
        .pipe(first())
        .subscribe((response) => {
          this.editSubscriptionId = response.responseData.uuid || null;
          this.model.plan_name = response.responseData.name || '';
          this.model.description = response.responseData.description || '';
          this.model.validity = response.responseData.validity || '';
          this.model.price = response.responseData.price || '';
          this.model.start_date = response.responseData.startDate || '';
          this.model.end_date = response.responseData.endDate || '';
          this.model.discount = response.responseData.discount || '';
          this.model.is_trial_plan = response.responseData.planType || '';
        });
    }
  }
}
