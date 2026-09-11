import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import TransfertActifResolve from './route/transfert-actif-routing-resolve.service';

const transfertActifRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/transfert-actif').then(m => m.TransfertActif),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/transfert-actif-detail').then(m => m.TransfertActifDetail),
    resolve: {
      transfertActif: TransfertActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/transfert-actif-update').then(m => m.TransfertActifUpdate),
    resolve: {
      transfertActif: TransfertActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/transfert-actif-update').then(m => m.TransfertActifUpdate),
    resolve: {
      transfertActif: TransfertActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default transfertActifRoute;
