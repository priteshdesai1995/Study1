import { RouterModule } from '@angular/router';
import { PreventSubmitOnEnterDirective } from './_directive/prevent-submit-on-enter.directive';
import { LoaderDirective } from './_directive/loader.directive';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FooterComponent } from './_component/footer/footer.component';
import { HeaderComponent } from './_component/header/header.component';
import { InvalidFormScrollDirective } from './_directive/invalid-form-scroll.directive';
import { OtpInputComponent } from './_component/otp-input/otp-input.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SpaceRemoveDirective } from './_directive/space-remove.directive';
import { SharedModule } from './_common/shared.module';
import { ListTableComponent } from './_component/list-table/list-table.component';
import { GraphModule } from './_component/graph/graph.module';
import { PersonaCardComponent } from './_component/persona-card/persona-card.component';
import { ViewPersonaComponent } from './_component/view-Persona/view-Persona.component';
import { CreateJourneyComponent } from './_component/journey/create-journey/create-journey.component';
import { ListJourneyComponent } from './_component/journey/list-journey/list-journey.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { BarGraphModule } from './_component/bar-graph/bar-graph.module';
import { MapComponent } from './_component/map/map.component';
import { NgxMasonryModule } from 'ngx-masonry';
import { LineGraphModule } from './_component/line-graph/line-graph.module';
import { CurveGraphModule } from './_component/curve-graph/curve-graph.module';
import { NzCollapseModule } from 'ng-zorro-antd/collapse';
import { NzProgressModule } from 'ng-zorro-antd/progress';

const shared = [
  FooterComponent,
  HeaderComponent,
  InvalidFormScrollDirective,
  LoaderDirective,
  SpaceRemoveDirective,
  PreventSubmitOnEnterDirective,
  OtpInputComponent,
  ListTableComponent,
  ViewPersonaComponent,
  PersonaCardComponent,
  CreateJourneyComponent,
  ListJourneyComponent,
  MapComponent,
  ];

@NgModule({
  declarations: [shared],
  exports: [
    shared
  ],
  imports: [
    NgxMasonryModule,
    CommonModule,
    FormsModule, 
    SharedModule,
    ReactiveFormsModule,
    NgSelectModule,
    RouterModule,GraphModule,BarGraphModule,LineGraphModule,CurveGraphModule,
    NzSpinModule,
    NzCollapseModule,
    NzProgressModule
    ],

})
export class CoreModule { }
