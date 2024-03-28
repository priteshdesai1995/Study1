import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home.component';
import { RouterModule, Routes } from '@angular/router';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';
import { NzProgressModule } from 'ng-zorro-antd/progress';


const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    data: {
      title: ''
    }

  }
];

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    CoreModule,
    SharedModule,
    NzProgressModule,
    RouterModule.forChild(routes)
  ]
})
export class HomeModule { }
