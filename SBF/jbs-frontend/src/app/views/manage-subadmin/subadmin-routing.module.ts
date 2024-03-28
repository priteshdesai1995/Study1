import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { SubadminComponent } from './subadmin.component';
import { SubadminAddEditComponent } from './subadmin-add-edit/subadmin-add-edit.component';
import { SubadminListComponent } from './subadmin-list/subadmin-list.component';
import { AuthGuard } from './../../_guards/auth.guard';

const routes: Routes = [
  {
    path: '',
    component: SubadminComponent,
    data: {
      title: 'Manage Sub Admin',
    },
    children: [
      {
        path: 'add',
        component: SubadminAddEditComponent,
        data: {
          title: 'Add Sub Admin',
          permission: 'SUB_ADMIN_CREATE',
          preTitle: 'Sub Admin List',
          preUrl: 'subadmin/list',
        },
      },
      {
        path: 'edit/:id',
        component: SubadminAddEditComponent,
        data: {
          title: 'Edit Sub Admin',
          permission: 'SUB_ADMIN_UPDATE',
          preTitle: 'Sub Admin List',
          preUrl: 'subadmin/list',
        },
      },
      {
        path: 'list',
        component: SubadminListComponent,
        data: {
          title: 'Sub Admin List',
          permission: 'SUB_ADMIN_LIST',
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
export class SubadminRoutingModule {}
