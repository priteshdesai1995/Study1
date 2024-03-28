import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HorizontalBarChartComponent } from './horizontal-bar-chart.component';
import { ChartsModule } from 'ng2-charts';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../_common/shared.module';



@NgModule({
  declarations: [HorizontalBarChartComponent],
  imports: [
    CommonModule,
    ChartsModule,
    FormsModule,SharedModule
  ],
  exports: [HorizontalBarChartComponent],

})
export class HorizontalBarChartModule { }
