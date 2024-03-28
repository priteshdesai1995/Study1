import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AnnouncementRoutingModule } from './announcement-routing.module';
import { AnnouncementComponent } from './announcement.component';
import { AnnouncementListComponent } from './announcement-list/announcement-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxPermissionsModule } from 'ngx-permissions';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AnnouncementDetailsComponent } from './announcement-details/announcement-details.component';
import { AnnouncementAddEditComponent } from './announcement-add-edit/announcement-add-edit.component';
import { AnnouncementReceiversComponent } from './announcement-receivers/announcement-receivers.component';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { SharedModule } from '../../_module/shared.module';
import { CKEditorModule } from 'ngx-ckeditor';
import { BsDatepickerModule } from 'ngx-bootstrap/datepicker';
import { MatDatepickerModule, MatFormFieldModule, MatInputModule } from '@angular/material';
import { LoaderModule } from '../../containers';

@NgModule({
  imports: [
    CommonModule,
    AnnouncementRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    NgxDatatableModule,
    NgxPermissionsModule,
    NgbModule,
    LoaderModule,
    BsDatepickerModule.forRoot(),
    SharedModule,
    CKEditorModule,
    MatInputModule,
    MatFormFieldModule,
  ],
  declarations: [
    AnnouncementComponent,
    AnnouncementListComponent,
    AnnouncementAddEditComponent,
    AnnouncementDetailsComponent,
    AnnouncementReceiversComponent,
  ],
})
export class AnnouncementModule {}
