import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { UserRegistrationComponent } from './user-registration.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgWizardModule, NgWizardConfig, THEME } from 'ng-wizard';
import { BusinessComponent } from './business/business.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { PersonalisedPlansComponent } from './personalised-plans/personalised-plans.component';
import { NgxCaptchaModule } from 'ngx-captcha';
import { NgSelectModule } from '@ng-select/ng-select';
import { CoreModule } from '../../core/core.module';

const ngWizardConfig: NgWizardConfig = {
  theme: THEME.circles
};
const routes: Routes = [
  {
    path: '',
    component: UserRegistrationComponent
  }
];

@NgModule({
  declarations: [
    UserRegistrationComponent,
    BusinessComponent,
    SignUpComponent,
    PersonalisedPlansComponent,
  ],
  imports: [
    CommonModule,
    NgbModule,
    FormsModule, ReactiveFormsModule,
    CoreModule,
    NgWizardModule.forRoot(ngWizardConfig),
    CommonModule,
    NgxCaptchaModule,
    NgSelectModule,
    RouterModule.forChild(routes)

  ]
})
export class UserRegistrationModule { }
