import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { CategoryComponent } from './category.component';
import { CategoryAddEditComponent } from './category-add-edit/category-add-edit.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { AuthGuard } from './../../_guards/auth.guard';
import { CategoryTreeviewComponent } from './category-treeview/category-treeview.component';

const routes: Routes = [
  {
    path: '',
    component: CategoryComponent,
    data: {
      title: 'Manage Category',
    },
    children: [
      {
        path: 'add',
        component: CategoryAddEditComponent,
        data: {
          title: 'Add Category',
          permission: 'CATEGORY_CREATE',
          preTitle: 'Category List',
          preUrl: 'category/list',
        },
      },
      {
        path: 'treeview',
        component: CategoryTreeviewComponent,
        data: {
          title: 'Category Treeview',
          permission: 'CATEGORY_TREEVIEW',
          preTitle: 'Category List',
          preUrl: 'category/list',
        },
      },
      {
        path: 'edit/:id',
        component: CategoryAddEditComponent,
        data: {
          title: 'Edit Category',
          permission: 'CATEGORY_UPDATE',
          preTitle: 'Category List',
          preUrl: 'category/list',
        },
      },
      {
        path: 'list',
        component: CategoryListComponent,
        data: {
          title: 'Category List',
          permission: 'CATEGORY_LIST',
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
export class CategoryRoutingModule {}
