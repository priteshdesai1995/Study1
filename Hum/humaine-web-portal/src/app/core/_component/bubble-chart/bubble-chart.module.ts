import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BubbleChartComponent } from './bubble-chart.component';
import { ChartsModule } from 'ng2-charts';
import { SharedModule } from '../../_common/shared.module';



@NgModule({
  declarations: [BubbleChartComponent],
  imports: [
    CommonModule,
    ChartsModule,
    SharedModule
  ],
  exports: [BubbleChartComponent],
})
export class BubbleChartModule { }
