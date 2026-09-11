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
    path: 'dgi',
    title: 'gestionActifsDgiApp.dgi.home.title',
    loadChildren: () => import('./dgi/dgi.routes'),
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
  {
    path: 'categorie-materiel',
    title: 'gestionActifsDgiApp.categorieMateriel.home.title',
    loadChildren: () => import('./categorie-materiel/categorie-materiel.routes'),
  },
  {
    path: 'service-dgi',
    title: 'gestionActifsDgiApp.serviceDgi.home.title',
    loadChildren: () => import('./service-dgi/service-dgi.routes'),
  },
  {
    path: 'agent',
    title: 'gestionActifsDgiApp.agent.home.title',
    loadChildren: () => import('./agent/agent.routes'),
  },
  {
    path: 'affectation-actif',
    title: 'gestionActifsDgiApp.affectationActif.home.title',
    loadChildren: () => import('./affectation-actif/affectation-actif.routes'),
  },
  {
    path: 'transfert-actif',
    title: 'gestionActifsDgiApp.transfertActif.home.title',
    loadChildren: () => import('./transfert-actif/transfert-actif.routes'),
  },
  {
    path: 'bordereau',
    title: 'gestionActifsDgiApp.bordereau.home.title',
    loadChildren: () => import('./bordereau/bordereau.routes'),
  },
  {
    path: 'planning-maintenance',
    title: 'gestionActifsDgiApp.planningMaintenance.home.title',
    loadChildren: () => import('./planning-maintenance/planning-maintenance.routes'),
  },
  {
    path: 'intervention',
    title: 'gestionActifsDgiApp.intervention.home.title',
    loadChildren: () => import('./intervention/intervention.routes'),
  },
  {
    path: 'panne',
    title: 'gestionActifsDgiApp.panne.home.title',
    loadChildren: () => import('./panne/panne.routes'),
  },
  {
    path: 'recensement',
    title: 'gestionActifsDgiApp.recensement.home.title',
    loadChildren: () => import('./recensement/recensement.routes'),
  },
  {
    path: 'equipement-recensement',
    title: 'gestionActifsDgiApp.equipementRecensement.home.title',
    loadChildren: () => import('./equipement-recensement/equipement-recensement.routes'),
  },
  {
    path: 'inventaire',
    title: 'gestionActifsDgiApp.inventaire.home.title',
    loadChildren: () => import('./inventaire/inventaire.routes'),
  },
  {
    path: 'historique-action',
    title: 'gestionActifsDgiApp.historiqueAction.home.title',
    loadChildren: () => import('./historique-action/historique-action.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
  // Les routes suivantes seront générées automatiquement par : jhipster jdl gestion-actifs-dgi.jdl
  // categorie-materiel, service-dgi, agent, affectation-actif, transfert-actif,
  // bordereau, planning-maintenance, intervention, panne, recensement,
  // equipement-recensement, inventaire, historique-action
];

export default routes;
