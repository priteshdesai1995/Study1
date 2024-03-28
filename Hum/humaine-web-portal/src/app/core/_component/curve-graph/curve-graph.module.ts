import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CurveGraphComponent } from './curve-graph.component';
import { ChartsModule } from 'ng2-charts';
import { FormsModule } from '@angular/forms';
import { SharedModule } from '../../_common/shared.module';

@NgModule({
  declarations: [CurveGraphComponent],
  imports: [
    CommonModule,
    ChartsModule,
    FormsModule,
    SharedModule
  ],
  exports: [CurveGraphComponent],

})
export class CurveGraphModule { }
