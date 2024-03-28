import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AdminSuggestionComponent } from './admin-suggestion.component';
import { AdminSuggestionListComponent } from './admin-suggestion-list/admin-suggestion-list.component';

const routes: Routes = [
  {
    path: '',
    component: AdminSuggestionComponent,
    data: {
      title: 'Suggestion',
    },
    children: [
      {
        path: 'list',
        component: AdminSuggestionListComponent,
        data: {
          title: 'Suggestion List',
          permission: 'SUGGESTION_LIST',
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
export class AdminSuggestionRoutingModule {}
