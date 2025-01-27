import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { CounselComponent } from './pages/counsel/counsel.component';
import { RepaymentComponent } from './pages/repayment/repayment.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'counsel',
    component: CounselComponent,
  },
  {
    path: 'repayment',
    component: RepaymentComponent,
  },
];
