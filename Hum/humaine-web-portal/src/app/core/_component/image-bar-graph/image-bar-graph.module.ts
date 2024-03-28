import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageBarGraphComponent } from './image-bar-graph.component';
import { ChartsModule } from 'ng2-charts';
import { SharedModule } from '../../_common/shared.module';



@NgModule({
  declarations: [ImageBarGraphComponent],
  imports: [
    CommonModule,
    ChartsModule,
    SharedModule
  ],
  exports: [ImageBarGraphComponent],

})
export class ImageBarGraphModule { }
