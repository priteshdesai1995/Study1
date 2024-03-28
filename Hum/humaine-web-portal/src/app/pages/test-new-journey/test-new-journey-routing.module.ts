import { MenuLinks } from './../../core/_enums/MenuLinks';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CreateJourneyComponent } from '../../core/_component/journey/create-journey/create-journey.component';
import { ListJourneyComponent } from '../../core/_component/journey/list-journey/list-journey.component';
import { URLS } from '../../core/_constant/api.config';
import { RouteURLIdentity } from '../../core/_constant/app-constant';
import { TestNewJourneyComponent } from './test-new-journey.component';
import { ViewPersonaComponent } from '../../core/_component/view-Persona/view-Persona.component';

const API_URLS = {
  apiURL: URLS.testJourneyElement,
  userURL: URLS.testJourneyGroups,
  createURL: URLS.testJourneyCreate,
  editURL: URLS.updateTestNewJourney,
  detailURL: URLS.testJourneyDetail
}

const routes: Routes = [
  {
    path: '',
    component: TestNewJourneyComponent,
    data: {
      title: ''
    },
    children: [
      {
        path: 'create-test-journey',
        component: CreateJourneyComponent,
        data: {
          ...API_URLS,
          identity: RouteURLIdentity.TEST_JOURNEY,
          breadcrumb: 'Test New Journey',
          isCreate: true,
          menuCountKey: MenuLinks.TEST_NEW_JOURNEY,
          listingRoute : '/customer-journey/test-new-journey/test-journey-listing'
        }
      },
      {
        path: 'detail/:id',
        component: CreateJourneyComponent,
        data: {
          ...API_URLS,
          identity: RouteURLIdentity.TEST_JOURNEY,
          isView: true,
          breadcrumb: 'Test New Journey',
          title: 'Test New Journey',
          listingRoute : '/customer-journey/test-new-journey/test-journey-listing',
        }
      },
      {
        path: 'edit/:id',
        component: CreateJourneyComponent,
        data: {
          ...API_URLS,
          isEdit: true,
          identity: RouteURLIdentity.TEST_JOURNEY,
          viewPageURL: 'customer-journey/test-new-journey/detail',
          breadcrumb: 'Test New Journey',
          title: 'Test New Journey',
          listingRoute : '/customer-journey/test-new-journey/test-journey-listing'
        }
      },
      {
        path: 'test-journey-listing',
        component: ListJourneyComponent,
        data: {
          listURL: URLS.testJourneyList,
          deleteURL: URLS.deeteTestNewJourney,
          deleteMultipleJourneyAPIURL: URLS.deleteMultipleTestJourney,
          title: '',
          breadcrumb:'Saved Journeys',
          identity: RouteURLIdentity.TEST_JOURNEY,
          detailPath: '/customer-journey/test-new-journey/detail/'
        }
      },
      {
        path: 'viewPersona/:id',
        component: ViewPersonaComponent,
        data: {
          editGroupAPI:URLS.editGroupNamePersona,
          allowEditGroupName :true,
          apiURL: URLS.manualViewPersona,
          breadcrumb: 'User Group',
          title: 'User Group'
        }
      },    
      { path: '', redirectTo: 'create-test-journey', pathMatch: 'full' }
    ]
  }
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestNewJourneyRoutingModule { }
