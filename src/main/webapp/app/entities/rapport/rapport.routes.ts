import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import RapportResolve from './route/rapport-routing-resolve.service';

const rapportRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/rapport').then(m => m.Rapport),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/rapport-detail').then(m => m.RapportDetail),
    resolve: {
      rapport: RapportResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/rapport-update').then(m => m.RapportUpdate),
    resolve: {
      rapport: RapportResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/rapport-update').then(m => m.RapportUpdate),
    resolve: {
      rapport: RapportResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default rapportRoute;
