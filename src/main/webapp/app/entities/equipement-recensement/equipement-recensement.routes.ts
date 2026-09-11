import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import EquipementRecensementResolve from './route/equipement-recensement-routing-resolve.service';

const equipementRecensementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/equipement-recensement').then(m => m.EquipementRecensement),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/equipement-recensement-detail').then(m => m.EquipementRecensementDetail),
    resolve: {
      equipementRecensement: EquipementRecensementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/equipement-recensement-update').then(m => m.EquipementRecensementUpdate),
    resolve: {
      equipementRecensement: EquipementRecensementResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/equipement-recensement-update').then(m => m.EquipementRecensementUpdate),
    resolve: {
      equipementRecensement: EquipementRecensementResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default equipementRecensementRoute;
