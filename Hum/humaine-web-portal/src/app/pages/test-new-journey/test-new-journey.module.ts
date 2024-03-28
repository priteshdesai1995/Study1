import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TestNewJourneyRoutingModule } from './test-new-journey-routing.module';
import { TestNewJourneyComponent } from './test-new-journey.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';

const PAGE_COMPONENTS = [
  TestNewJourneyComponent
]

@NgModule({
  declarations: [...PAGE_COMPONENTS],
  imports: [
    CommonModule,
    FormsModule, 
    ReactiveFormsModule ,
    CoreModule,
    CommonModule,
    SharedModule,
    TestNewJourneyRoutingModule
  ],
  exports: [
    ...PAGE_COMPONENTS
   ]
})

export class TestNewJourneyModule { }
