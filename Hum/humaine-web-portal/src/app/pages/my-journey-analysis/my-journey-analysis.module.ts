import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { MyJourneyAnalysisRoutingModule } from './my-journey-analysis-routing.module';
import { MyJourneyAnalysisComponent } from './my-journey-analysis.component';
import { SharedModule } from '../../core/_common/shared.module';
import { CoreModule } from '../../core/core.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

const PAGE_COMPONENTS = [
  MyJourneyAnalysisComponent
]
@NgModule({
  declarations: [...PAGE_COMPONENTS],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    CoreModule,
    CommonModule,
    SharedModule,
    MyJourneyAnalysisRoutingModule
  ],
  exports: [
    ...PAGE_COMPONENTS
  ]
})
export class MyJourneyAnalysisModule { }
