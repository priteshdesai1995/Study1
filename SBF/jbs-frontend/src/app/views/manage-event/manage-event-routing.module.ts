import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { EventListComponent } from './event-list/event-list.component';
import { ManageEventComponent } from './manage-event.component';
import { EventAddEditComponent } from './event-add-edit/event-add-edit.component';
import { EventViewComponent } from './event-view/event-view.component';

const routes: Routes = [
  {
    path: '',
    component: ManageEventComponent,
    data: {
      title: 'Manage Event',
    },
    children: [
      {
        path: 'add',
        component: EventAddEditComponent,
        data: {
          title: 'Add Event',
          permission: 'EVENT_CREATE',
          preTitle: 'Event List',
          preUrl: 'rulesets/list',
        },
      },
      {
        path: 'edit/:id',
        component: EventAddEditComponent,
        data: {
          title: 'Edit Event',
          permission: 'EVENT_UPDATE',
          preTitle: 'Event List',
          preUrl: 'event/list',
        },
      },
      {
        path: 'view/:id',
        component: EventViewComponent,
        data: {
          title: 'View Event',
          permission: 'EVENT_VIEW',
          preTitle: 'Event List',
          preUrl: 'event/list',
        },
      },
      {
        path: 'list',
        component: EventListComponent,
        data: {
          title: 'Event List',
          permission: 'EVENT_LIST',
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
export class ManageEventRoutingModule {}
