import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsModule } from 'ng2-charts';
import { LineGraphComponent } from './line-graph.component';
import { SharedModule } from '../../_common/shared.module';

@NgModule({
  declarations: [LineGraphComponent],
  imports: [
    CommonModule,
    ChartsModule ,
    SharedModule
  ],
  exports: [LineGraphComponent],

})
export class LineGraphModule { }
