import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AIGeneratedUserGroupComponent } from './ai-generated-user-group.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../core/core.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../core/_common/shared.module';
import { AiGeneratedUserGroupRoutingModule } from './ai-generated-user-group-routing.module';

const PAGE_COMPONENTS = [
  AIGeneratedUserGroupComponent,
]

@NgModule({
  declarations: [...PAGE_COMPONENTS],
  imports: [
    FormsModule, 
    ReactiveFormsModule ,
    CoreModule,
    CommonModule,
    NgSelectModule,
    SharedModule,
    AiGeneratedUserGroupRoutingModule
    ],
    exports: [
      ...PAGE_COMPONENTS
     ]
})

export class AIGeneratedUserGroupModule { }
