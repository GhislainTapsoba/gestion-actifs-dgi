import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import PanneResolve from './route/panne-routing-resolve.service';

const panneRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/panne').then(m => m.Panne),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/panne-detail').then(m => m.PanneDetail),
    resolve: {
      panne: PanneResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/panne-update').then(m => m.PanneUpdate),
    resolve: {
      panne: PanneResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/panne-update').then(m => m.PanneUpdate),
    resolve: {
      panne: PanneResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default panneRoute;
