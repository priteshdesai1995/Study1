import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ManageOfferComponent } from './manage-offer.component';
import { ManageOfferAddEditComponent } from './manage-offer-add-edit/manage-offer-add-edit.component';
import { ManageOfferListComponent } from './manage-offer-list/manage-offer-list.component';

const routes: Routes = [
  {
    path: '',
    component: ManageOfferComponent,
    data: {
      title: 'Manage Offer',
    },
    children: [
      {
        path: 'add',
        component: ManageOfferAddEditComponent,
        data: {
          title: 'Add Offer',
          permission: 'OFFER_CREATE',
          preTitle: 'Offer List',
          preUrl: 'manage-offer/list',
        },
      },
      {
        path: 'edit/:id',
        component: ManageOfferAddEditComponent,
        data: {
          title: 'Edit Offer',
          permission: 'OFFER_UPDATE',
          preTitle: 'Offer List',
          preUrl: 'manage-offer/list',
        },
      },
      {
        path: 'list',
        component: ManageOfferListComponent,
        data: {
          title: 'Offer List',
          permission: 'OFFER_LIST',
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
export class ManageOfferRoutingModule {}
