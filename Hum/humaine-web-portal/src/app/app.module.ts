import { ErrorInterceptor } from './core/_interceptor/error.interceptor';
import { LoginGuard } from './core/_guard/login.guard';
import { RouterModule } from '@angular/router';
import { AuthGuard } from './core/_guard/auth.guard';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { HttpConfigInterceptor } from './core/_interceptor/httpconfig.interceptor';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import { CoreModule } from './core/core.module';
import { NgWizardModule, NgWizardConfig, THEME } from 'ng-wizard';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DefaultLayoutComponent } from './containers/default-layout/default-layout.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule, } from 'ng-zorro-antd/form';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzTableModule } from 'ng-zorro-antd/table';
import { NzUploadModule } from 'ng-zorro-antd/upload';
import { NZ_I18N } from 'ng-zorro-antd/i18n';
import { en_US } from 'ng-zorro-antd/i18n';
import { HashLocationStrategy, LocationStrategy, registerLocaleData } from '@angular/common';
import en from '@angular/common/locales/en';
import { SharedModule } from './core/_common/shared.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { NgIdleKeepaliveModule } from '@ng-idle/keepalive';

const ngWizardConfig: NgWizardConfig = {
  theme: THEME.default
};
registerLocaleData(en);

@NgModule({
  declarations: [
    AppComponent,
    DefaultLayoutComponent,
    DashboardComponent,
  ],
  imports: [
    ScrollingModule,
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    CoreModule,
    NzButtonModule,
    NzFormModule,
    NzCardModule,
    NzInputModule,
    NzSelectModule,
    NzLayoutModule,
    NzTableModule,
    NzUploadModule,
    SharedModule,
    NgSelectModule,
    NzDropDownModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot(), // ToastrModule added
    FormsModule, ReactiveFormsModule,
    NgWizardModule.forRoot(ngWizardConfig),
    NgIdleKeepaliveModule.forRoot(),
    RouterModule
  ],
  providers: [
    AuthGuard,
    LoginGuard,
    /** config ng-zorro-antd i18n (language && date) **/
    { provide: NZ_I18N, useValue: en_US },
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: HttpConfigInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
