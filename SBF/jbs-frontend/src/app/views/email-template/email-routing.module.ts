import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { EmailComponent } from './email.component';
import { EmailAddEditComponent } from './email-add-edit/email-add-edit.component';
import { EmailListComponent } from './email-list/email-list.component';
import { AuthGuard } from './../../_guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: EmailComponent,
    data: {
      title: 'Email Template',
    },
    children: [
      {
        path: 'add',
        component: EmailAddEditComponent,
        data: {
          title: 'Add Email Template',
          permission: 'EMAIL_TEMP_ADD',
          preTitle: 'Email Template List',
          preUrl: 'email/list',
        },
      },
      {
        path: 'edit/:id',
        component: EmailAddEditComponent,
        data: {
          title: 'Edit Email Template',
          permission: 'EMAIL_TEMP_UPDATE',
          preTitle: 'Email Template List',
          preUrl: 'email/list',
        },
      },
      {
        path: 'list',
        component: EmailListComponent,
        data: {
          title: 'Email Template List',
          permission: 'EMAIL_TEMP_LIST',
        },
      },
      { path: '', redirectTo: 'list', pathMatch: 'full' },
      // permission: 'USER_CREATE',
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class EmailRoutingModule {}
