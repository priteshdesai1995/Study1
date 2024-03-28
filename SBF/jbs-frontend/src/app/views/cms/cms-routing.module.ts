import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CmsComponent } from './cms.component';
import { CmsAddEditComponent } from './cms-add-edit/cms-add-edit.component';
import { CmsListComponent } from './cms-list/cms-list.component';
import { CmsPageBuilderComponent } from './cms-page-builder/cms-page-builder.component';
import { CmsPageViewComponent } from './cms-page-view/cms-page-view.component';
import { CmsComponentViewComponent } from './cms-component-view/cms-component-view.component';

const routes: Routes = [
  {
    path: '',
    component: CmsComponent,
    data: {
      title: 'CMS Management',
    },
    children: [
      {
        path: 'add',
        component: CmsAddEditComponent,
        data: {
          title: 'Add CMS',
          permission: 'CMS_CREATE',
          preTitle: 'CMS List',
          preUrl: 'cms/list',
        },
      },
      {
        path: 'edit/:id',
        component: CmsAddEditComponent,
        data: {
          title: 'Edit CMS',
          permission: 'CMS_UPDATE',
          preTitle: 'CMS List',
          preUrl: 'cms/list',
        },
      },
      {
        path: 'page-builder/:id',
        component: CmsPageBuilderComponent,
        data: {
          title: 'Page Builder',
          permission: 'PAGE_BUILDER_CREATE',
          preTitle: 'CMS List',
          preUrl: 'cms/list',
        },
      },
      {
        path: 'view-template/:id',
        component: CmsPageViewComponent,
        data: {
          title: 'View Template',
          permission: 'PAGE_BUILDER_LOAD',
          preTitle: 'CMS List',
          preUrl: 'cms/list',
        },
      },
      {
        path: 'view-component-template/:id',
        component: CmsComponentViewComponent,
        data: {
          title: 'View Component Template',
          permission: 'PAGE_BUILDER_LOAD',
          preTitle: 'CMS List',
          preUrl: 'cms/list',
        },
      },
      {
        path: 'list',
        component: CmsListComponent,
        data: {
          title: 'CMS List',
          permission: 'CMS_LIST',
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
export class CmsRoutingModule {}
