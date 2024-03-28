import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgxPermissionsModule } from 'ngx-permissions';
import { MatDatepickerModule, MatFormFieldModule, MatInputModule } from '@angular/material';
import { LoaderModule } from '../../containers';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { SharedModule } from '../../_module/shared.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';

import { ManageSubscriptionRoutingModule } from './manage-subscription-routing.module';
import { SubscriptionListComponent } from './subscription-list/subscription-list.component';
import { ManageSubscriptionComponent } from './manage-subscription.component';
import { SubscriptionAddEditComponent } from './subscription-add-edit/subscription-add-edit.component';
import { CookieService } from 'ngx-cookie-service';
import { SubscriptionViewComponent } from './subscription-view/subscription-view.component';

@NgModule({
  declarations: [ManageSubscriptionComponent, SubscriptionListComponent, SubscriptionAddEditComponent, SubscriptionViewComponent],
  imports: [
    CommonModule,
    ManageSubscriptionRoutingModule,
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    NgxDatatableModule,
    NgxPermissionsModule,
    NgbModule,
    MatInputModule,
    MatFormFieldModule,
    MatDatepickerModule,
    LoaderModule,
    BsDatepickerModule.forRoot(),
    SharedModule,
  ],
  providers: [CookieService],
})
export class ManageSubscriptionModule {}
