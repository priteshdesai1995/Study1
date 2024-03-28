import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UxLayoutComponent } from './ux-layout.component';
import { CoreModule } from '../../core/core.module';
import { SharedModule } from '../../core/_common/shared.module';
import { RouterModule, Routes } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';


const routes: Routes = [
  {
    path: '',
    component: UxLayoutComponent
  }
];

@NgModule({
  declarations: [
    UxLayoutComponent
  ],
  imports: [
    CommonModule,
    CoreModule,
    SharedModule,
    NgSelectModule,
    RouterModule.forChild(routes)
  ]
})

export class UxLayoutModule { }
