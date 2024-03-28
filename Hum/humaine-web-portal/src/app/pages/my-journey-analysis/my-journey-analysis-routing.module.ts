import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateJourneyComponent } from '../../core/_component/journey/create-journey/create-journey.component';
import { ListJourneyComponent } from '../../core/_component/journey/list-journey/list-journey.component';
import { ViewPersonaComponent } from '../../core/_component/view-Persona/view-Persona.component';
import { URLS } from '../../core/_constant/api.config';
import { RouteURLIdentity } from '../../core/_constant/app-constant';
import { MyJourneyAnalysisComponent } from './my-journey-analysis.component';


const API_URLS = {
  apiURL: URLS.testJourneyElement,
  userURL: URLS.testJourneyGroups,
  detailURL: URLS.myJourneyDetail,
}


const routes: Routes = [
  {
    path: '',
    component: MyJourneyAnalysisComponent,
    data: {
      title: ''
    },
    children: [
      {
        path: 'journey-analysis-listing',
        component: ListJourneyComponent,
        data: {
          listURL: URLS.myJourneyList,
          deleteURL: URLS.deleteMyJourney,
          deleteMultipleJourneyAPIURL: URLS.deleteMultipleMyJourney,
          isView: true,
          identity: RouteURLIdentity.JOURNEY_ANAYSIS,
          isMock: false,
          breadcrumb: 'My Journey',
          detailPath: '/customer-journey/my-journey-analysis/detail/'
        }
      },
      {
        path: 'viewPersona/:id',
        component: ViewPersonaComponent,
        data: {
          isMockData: false,
          allowEditGroupName: false,
          apiURL: URLS.myUserViewPersona,
          breadcrumb: 'My User Group',
          title: 'My User Group'
        }
      },
      {
        path: 'detail/:id',
        component: CreateJourneyComponent,
        data: {
          ...API_URLS,
          isView: true,
          identity: RouteURLIdentity.JOURNEY_ANAYSIS,
          breadcrumb: 'My Journey',
          title: 'My Journey analysis',
          detailPath: '/customer-journey/my-journey-analysis/detail'
        }
      },
      { path: '', redirectTo: 'journey-analysis-listing', pathMatch: 'full' }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MyJourneyAnalysisRoutingModule { }
