import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CategoryComponent } from './category.component';
import { CategoryRoutingModule } from './category-routing.module';
import { CategoryAddEditComponent } from './category-add-edit/category-add-edit.component';
import { CategoryListComponent } from './category-list/category-list.component';
import { CategoryTreeviewComponent } from './category-treeview/category-treeview.component';
import { TreeviewModule } from 'ngx-treeview';
import { TreeModule } from 'angular-tree-component';
import { NgxPermissionsModule } from 'ngx-permissions';
import { NgxTreeSelectModule } from 'ngx-tree-select';
import { LoaderModule } from '../../containers';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedModule } from '../../_module/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CategoryRoutingModule,
    NgxDatatableModule,
    TreeModule,
    NgxPermissionsModule,
    TreeviewModule.forRoot(),
    NgxTreeSelectModule.forRoot({
      allowFilter: false,
      allowParentSelection: true,
      expandMode: 'All',
    }),
    LoaderModule,
    SharedModule,
  ],
  declarations: [CategoryComponent, CategoryAddEditComponent, CategoryListComponent, CategoryTreeviewComponent],
})
export class CategoryModule {}
