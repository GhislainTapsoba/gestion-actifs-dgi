import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TransfertResolve from './route/transfert-routing-resolve.service';

const transfertRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transfert').then(m => m.Transfert),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transfert-detail').then(m => m.TransfertDetail),
    resolve: {
      transfert: TransfertResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transfert-update').then(m => m.TransfertUpdate),
    resolve: {
      transfert: TransfertResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transfert-update').then(m => m.TransfertUpdate),
    resolve: {
      transfert: TransfertResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default transfertRoute;
