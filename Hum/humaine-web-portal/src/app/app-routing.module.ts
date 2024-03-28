import { LoginGuard } from './core/_guard/login.guard';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { DefaultLayoutComponent } from './containers/default-layout/default-layout.component';
import { AuthGuard } from './core/_guard/auth.guard';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { URLS } from './core/_constant/api.config';
import { RouteURLIdentity } from './core/_constant/app-constant';
import { HomeComponent } from './pages/home/home.component';
import { UxLayoutModule } from './pages/ux-layout/ux-layout.module';

const routes: Routes = [
  {
    path: 'registration',
    loadChildren: () => import('../app/pages/user-registration/user-registration.module').then(m => m.UserRegistrationModule),
  },
  {
    path: 'login',
    loadChildren: () => import('../app/pages/login/login.module').then(m => m.LoginModule),
    canActivate: [LoginGuard]
  },
  {
    path: 'forgetPassword',
    loadChildren: () => import('../app/pages/forget-password/forget-password.module').then(m => m.ForgetPasswordModule),
    canActivate: [LoginGuard]
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
        path: 'live',
        data: {
          breadcrumb: 'Live',
          title:'Live'
        },
        loadChildren: () => import('./pages/live/live.module').then(mod => mod.LiveModule)
      },
      {
        path: 'customer-journey/create-user-groups',
        data: {
          breadcrumb: 'Create User Groups'
        },
        loadChildren: () => import('./pages/create-user-group/create-user-group.module').then(mod => mod.CreateUserGroupModule)
      },
      {
        path: 'customer-journey/my-user-groups',
        data: {
          breadcrumb: 'My User Groups'
        },
        loadChildren: () => import('./pages/view-user-group/view-user-group.module').then(mod => mod.ViewUserGroupModule)
      },
      {
        path: 'customer-journey/ai-generated-user-groups',
        data: {
          breadcrumb: 'AI Generated User Groups'
        },
        loadChildren: () => import('./pages/ai-generated-user-group/ai-generated-user-group.module').then(mod => mod.AIGeneratedUserGroupModule)
      },
      {
        path: 'customer-journey/test-new-journey',
        data: {
        },
        loadChildren: () => import('./pages/test-new-journey/test-new-journey.module').then(mod => mod.TestNewJourneyModule)
      },
      {
        path: 'customer-journey/my-journey-analysis',
        data: {
          breadcrumb: 'My Journey Analysis',
        },
        loadChildren: () => import('./pages/my-journey-analysis/my-journey-analysis.module').then(mod => mod.MyJourneyAnalysisModule)
      },
      {
        path: 'customer-journey/ai-journey',
        data: {
          breadcrumb: 'AI Generated Journeys',
          identity: RouteURLIdentity.AI_GENERATED_JOURNEY
        },
        loadChildren: () => import('./pages/ai-generated-journey/ai-generated-journey.module').then(mod => mod.AIGeneratedJourneyModule)
      },
      {
        path: 'ux-optimization/ux-analytics',
        data: {
          breadcrumb: 'UX Analytics',
        },
        loadChildren: () => import('./pages/ux-insight/ux-insight.module').then(mod => mod.UxInsightModule)
      },
      {
        path: 'ux-optimization/heatmaps',
        data: {
          breadcrumb: 'Heatmaps',
        },
        loadChildren: () => import('./pages/heatmap/heatmap.module').then(mod => mod.HeatmapModule)
      },
      {
        path: 'ux-optimization/ux-layout',
        data: {
          breadcrumb: 'UX Layout',
        },
        loadChildren: () => import('./pages/ux-layout/ux-layout.module').then(mod => UxLayoutModule)
      },
      {
        path: 'integration',
        data: {
          breadcrumb: 'Integrations',
        },
        loadChildren: () => import('./pages/setting/setting.module').then(mod => mod.SettingModule)
      },
      {
        path: 'product-intelligence/all-produt-matches',
        data: {
          breadcrumb: 'All Product Matches',
        },
        loadChildren: () => import('./pages/product-intelligence/product-intelligence.module').then(mod => mod.ProductIntelligenceModule)
      },
      
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes,{
    scrollPositionRestoration: 'enabled'
  })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
