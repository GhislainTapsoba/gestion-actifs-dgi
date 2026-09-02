import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import AffectationResolve from './route/affectation-routing-resolve.service';

const affectationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/affectation').then(m => m.Affectation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/affectation-detail').then(m => m.AffectationDetail),
    resolve: {
      affectation: AffectationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/affectation-update').then(m => m.AffectationUpdate),
    resolve: {
      affectation: AffectationResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/affectation-update').then(m => m.AffectationUpdate),
    resolve: {
      affectation: AffectationResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default affectationRoute;
