import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { BsMediaComponent } from './bs-media.component';
import { BsMediaListComponent } from './bs-media-list/bs-media-list.component';

const routes: Routes = [
  {
    path: '',
    component: BsMediaComponent,
    data: {
      title: 'Manage Bs Media',
    },
    children: [
      {
        path: 'list',
        component: BsMediaListComponent,
        data: {
          title: 'Bs Media List',
          permission: 'BS_MEDIA_LIST',
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
export class BsMediaRoutingModule {}
