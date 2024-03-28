import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LiveComponent } from './live.component';
import { RouterModule, Routes } from '@angular/router';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';
import { GraphModule } from '../../core/_component/graph/graph.module';
import { LineGraphModule } from '../../core/_component/line-graph/line-graph.module';
import { ApdexGraphModule } from '../../core/_component/apdex-graph/apdex-graph.module';
import { CurveGraphModule } from '../../core/_component/curve-graph/curve-graph.module';
import { ThinBarGraphModule } from '../../core/_component/thin-bar-graph/thin-bar-graph.module';
import { HorizontalBarChartModule } from '../../core/_component/horizontal-bar-chart/horizontal-bar-chart.module';
import { ImageBarGraphModule } from '../../core/_component/image-bar-graph/image-bar-graph.module';


const routes: Routes = [
  {
    path: '',
    component: LiveComponent,
    data: {
      title: ''
    }

  }
];

@NgModule({
  declarations: [
    LiveComponent
  ],
  imports: [
    CommonModule,
    CoreModule,
    SharedModule,
    GraphModule,
    ApdexGraphModule,
    LineGraphModule,
    CurveGraphModule,
    ThinBarGraphModule,
    HorizontalBarChartModule,
    ImageBarGraphModule,
    RouterModule.forChild(routes)
  ]
})
export class LiveModule { }
