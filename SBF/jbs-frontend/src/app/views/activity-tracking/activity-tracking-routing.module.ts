import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ActivityTrackingComponent } from './activity-tracking.component';
import { ListActivityTrackingComponent } from './list-activity-tracking/list-activity-tracking.component';

const routes: Routes = [
  {
    path: '',
    component: ActivityTrackingComponent,
    data: {
      title: 'Activity Tracking',
    },
    children: [
      {
        path: 'list',
        component: ListActivityTrackingComponent,
        data: {
          title: 'Activity Tracking List',
          permission: 'ACTIVITY_TRACKING_LIST',
          preTitle: 'Activity Tracking List',
          preUrl: 'activity-tracking/list',
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
export class ActivityTrackingRoutingModule {}
