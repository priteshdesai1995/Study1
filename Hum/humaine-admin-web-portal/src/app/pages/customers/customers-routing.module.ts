import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers.component';
import { URLS } from 'src/app/_constant/api.config';
import { RouteURLIdentity } from 'src/app/_constant/app.constant';
import { ListCustomerComponent } from 'src/app/_core/_component/customer/list-customer/list-customer/list-customer.component';
import { EditCustomerComponent } from 'src/app/_core/_component/customer/edit-customer/edit-customer.component';


const API_URLS = {
  createURL: URLS.customerCreate,
  editURL: URLS.customerEdit,
  detailURL: URLS.customerDetail
}

const routes: Routes = [
  {
    path: '',
    component: CustomersComponent,
    data: {
      title: ''
    },
    children: [
      {
        path: 'customers-listing',
        component: ListCustomerComponent,
        data: {
          listURL: URLS.customersList,
          deleteURL: URLS.deleteCustomer,
          deleteMultipleJourneyAPIURL: URLS.deleteMultipleCustomers,
          title: '',
          breadcrumb:'',
          identity: RouteURLIdentity.CUSTOMERS,
          detailPath: '/customers/detail/',
          isMock: false
        }
      },
      {
        path: 'detail/:id',
        component: EditCustomerComponent,
        data: {
          ...API_URLS,
          identity: RouteURLIdentity.CUSTOMERS,
          isView: true,
          breadcrumb: 'View Customer',
          title: 'View Customer',
          editPageURL: 'customers/edit/',
        }
      },
      {
        path: 'edit/:id',
        component: EditCustomerComponent,
        data: {
          isEdit: true,
          identity: RouteURLIdentity.CUSTOMERS,
          viewPageURL: 'customers/detail/',
          breadcrumb: 'Edit Customer',
          title: 'Edit Customer',
          // listingRoute : '/customer-journey/test-new-journey/test-journey-listing'
        }
      },
      { path: '', redirectTo: 'customers-listing', pathMatch: 'full' }
    ]
  }
];
@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
