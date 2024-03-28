import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ManageLocationComponent } from './manage-location.component';
import { ManageStateComponent } from './manage-state/manage-state.component';
import { StateAddEditComponent } from './manage-state/state-add-edit/state-add-edit.component';
import { ManageCityComponent } from './manage-city/manage-city.component';
import { CityAddEditComponent } from './manage-city/city-add-edit/city-add-edit.component';
import { ManageCountryComponent } from './manage-country/manage-country.component';
import { CountryAddEditComponent } from './manage-country/country-add-edit/country-add-edit.component';

const routes: Routes = [
  {
    path: '',
    component: ManageLocationComponent,
    data: {
      title: 'Manage Location',
    },
    children: [
      {
        path: '',
        redirectTo: 'country',
      },
      {
        path: 'country',
        component: ManageCountryComponent,
        data: {
          title: 'Manage Country',
          permission: 'COUNTRY_LIST',
        },
      },
      {
        path: 'country/add',
        component: CountryAddEditComponent,
        data: {
          title: 'Add Country',
          permission: 'COUNTRY_CREATE',
        },
      },
      {
        path: 'country/edit/:id',
        component: CountryAddEditComponent,
        data: {
          title: 'Edit Country',
          permission: 'COUNTRY_UPDATE',
        },
      },
      {
        path: 'state',
        component: ManageStateComponent,
        data: {
          title: 'Manage State',
          permission: 'STATE_LIST',
        },
      },
      {
        path: 'state/add',
        component: StateAddEditComponent,
        data: {
          title: 'Add State',
          permission: 'STATE_CREATE',
        },
      },
      {
        path: 'state/edit/:id',
        component: StateAddEditComponent,
        data: {
          title: 'Edit State',
          permission: 'STATE_UPDATE',
        },
      },
      {
        path: 'city',
        component: ManageCityComponent,
        data: {
          title: 'Manage City',
          permission: 'CITY_LIST',
        },
      },
      {
        path: 'city/add',
        component: CityAddEditComponent,
        data: {
          title: 'Add City',
          permission: 'CITY_CREATE',
        },
      },
      {
        path: 'city/edit/:id',
        component: CityAddEditComponent,
        data: {
          title: 'Edit City',
          permission: 'CITY_UPDATE',
        },
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ManageLocationRoutingModule {}
