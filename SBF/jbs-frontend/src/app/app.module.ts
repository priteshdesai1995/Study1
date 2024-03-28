import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';

import { PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { PERFECT_SCROLLBAR_CONFIG } from 'ngx-perfect-scrollbar';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
};

import { AppComponent } from './app.component';

// Import containers
import { DefaultLayoutComponent, LoaderModule } from './containers';

import { P404Component } from './views/error/404.component';
import { P403Component } from './views/error/403.component';
import { P500Component } from './views/error/500.component';
import { LoginComponent } from './views/login/login.component';
import { RegisterComponent } from './views/register/register.component';
import { ResetPasswordComponent } from './views/reset-password/reset-password.component';

const APP_CONTAINERS = [DefaultLayoutComponent];

import { AppAsideModule, AppBreadcrumbModule, AppHeaderModule, AppFooterModule, AppSidebarModule } from '@coreui/angular';

// Import routing module
import { AppRoutingModule } from './app.routing';

// Import 3rd party components
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ChartsModule } from 'ng2-charts';

import { AuthGuard } from './_guards/auth.guard';
import { JwtInterceptor } from './_helpers/jwt.interceptor';
import { ErrorInterceptor } from './_helpers/error.interceptor';

import { ModalModule } from 'ngx-bootstrap/modal';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import { TreeviewModule } from 'ngx-treeview';
import { NgxPermissionsModule } from 'ngx-permissions';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TextUserListComponent } from './views/manage-survey/survey-report/text-user-list.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { CheckUserListComponent } from './views/manage-survey/survey-report/check-user-list.component';
import { SurveyUserReportComponent } from './views/manage-survey/survey-list/user-report.component';
import { PopupImageOpenComponent } from './views/announcement/announcement-details/popup-image-open.component';
import { OfferReportListComponent } from './views/manage-offer/manage-offer-list/offer-report-list.component';
import { SharedModule } from './_module/shared.module';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';

const BASE_MODULES = [CommonModule, FormsModule, ReactiveFormsModule];

@NgModule({
  imports: [
    BrowserModule,
    ...BASE_MODULES,
    HttpClientModule,
    AppRoutingModule,
    AppAsideModule,
    AppBreadcrumbModule.forRoot(),
    AppFooterModule,
    AppHeaderModule,
    AppSidebarModule,
    PerfectScrollbarModule,
    BsDropdownModule.forRoot(),
    BrowserAnimationsModule,
    TabsModule.forRoot(),
    ChartsModule,
    ModalModule.forRoot(),
    NgbModule,
    ToastrModule.forRoot({
      timeOut: 5000,
      closeButton: true,
      preventDuplicates: true,
    }), // ToastrModule added
    TreeviewModule.forRoot(),
    NgxPermissionsModule.forRoot(),
    LoaderModule,
    NgxDatatableModule,
    SharedModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),
  ],
  declarations: [
    AppComponent,
    ...APP_CONTAINERS,
    P403Component,
    P404Component,
    P500Component,
    LoginComponent,
    RegisterComponent,
    ResetPasswordComponent,
    TextUserListComponent,
    CheckUserListComponent,
    SurveyUserReportComponent,
    PopupImageOpenComponent,
    OfferReportListComponent,
  ],
  entryComponents: [
    TextUserListComponent,
    CheckUserListComponent,
    SurveyUserReportComponent,
    PopupImageOpenComponent,
    OfferReportListComponent,
  ],
  providers: [
    AuthGuard,
    { provide: LocationStrategy, useClass: HashLocationStrategy },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
