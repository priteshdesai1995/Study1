import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateJourneyComponent } from './create-journey.component';
import { SharedModule } from '../../../_common/shared.module';
import { RouterModule, Routes } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../../core.module';
import { GraphComponent } from '../../graph/graph.component';
import { GraphModule } from '../../graph/graph.module';
import { NzSpinModule } from 'ng-zorro-antd/spin';

const routes: Routes = [
  {
    path: '',
    component: CreateJourneyComponent
  }
];

const PAGE_COMPONENTS = [
  ,
]

@NgModule({
  declarations: [CreateJourneyComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule, ReactiveFormsModule,
    NgSelectModule,
    CoreModule,
    GraphModule,
    NzSpinModule,
    RouterModule.forChild(routes)
  ]
})
export class CreateJourneyModule { }
