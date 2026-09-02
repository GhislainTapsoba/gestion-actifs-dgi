import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ContratResolve from './route/contrat-routing-resolve.service';

const contratRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/contrat').then(m => m.Contrat),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/contrat-detail').then(m => m.ContratDetail),
    resolve: {
      contrat: ContratResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/contrat-update').then(m => m.ContratUpdate),
    resolve: {
      contrat: ContratResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/contrat-update').then(m => m.ContratUpdate),
    resolve: {
      contrat: ContratResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default contratRoute;
