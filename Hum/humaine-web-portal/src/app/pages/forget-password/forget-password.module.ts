import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ForgetPasswordComponent } from './forget-password.component';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CoreModule } from '../../core/core.module';


const routes: Routes = [
  {
    path: '',
    component: ForgetPasswordComponent
  }
];

@NgModule({
  declarations: [ForgetPasswordComponent],
  imports: [
    CommonModule,
    FormsModule, ReactiveFormsModule,
    CoreModule,
    RouterModule.forChild(routes)

  ]
})
export class ForgetPasswordModule { }
