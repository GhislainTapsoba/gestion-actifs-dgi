import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import RecensementResolve from './route/recensement-routing-resolve.service';

const recensementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/recensement').then(m => m.Recensement),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/recensement-detail').then(m => m.RecensementDetail),
    resolve: {
      recensement: RecensementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/recensement-update').then(m => m.RecensementUpdate),
    resolve: {
      recensement: RecensementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/recensement-update').then(m => m.RecensementUpdate),
    resolve: {
      recensement: RecensementResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default recensementRoute;
