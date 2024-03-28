import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApdexGraphComponent } from './apdex-graph.component';
import { ChartsModule } from 'ng2-charts';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../_common/shared.module';



@NgModule({
  declarations: [ApdexGraphComponent],
  imports: [
    CommonModule,
    ChartsModule,
    FormsModule,
    SharedModule
  ],
  exports: [ApdexGraphComponent],

})
export class ApdexGraphModule { }
