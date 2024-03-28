import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateUserGroupComponent } from './create-user-group.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../core/core.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../core/_common/shared.module';
import { CreateUserGroupRoutingModule } from './create-user-group-routing.module';

const PAGE_COMPONENTS = [
  CreateUserGroupComponent,
]

@NgModule({
  declarations: [...PAGE_COMPONENTS],
  imports: [
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    CommonModule,
    NgSelectModule,
    SharedModule,
    CreateUserGroupRoutingModule
  ],
  exports: [
    ...PAGE_COMPONENTS
   ]
})
export class CreateUserGroupModule { }
