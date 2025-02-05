import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CounselComponent } from './pages/counsel/counsel.component';
import { RepaymentComponent } from './pages/repayment/repayment.component';
import { ApplicationComponent } from './pages/application/application.component';
import { AdminHomeComponent } from './pages/admin/admin-home/admin-home.component';
import { AdminApplicationComponent } from './pages/admin/admin-application/admin-application.component';
import { authGuard } from './auth/auth.guard';
import { AdminCounselComponent } from './pages/admin/admin-counsel/admin-counsel.component';
import { AdminApplicationDetailComponent } from './pages/admin/admin-application-detail/admin-application-detail.component';
import { AdminCounselDetailComponent } from './pages/admin/admin-counsel-detail/admin-counsel-detail.component';
import { AdminTermsComponent } from './pages/admin/admin-terms/admin-terms.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [authGuard],
    data: { expectedRole: 'CUSTOMER' },
  },
  {
    path: 'counsel',
    component: CounselComponent,
    canActivate: [authGuard],
    data: { expectedRole: 'CUSTOMER' },
  },
  {
    path: 'repayment',
    component: RepaymentComponent,
    canActivate: [authGuard],
    data: { expectedRole: 'CUSTOMER' },
  },
  {
    path: 'application',
    component: ApplicationComponent,
    canActivate: [authGuard],
    data: { expectedRole: 'CUSTOMER' },
  },
  {
    path: 'admin-application',
    canActivate: [authGuard],
    data: { expectedRole: 'MANAGER' },
    component: AdminApplicationComponent,
  },
  {
    path: 'admin-counsel',
    canActivate: [authGuard],
    data: { expectedRole: 'MANAGER' },
    component: AdminCounselComponent,
  },
  {
    path: 'admin-home',
    component: AdminHomeComponent,
    canActivate: [authGuard],
    data: { expectedRole: 'MANAGER' },
  },
  {
    path: 'admin-application/:applicationId',
    canActivate: [authGuard],
    data: { expectedRole: 'MANAGER' },
    component: AdminApplicationDetailComponent,
  },
  {
    path: 'admin-counsel/:counselId',
    canActivate: [authGuard],
    data: { expectedRole: 'MANAGER' },
    component: AdminCounselDetailComponent,
  },
  {
    path: 'admin-terms',
    component: AdminTermsComponent,
    canActivate: [authGuard],
    data: { expectedRole: 'MANAGER' },
  },
];
