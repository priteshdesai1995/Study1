import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AnnouncementComponent } from './announcement.component';
import { AnnouncementListComponent } from './announcement-list/announcement-list.component';
import { AnnouncementDetailsComponent } from './announcement-details/announcement-details.component';
import { AnnouncementAddEditComponent } from './announcement-add-edit/announcement-add-edit.component';

const routes: Routes = [
  {
    path: '',
    component: AnnouncementComponent,
    data: {
      title: 'Manage Announcement',
    },
    children: [
      {
        path: 'list',
        component: AnnouncementListComponent,
        data: {
          title: 'Manage Announcement List',
          permission: 'MANAGE_ANNOUNCEMENT_LIST',
        },
      },
      {
        path: 'add',
        component: AnnouncementAddEditComponent,
        data: {
          title: 'Add Announcement',
          permission: 'MANAGE_ANNOUNCEMENT_CREATE',
          preTitle: 'Manage Announcement List',
          preUrl: 'manage-announcement/list',
        },
      },
      {
        path: 'view/:id',
        component: AnnouncementDetailsComponent,
        data: {
          title: 'View Announcement Details',
          permission: 'MANAGE_ANNOUNCEMENT_VIEW',
          preTitle: 'Manage Announcement List',
          preUrl: 'manage-announcement/list',
        },
      },
      { path: '', redirectTo: 'list', pathMatch: 'full' },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AnnouncementRoutingModule {}
