import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsModule } from 'ng2-charts';
import { BarGraphComponent } from './bar-graph.component';



@NgModule({
  declarations: [BarGraphComponent],
  imports: [
    CommonModule,
    ChartsModule
  ],
  exports: [BarGraphComponent],

})
export class BarGraphModule { }
