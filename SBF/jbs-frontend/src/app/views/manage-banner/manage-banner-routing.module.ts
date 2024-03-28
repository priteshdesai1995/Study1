import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ManageBannerComponent } from './manage-banner.component';
import { ManageBannerAddEditComponent } from './manage-banner-add-edit/manage-banner-add-edit.component';
import { ManageBannerListComponent } from './manage-banner-list/manage-banner-list.component';

const routes: Routes = [
  {
    path: '',
    component: ManageBannerComponent,
    data: {
      title: 'Manage Banner',
    },
    children: [
      {
        path: 'add',
        component: ManageBannerAddEditComponent,
        data: {
          title: 'Add Banner',
          permission: 'BANNER_CREATE',
          preTitle: 'Banner List',
          preUrl: 'manage-banner/list',
        },
      },
      {
        path: 'edit/:id',
        component: ManageBannerAddEditComponent,
        data: {
          title: 'Edit Banner',
          permission: 'BANNER_UPDATE',
          preTitle: 'Banner List',
          preUrl: 'manage-banner/list',
        },
      },
      {
        path: 'list',
        component: ManageBannerListComponent,
        data: {
          title: 'Banner List',
          permission: 'BANNER_LIST',
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
export class ManageBannerRoutingModule {}
