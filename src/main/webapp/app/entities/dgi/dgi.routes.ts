import { Routes } from '@angular/router';

const dgiRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./dgi').then(m => m.DgiDashboard),
  },
];

export default dgiRoutes;
