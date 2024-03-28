import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NumberFormatPipe } from '../../_common/number-format.pipe';
import { SharedModule } from '../../_common/shared.module';


@NgModule({
  declarations: [NumberFormatPipe],
  imports: [
    CommonModule,
    SharedModule
  ]
})
export class MapModule { }
