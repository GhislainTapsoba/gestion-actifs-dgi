import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import BordereauResolve from './route/bordereau-routing-resolve.service';

const bordereauRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bordereau').then(m => m.Bordereau),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bordereau-detail').then(m => m.BordereauDetail),
    resolve: {
      bordereau: BordereauResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bordereau-update').then(m => m.BordereauUpdate),
    resolve: {
      bordereau: BordereauResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bordereau-update').then(m => m.BordereauUpdate),
    resolve: {
      bordereau: BordereauResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default bordereauRoute;
