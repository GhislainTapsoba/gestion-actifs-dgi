import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import MaintenanceResolve from './route/maintenance-routing-resolve.service';

const maintenanceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/maintenance').then(m => m.Maintenance),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/maintenance-detail').then(m => m.MaintenanceDetail),
    resolve: {
      maintenance: MaintenanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/maintenance-update').then(m => m.MaintenanceUpdate),
    resolve: {
      maintenance: MaintenanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/maintenance-update').then(m => m.MaintenanceUpdate),
    resolve: {
      maintenance: MaintenanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default maintenanceRoute;
