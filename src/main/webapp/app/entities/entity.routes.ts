import { Routes } from '@angular/router';
import { userRouteAccessService } from 'app/core/auth';
import { Authority } from 'app/shared/jhipster/constants';

const staffAuthorities = [Authority.ADMIN, Authority.TECHNICIEN, Authority.RESPONSABLE];
const inventoryAuthorities = [...staffAuthorities, Authority.AGENT];
const managerAuthorities = [Authority.ADMIN, Authority.RESPONSABLE];
const incidentAuthorities = [...inventoryAuthorities];

const routes: Routes = [
  {
    path: 'user-management',
    title: 'userManagement.home.title',
    data: { authorities: [Authority.ADMIN] },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'authority',
    title: 'gestionActifsDgiApp.adminAuthority.home.title',
    data: { authorities: [Authority.ADMIN] },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'actif',
    title: 'gestionActifsDgiApp.actif.home.title',
    data: { authorities: inventoryAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./actif/actif.routes'),
  },
  {
    path: 'affectation',
    title: 'gestionActifsDgiApp.affectation.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./affectation/affectation.routes'),
  },
  {
    path: 'transfert',
    title: 'gestionActifsDgiApp.transfert.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./transfert/transfert.routes'),
  },
  {
    path: 'dgi',
    title: 'gestionActifsDgiApp.dgi.home.title',
    data: { authorities: managerAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./dgi/dgi.routes'),
  },
  {
    path: 'maintenance',
    title: 'gestionActifsDgiApp.maintenance.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./maintenance/maintenance.routes'),
  },
  {
    path: 'fournisseur',
    title: 'gestionActifsDgiApp.fournisseur.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./fournisseur/fournisseur.routes'),
  },
  {
    path: 'contrat',
    title: 'gestionActifsDgiApp.contrat.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./contrat/contrat.routes'),
  },
  {
    path: 'categorie-materiel',
    title: 'gestionActifsDgiApp.categorieMateriel.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./categorie-materiel/categorie-materiel.routes'),
  },
  {
    path: 'service-dgi',
    title: 'gestionActifsDgiApp.serviceDgi.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./service-dgi/service-dgi.routes'),
  },
  {
    path: 'agent',
    title: 'gestionActifsDgiApp.agent.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./agent/agent.routes'),
  },
  {
    path: 'affectation-actif',
    title: 'gestionActifsDgiApp.affectationActif.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./affectation-actif/affectation-actif.routes'),
  },
  {
    path: 'transfert-actif',
    title: 'gestionActifsDgiApp.transfertActif.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./transfert-actif/transfert-actif.routes'),
  },
  {
    path: 'bordereau',
    title: 'gestionActifsDgiApp.bordereau.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./bordereau/bordereau.routes'),
  },
  {
    path: 'planning-maintenance',
    title: 'gestionActifsDgiApp.planningMaintenance.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./planning-maintenance/planning-maintenance.routes'),
  },
  {
    path: 'intervention',
    title: 'gestionActifsDgiApp.intervention.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./intervention/intervention.routes'),
  },
  {
    path: 'panne',
    title: 'gestionActifsDgiApp.panne.home.title',
    data: { authorities: incidentAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./panne/panne.routes'),
  },
  {
    path: 'recensement',
    title: 'gestionActifsDgiApp.recensement.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./recensement/recensement.routes'),
  },
  {
    path: 'equipement-recensement',
    title: 'gestionActifsDgiApp.equipementRecensement.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./equipement-recensement/equipement-recensement.routes'),
  },
  {
    path: 'inventaire',
    title: 'gestionActifsDgiApp.inventaire.home.title',
    data: { authorities: staffAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./inventaire/inventaire.routes'),
  },
  {
    path: 'historique-action',
    title: 'gestionActifsDgiApp.historiqueAction.home.title',
    data: { authorities: managerAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./historique-action/historique-action.routes'),
  },
  {
    path: 'rapport',
    title: 'gestionActifsDgiApp.rapport.home.title',
    data: { authorities: managerAuthorities },
    canActivate: [userRouteAccessService],
    loadChildren: () => import('./rapport/rapport.routes'),
  },
  // jhipster-needle-add-entity-route - JHipster will add entity modules routes here
  // Les routes suivantes seront générées automatiquement par : jhipster jdl gestion-actifs-dgi.jdl
  // categorie-materiel, service-dgi, agent, affectation-actif, transfert-actif,
  // bordereau, planning-maintenance, intervention, panne, recensement,
  // equipement-recensement, inventaire, historique-action
];

export default routes;
