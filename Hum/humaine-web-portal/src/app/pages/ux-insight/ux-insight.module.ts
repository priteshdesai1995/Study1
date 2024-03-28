import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { UxInsightComponent } from './ux-insight.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';
import { BarGraphModule } from '../../core/_component/bar-graph/bar-graph.module';

const routes: Routes = [
  {
    path: '',
    component: UxInsightComponent
  }
];

@NgModule({
  declarations: [
    UxInsightComponent
  ],
  imports: [
    CommonModule,
    FormsModule, ReactiveFormsModule,
    CoreModule,
    SharedModule,
    BarGraphModule,
    RouterModule.forChild(routes)
  ]
})

export class UxInsightModule { }
