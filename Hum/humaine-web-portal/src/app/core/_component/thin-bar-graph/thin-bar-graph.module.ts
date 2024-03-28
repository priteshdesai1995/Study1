import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ThinBarGraphComponent } from './thin-bar-graph.component';
import { ChartsModule } from 'ng2-charts';



@NgModule({
  declarations: [ThinBarGraphComponent],
  imports: [
    CommonModule,
    ChartsModule
  ],
  exports: [ThinBarGraphComponent],

})
export class ThinBarGraphModule { }
