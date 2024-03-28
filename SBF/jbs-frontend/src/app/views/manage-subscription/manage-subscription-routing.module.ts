import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SubscriptionAddEditComponent } from './subscription-add-edit/subscription-add-edit.component';
import { SubscriptionListComponent } from './subscription-list/subscription-list.component';
import { ManageSubscriptionComponent } from './manage-subscription.component';
import { SubscriptionViewComponent } from './subscription-view/subscription-view.component';

const routes: Routes = [
  {
    path: '',
    component: ManageSubscriptionComponent,
    data: {
      title: 'Manage Subscription',
    },
    children: [
      {
        path: 'add',
        component: SubscriptionAddEditComponent,
        data: {
          title: 'Add Subscription',
          permission: 'SUBSCRIPTION_CREATE',
          preTitle: 'Subscription List',
          preUrl: 'manage-subscription/list',
        },
      },
      {
        path: 'edit/:id',
        component: SubscriptionAddEditComponent,
        data: {
          title: 'Edit Subscription',
          permission: 'SUBSCRIPTION_UPDATE',
          preTitle: 'Subscription List',
          preUrl: 'manage-subscription/list',
        },
      },
      {
        path: 'view/:id',
        component: SubscriptionViewComponent,
        data: {
          title: 'View Subscription',
          permission: 'SUBSCRIPTION_VIEW',
          preTitle: 'Subscription List',
          preUrl: 'manage-subscription/list',
        },
      },
      {
        path: 'list',
        component: SubscriptionListComponent,
        data: {
          title: 'Subscription List',
          permission: 'SUBSCRIPTION_LIST',
        },
      },
      { path: '', redirectTo: 'list', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ManageSubscriptionRoutingModule {}
