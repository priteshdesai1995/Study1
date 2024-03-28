import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateJourneyComponent } from '../../core/_component/journey/create-journey/create-journey.component';
import { ListJourneyComponent } from '../../core/_component/journey/list-journey/list-journey.component';
import { URLS } from '../../core/_constant/api.config';
import { RouteURLIdentity } from '../../core/_constant/app-constant';
import { MenuLinks } from '../../core/_enums/MenuLinks';
import { AIGeneratedJourneyComponent } from './ai-generated-journey.component';



const API_URLS = {
  apiURL: URLS.testJourneyElement,
  userURL: URLS.testJourneyGroups,
}

const routes: Routes = [
  {
    path: '',
    component: AIGeneratedJourneyComponent,
    data: {
      title: ''
    },
    children: [
      {
        path: 'ai-generated-journey',
        component: CreateJourneyComponent,
        data: {
          ...API_URLS,
          identity: RouteURLIdentity.AI_GENERATED_JOURNEY,
          breadcrumb: '',
          isCreate: true,
          menuCountKey: MenuLinks.TEST_NEW_JOURNEY,
          title: 'AI Generated Journeys',
          listingRoute : '/customer-journey/ai-journey/ai-journey-listing'
        }
      },
      {
        path: 'detail/:id',
        component: CreateJourneyComponent,
        data: {
          ...API_URLS,
          identity: RouteURLIdentity.AI_GENERATED_JOURNEY,
          isView: true,
          breadcrumb: '',
          title: 'AI Generated Journeys',
          listingRoute : '/customer-journey/ai-journey/test-journey-listing',
        }
      },
      {
        path: 'edit/:id',
        component: CreateJourneyComponent,
        data: {
          isEdit: true,
          identity: RouteURLIdentity.AI_GENERATED_JOURNEY,
          viewPageURL: 'customer-journey/ai-generated-journey/detail',
          breadcrumb: '',
          title: 'AI Generated Journeys',
          listingRoute : '/customer-journey/ai-journey/test-journey-listing'
        }
      },
      {
        path: 'ai-journey-listing',
        component: ListJourneyComponent,
        data: {
          ...API_URLS,
          listURL: URLS.testJourneyList,
          isMock : true,
          identity: RouteURLIdentity.AI_GENERATED_JOURNEY,
          detailPath: '/customer-journey/ai-journey/detail/'
        }
      },
      { path: '', redirectTo: 'ai-journey-listing', pathMatch: 'full' }
    ]
  }
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AIGeneratedJourneyRoutingModule { }
