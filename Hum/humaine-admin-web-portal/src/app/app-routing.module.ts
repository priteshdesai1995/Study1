import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { DefaultLayoutComponent } from './containers/default-layout/default-layout.component';
import { HomeComponent } from './pages/home/home.component';
import { CustomersComponent } from './pages/customers/customers.component';
import { AuthGuard } from './_core/_guard/auth.guard';

const appRoutes: Routes = [
  {
    path: 'login', 
    loadChildren: () => import('../app/pages/login/login.module').then(m => m.LoginModule),
  },
  {
    path: 'forgotPassword',
    loadChildren: () => import('../app/pages/forgot-password/forgot-password.module').then(m => m.ForgotPasswordModule),
  },
  {
    path: '',
    component: DefaultLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'home',
        loadChildren: () => import('../app/pages/home/home.module').then(m => m.HomeModule),
        data: {
          breadcrumb: 'Home',
          title:'Home'
        },
        canActivate: [AuthGuard]
      },
      {
        path: 'dashboard',
        component: HomeComponent,
        data: {
          breadcrumb: 'Home',
          title:'Home'
        },
        canActivateChild: [AuthGuard],
      },
      {
        path: 'customers',
        loadChildren: () => import('../app/pages/customers/customers.module').then(m => m.CustomersModule),
        data: {
          breadcrumb: 'Customers',
          title:'Customers'
        },
      },
      { path: '', redirectTo: 'home', pathMatch: 'full' },
    ]}
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(appRoutes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
