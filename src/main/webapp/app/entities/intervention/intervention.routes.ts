import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InterventionResolve from './route/intervention-routing-resolve.service';

const interventionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/intervention').then(m => m.Intervention),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/intervention-detail').then(m => m.InterventionDetail),
    resolve: {
      intervention: InterventionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/intervention-update').then(m => m.InterventionUpdate),
    resolve: {
      intervention: InterventionResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/intervention-update').then(m => m.InterventionUpdate),
    resolve: {
      intervention: InterventionResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default interventionRoute;
