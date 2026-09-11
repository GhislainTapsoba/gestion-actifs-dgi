import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import InventaireResolve from './route/inventaire-routing-resolve.service';

const inventaireRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inventaire').then(m => m.Inventaire),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inventaire-detail').then(m => m.InventaireDetail),
    resolve: {
      inventaire: InventaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inventaire-update').then(m => m.InventaireUpdate),
    resolve: {
      inventaire: InventaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inventaire-update').then(m => m.InventaireUpdate),
    resolve: {
      inventaire: InventaireResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default inventaireRoute;
