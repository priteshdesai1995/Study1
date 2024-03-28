import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ManageContactComponent } from './manage-contact.component';
import { ManageContactListComponent } from './manage-contact-list/manage-contact-list.component';

const routes: Routes = [
  {
    path: '',
    component: ManageContactComponent,
    data: {
      title: 'Enquiry',
    },
    children: [
      {
        path: 'list',
        component: ManageContactListComponent,
        data: {
          title: 'Enquiry List',
          permission: 'CONTACT_LIST',
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
export class ManageContactRoutingModule {}
