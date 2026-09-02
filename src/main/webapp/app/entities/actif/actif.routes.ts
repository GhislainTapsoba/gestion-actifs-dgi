import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ActifResolve from './route/actif-routing-resolve.service';

const actifRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/actif').then(m => m.Actif),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/actif-detail').then(m => m.ActifDetail),
    resolve: {
      actif: ActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/actif-update').then(m => m.ActifUpdate),
    resolve: {
      actif: ActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/actif-update').then(m => m.ActifUpdate),
    resolve: {
      actif: ActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default actifRoute;
