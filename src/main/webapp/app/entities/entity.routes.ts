import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'gestionActifsDgiApp.adminAuthority.home.title',
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'actif',
    title: 'gestionActifsDgiApp.actif.home.title',
    loadChildren: () => import('./actif/actif.routes'),
  },
  {
    path: 'affectation',
    title: 'gestionActifsDgiApp.affectation.home.title',
    loadChildren: () => import('./affectation/affectation.routes'),
  },
  {
    path: 'transfert',
    title: 'gestionActifsDgiApp.transfert.home.title',
    loadChildren: () => import('./transfert/transfert.routes'),
  },
  {
    path: 'maintenance',
    title: 'gestionActifsDgiApp.maintenance.home.title',
    loadChildren: () => import('./maintenance/maintenance.routes'),
  },
  {
    path: 'fournisseur',
    title: 'gestionActifsDgiApp.fournisseur.home.title',
    loadChildren: () => import('./fournisseur/fournisseur.routes'),
  },
  {
    path: 'contrat',
    title: 'gestionActifsDgiApp.contrat.home.title',
    loadChildren: () => import('./contrat/contrat.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
];

export default routes;
