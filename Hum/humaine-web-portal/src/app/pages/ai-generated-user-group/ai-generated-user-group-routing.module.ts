import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AIGeneratedUserGroupComponent } from './ai-generated-user-group.component';
import { ViewPersonaComponent } from '../../core/_component/view-Persona/view-Persona.component';
import { URLS } from '../../core/_constant/api.config';
import { RouteURLIdentity } from '../../core/_constant/app-constant';

const routes: Routes = [
  {
    path: '',
    component: AIGeneratedUserGroupComponent,
    data: {
      title: '',
      identity: RouteURLIdentity.AI_GENERATED_USER_GROUP,
    }
  },
  {
    path: 'ai/viewPersona/:id',
    component: ViewPersonaComponent,
    data: {
      isMockData: false,
      allowEditGroupName: false,
      apiURL: URLS.aiViewPersona,
      breadcrumb: 'AI User Group',
      title: 'AI User Group'
    }
  },
  {
    path: 'saved/viewPersona/:id',
    component: ViewPersonaComponent,
    data: {
      isMockData: false,
      allowEditGroupName: false,
      apiURL: URLS.savedAiViewPersona,
      breadcrumb: 'AI User Group',
      title: 'AI User Group'
    }
  }

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AiGeneratedUserGroupRoutingModule { }
