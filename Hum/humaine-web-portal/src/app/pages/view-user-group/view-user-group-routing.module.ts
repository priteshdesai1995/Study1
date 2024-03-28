import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ViewUserGroupComponent } from './view-user-group.component';
import { ViewPersonaComponent } from '../../core/_component/view-Persona/view-Persona.component';
import { URLS } from '../../core/_constant/api.config';

const routes: Routes = [
  {
    path: '',
    component: ViewUserGroupComponent,
    data: {
      title: ''
    }
  },
  {
    path: 'viewPersona/:id',
    component: ViewPersonaComponent,
    data: {
      isMockData : false,
      allowEditGroupName :false,
      apiURL: URLS.myUserViewPersona,
      breadcrumb: 'My User Group',
      title: 'My User Group',
      showMerkindelIntelligence: true,
      merkindeIntelligenceDetailsURL: URLS.populerProductList
    }
  },
  {
    path: 'groups/viewPersona/:id',
    component: ViewPersonaComponent,
    data: {
      isMockData : false,
      withMultipleGroups: false,
      allowEditGroupName :false,
      apiURL: URLS.groupContainerPersona,
      breadcrumb: 'My User Group',
      title: 'My User Group',
      showMerkindelIntelligence: true,
      merkindeIntelligenceDetailsURL: URLS.populerProductList
    }
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ViewUserGroupRoutingModule { }
