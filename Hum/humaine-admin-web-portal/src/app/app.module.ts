import { RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import { NgWizardModule, NgWizardConfig, THEME } from 'ng-wizard';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DefaultLayoutComponent } from './containers/default-layout/default-layout.component';
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
import { NgSelectModule } from '@ng-select/ng-select';
import { NzDropDownModule } from 'ng-zorro-antd/dropdown';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { CoreModule } from './_core/core.module';
import { SharedModule } from './_core/_common/shared.module';
import { HttpConfigInterceptor } from './_core/_interceptor/httpconfig.interceptor';
import { ErrorInterceptor } from './_core/_interceptor/error.interceptor';


const ngWizardConfig: NgWizardConfig = {
  theme: THEME.default
};

@NgModule({
  declarations: [
    AppComponent,
    DefaultLayoutComponent
  ],
  imports: [
    ScrollingModule,
    NgbModule,
    BrowserModule,
    AppRoutingModule,
    CoreModule,
    HttpClientModule,
    SharedModule,
    NgSelectModule,
    NzDropDownModule,
    BrowserAnimationsModule, // required animations module
    ToastrModule.forRoot(), // ToastrModule added
    FormsModule, ReactiveFormsModule,
    NgWizardModule.forRoot(ngWizardConfig),
    RouterModule,
    NzButtonModule,
    NzFormModule,
    NzCardModule,
    NzInputModule,
    NzSelectModule,
    NzLayoutModule,
    NzTableModule,
    NzUploadModule,
  ],
  
  providers: [
     /** config ng-zorro-antd i18n (language && date) **/
     { provide: NZ_I18N, useValue: en_US },
     { provide: LocationStrategy, useClass: HashLocationStrategy },
     { provide: HTTP_INTERCEPTORS, useClass: HttpConfigInterceptor, multi: true },
     { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
