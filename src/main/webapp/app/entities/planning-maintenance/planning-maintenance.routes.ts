import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PlanningMaintenanceResolve from './route/planning-maintenance-routing-resolve.service';

const planningMaintenanceRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/planning-maintenance').then(m => m.PlanningMaintenance),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/planning-maintenance-detail').then(m => m.PlanningMaintenanceDetail),
    resolve: {
      planningMaintenance: PlanningMaintenanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/planning-maintenance-update').then(m => m.PlanningMaintenanceUpdate),
    resolve: {
      planningMaintenance: PlanningMaintenanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/planning-maintenance-update').then(m => m.PlanningMaintenanceUpdate),
    resolve: {
      planningMaintenance: PlanningMaintenanceResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default planningMaintenanceRoute;
