import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import FournisseurResolve from './route/fournisseur-routing-resolve.service';

const fournisseurRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/fournisseur').then(m => m.Fournisseur),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/fournisseur-detail').then(m => m.FournisseurDetail),
    resolve: {
      fournisseur: FournisseurResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/fournisseur-update').then(m => m.FournisseurUpdate),
    resolve: {
      fournisseur: FournisseurResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/fournisseur-update').then(m => m.FournisseurUpdate),
    resolve: {
      fournisseur: FournisseurResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default fournisseurRoute;
