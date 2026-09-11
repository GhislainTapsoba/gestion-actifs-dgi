import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import AffectationActifResolve from './route/affectation-actif-routing-resolve.service';

const affectationActifRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/affectation-actif').then(m => m.AffectationActif),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/affectation-actif-detail').then(m => m.AffectationActifDetail),
    resolve: {
      affectationActif: AffectationActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/affectation-actif-update').then(m => m.AffectationActifUpdate),
    resolve: {
      affectationActif: AffectationActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/affectation-actif-update').then(m => m.AffectationActifUpdate),
    resolve: {
      affectationActif: AffectationActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default affectationActifRoute;
