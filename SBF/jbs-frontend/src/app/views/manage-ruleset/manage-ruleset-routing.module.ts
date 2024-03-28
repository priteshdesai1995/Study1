import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ManageRulesetComponent } from './manage-ruleset.component';
import { RulesetAddEditComponent } from './ruleset-add-edit/ruleset-add-edit.component';
import { RulesetListComponent } from './ruleset-list/ruleset-list.component';

const routes: Routes = [
  {
    path: '',
    component: ManageRulesetComponent,
    data: {
      title: 'Manage Rule Sets',
    },
    children: [
      {
        path: 'add',
        component: RulesetAddEditComponent,
        data: {
          title: 'Add Rule Sets',
          permission: 'RULESETS_CREATE',
          preTitle: 'Rule Sets List',
          preUrl: 'rulesets/list',
        },
      },
      {
        path: 'edit/:id',
        component: RulesetAddEditComponent,
        data: {
          title: 'Edit Rule Sets',
          permission: 'RULESETS_UPDATE',
          preTitle: 'Rule Sets List',
          preUrl: 'rulesets/list',
        },
      },
      {
        path: 'list',
        component: RulesetListComponent,
        data: {
          title: 'Rule Sets List',
          permission: 'RULESETS_LIST',
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
export class ManageRulesetRoutingModule {}
