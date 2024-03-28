import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ManageUserComponent } from './manage-user.component';
import { ManageUserAddEditComponent } from './manage-user-add-edit/manage-user-add-edit.component';
import { ManageUserListComponent } from './manage-user-list/manage-user-list.component';
import { ManageUserViewComponent } from './manage-user-view/manage-user-view.component';

const routes: Routes = [
  {
    path: '',
    component: ManageUserComponent,
    data: {
      title: 'Manage Users',
    },
    children: [
      {
        path: 'add',
        component: ManageUserAddEditComponent,
        data: {
          title: 'Add User',
          permission: 'USER_CREATE',
          preTitle: 'User List',
          preUrl: 'manage-user/list',
        },
      },
      {
        path: 'edit/:id',
        component: ManageUserAddEditComponent,
        data: {
          title: 'Edit User',
          permission: 'USER_UPDATE',
          preTitle: 'User List',
          preUrl: 'manage-user/list',
        },
      },
      {
        path: 'view/:id',
        component: ManageUserViewComponent,
        data: {
          title: 'View User',
          permission: 'USER_View',
          preTitle: 'User List',
          preUrl: 'manage-user/list',
        },
      },
      {
        path: 'list',
        component: ManageUserListComponent,
        data: {
          title: 'User List',
          permission: 'USER_LIST',
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
export class ManageUserRoutingModule {}
