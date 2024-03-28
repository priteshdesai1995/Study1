import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateUserGroupComponent } from './create-user-group.component';
import { URLS } from '../../core/_constant/api.config';
import { ViewPersonaComponent } from '../../core/_component/view-Persona/view-Persona.component';

const routes: Routes = [
  {
    path: '',
    component: CreateUserGroupComponent,
    data: {
      title: ''
    }
  },
  {
    path: 'viewPersona/:id',
    component: ViewPersonaComponent,
    data: {
      editGroupAPI:URLS.editGroupNamePersona,
      allowEditGroupName :true,
      apiURL: URLS.manualViewPersona,
      breadcrumb: 'Manual User Group',
      title: 'Manual User Group'
      //allowDemoGraphicData: true
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CreateUserGroupRoutingModule { }
