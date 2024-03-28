import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeatmapComponent } from './heatmap.component';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';

const routes: Routes = [
  {
    path: '',
    component: HeatmapComponent
  }
];

@NgModule({
  declarations: [
    HeatmapComponent
  ],
  imports: [
    FormsModule, 
    ReactiveFormsModule ,
    CommonModule,
    CoreModule,
    SharedModule,
    CommonModule,
    NgSelectModule,
    RouterModule.forChild(routes)
  ]
})
export class HeatmapModule { }
