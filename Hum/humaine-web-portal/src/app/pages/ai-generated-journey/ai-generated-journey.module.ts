import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AIGeneratedJourneyRoutingModule } from './ai-generated-journey-routing.module';
import { AIGeneratedJourneyComponent } from './ai-generated-journey.component';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';


const PAGE_COMPONENTS = [
  AIGeneratedJourneyComponent
]

@NgModule({
  declarations: [...PAGE_COMPONENTS],
  imports: [
    CommonModule,
    CoreModule,
    CommonModule,
    SharedModule,
    AIGeneratedJourneyRoutingModule
  ],
  exports: [
    ...PAGE_COMPONENTS
   ]
})
export class AIGeneratedJourneyModule { }
