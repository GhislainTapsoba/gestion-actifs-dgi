import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import CategorieMaterielResolve from './route/categorie-materiel-routing-resolve.service';

const categorieMaterielRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/categorie-materiel').then(m => m.CategorieMateriel),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/categorie-materiel-detail').then(m => m.CategorieMaterielDetail),
    resolve: {
      categorieMateriel: CategorieMaterielResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/categorie-materiel-update').then(m => m.CategorieMaterielUpdate),
    resolve: {
      categorieMateriel: CategorieMaterielResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/categorie-materiel-update').then(m => m.CategorieMaterielUpdate),
    resolve: {
      categorieMateriel: CategorieMaterielResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default categorieMaterielRoute;
