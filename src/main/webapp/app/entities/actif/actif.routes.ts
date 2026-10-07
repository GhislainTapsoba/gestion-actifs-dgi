import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';
import { Authority } from 'app/shared/jhipster/constants';

import ActifResolve from './route/actif-routing-resolve.service';

const assetManagers = [Authority.ADMIN, Authority.TECHNICIEN, Authority.RESPONSABLE];

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
    path: 'en-maintenance',
    loadComponent: () =>
      import('./equipements-en-maintenance/equipements-en-maintenance.component').then(m => m.EquipementsEnMaintenanceComponent),
    data: { authorities: assetManagers },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'a-reformer',
    loadComponent: () => import('./equipements-a-reformer/equipements-a-reformer.component').then(m => m.EquipementsAReformerComponent),
    data: { authorities: assetManagers },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'non-affectes',
    loadComponent: () =>
      import('./equipements-non-affectes/equipements-non-affectes.component').then(m => m.EquipementsNonAffectesComponent),
    data: { authorities: assetManagers },
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
    data: { authorities: assetManagers },
    resolve: {
      actif: ActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/actif-update').then(m => m.ActifUpdate),
    data: { authorities: assetManagers },
    resolve: {
      actif: ActifResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default actifRoute;
