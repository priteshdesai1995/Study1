import { GraphModule } from './../../core/_component/graph/graph.module';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ViewUserGroupComponent } from './view-user-group.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../core/core.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../core/_common/shared.module';
import { ViewUserGroupRoutingModule } from './view-user-group-routing.module';
import { NzCarouselModule } from 'ng-zorro-antd/carousel';
import { BubbleViewComponent } from '../../core/_component/bubble-view/bubble-view/bubble-view.component';

const PAGE_COMPONENTS = [
  ViewUserGroupComponent,
  BubbleViewComponent
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
    ViewUserGroupRoutingModule,
    GraphModule,
    NzCarouselModule
  ],
  exports: [
    ...PAGE_COMPONENTS
   ]
})

export class ViewUserGroupModule { }
