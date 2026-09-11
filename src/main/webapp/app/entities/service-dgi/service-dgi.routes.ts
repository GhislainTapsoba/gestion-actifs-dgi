import { Routes } from '@angular/router';

import { ASC } from 'app/config';
import { userRouteAccessService } from 'app/core/auth';

import ServiceDgiResolve from './route/service-dgi-routing-resolve.service';

const serviceDgiRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/service-dgi').then(m => m.ServiceDgi),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/service-dgi-detail').then(m => m.ServiceDgiDetail),
    resolve: {
      serviceDgi: ServiceDgiResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/service-dgi-update').then(m => m.ServiceDgiUpdate),
    resolve: {
      serviceDgi: ServiceDgiResolve,
    },
    canActivate: [userRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/service-dgi-update').then(m => m.ServiceDgiUpdate),
    resolve: {
      serviceDgi: ServiceDgiResolve,
    },
    canActivate: [userRouteAccessService],
  },
];

export default serviceDgiRoute;
