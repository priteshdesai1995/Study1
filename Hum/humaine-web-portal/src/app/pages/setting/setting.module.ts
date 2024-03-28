import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CoreModule } from '../../core/core.module';
import { RouterModule, Routes } from '@angular/router';
import { SettingComponent } from './setting.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NzCollapseModule } from 'ng-zorro-antd/collapse';
import { SharedModule } from '../../core/_common/shared.module';
import { NzStepsModule } from 'ng-zorro-antd/steps';


const routes: Routes = [
  {
    path: '',
    component: SettingComponent
  }
];

@NgModule({
  declarations: [
    SettingComponent
  ],
  imports: [
    FormsModule, 
    ReactiveFormsModule ,
    CommonModule,
    CoreModule,
    SharedModule,
    CommonModule,NzStepsModule,
    NzCollapseModule,
    RouterModule.forChild(routes)
  ]
})
export class SettingModule { }
