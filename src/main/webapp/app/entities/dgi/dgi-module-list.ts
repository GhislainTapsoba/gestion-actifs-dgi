import { Component, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

export type DgiTableColumn = { key: string; label: string };
export type DgiTableDefinition = {
  key: string;
  title: string;
  subtitle: string;
  columns: DgiTableColumn[];
  rows: Record<string, string | number | null>[];
};

const catalog: Record<string, DgiTableDefinition> = {
  agent: {
    key: 'agent',
    title: 'Agents DGI',
    subtitle: 'Liste des agents affectés aux services',
    columns: [
      { key: 'nom', label: 'Nom' },
      { key: 'service', label: 'Service' },
      { key: 'telephone', label: 'Téléphone' },
      { key: 'statut', label: 'Statut' },
    ],
    rows: [
      { nom: 'Kouassi Yao', service: 'Direction', telephone: '01020304', statut: 'Actif' },
      { nom: 'Awa Diallo', service: 'Gestion du parc', telephone: '01020305', statut: 'Actif' },
      { nom: 'Moussa Diop', service: 'Maintenance', telephone: '01020306', statut: 'En mission' },
    ],
  },
  'service-dgi': {
    key: 'service-dgi',
    title: 'Services DGI',
    subtitle: 'Structure des services et unités de gestion',
    columns: [
      { key: 'libelle', label: 'Service' },
      { key: 'chef', label: 'Chef' },
      { key: 'effectif', label: 'Effectif' },
      { key: 'niveau', label: 'Niveau' },
    ],
    rows: [
      { libelle: 'Direction', chef: 'K. Tapsoba', effectif: 6, niveau: 'Direction' },
      { libelle: 'Gestion du parc', chef: 'A. Diop', effectif: 14, niveau: 'Unité' },
      { libelle: 'Maintenance', chef: 'F. Ndao', effectif: 9, niveau: 'Unité' },
    ],
  },
  categorie: {
    key: 'categorie',
    title: 'Catégories de matériel',
    subtitle: 'Classement standard du patrimoine informatique',
    columns: [
      { key: 'libelle', label: 'Catégorie' },
      { key: 'code', label: 'Code' },
      { key: 'description', label: 'Description' },
    ],
    rows: [
      { libelle: 'Ordinateurs', code: 'MAT-01', description: 'Postes de travail' },
      { libelle: 'Serveurs', code: 'MAT-02', description: 'Infrastructure de production' },
      { libelle: 'Réseaux', code: 'MAT-03', description: 'Switchs, routeurs et WIFI' },
    ],
  },
  inventaire: {
    key: 'inventaire',
    title: 'Inventaires',
    subtitle: 'Campagnes et relevés du parc informatique',
    columns: [
      { key: 'nom', label: 'Nom' },
      { key: 'date', label: 'Date' },
      { key: 'statut', label: 'Statut' },
      { key: 'responsable', label: 'Responsable' },
    ],
    rows: [
      { nom: 'Inventaire 2025 Q1', date: '2025-01-18', statut: 'Validé', responsable: 'A. Diop' },
      { nom: 'Inventaire 2025 Q2', date: '2025-04-21', statut: 'En cours', responsable: 'K. Yao' },
      { nom: 'Inventaire 2025 Q3', date: '2025-07-12', statut: 'Planifié', responsable: 'M. Camara' },
    ],
  },
  recensement: {
    key: 'recensement',
    title: 'Recensements',
    subtitle: 'Contrôles et vérifications périodiques',
    columns: [
      { key: 'obj', label: 'Objet' },
      { key: 'date', label: 'Date' },
      { key: 'resultat', label: 'Résultat' },
      { key: 'agent', label: 'Agent' },
    ],
    rows: [
      { obj: 'Contrôle du site Abidjan', date: '2025-08-10', resultat: 'OK', agent: 'M. Kouassi' },
      { obj: 'Vérification des stocks', date: '2025-08-17', resultat: 'À corriger', agent: 'A. Diallo' },
      { obj: 'Audit du réseau', date: '2025-08-24', resultat: 'OK', agent: 'R. Diop' },
    ],
  },
  intervention: {
    key: 'intervention',
    title: 'Interventions',
    subtitle: 'Suivi des maintenances et interventions techniques',
    columns: [
      { key: 'code', label: 'Code' },
      { key: 'actif', label: 'Actif' },
      { key: 'date', label: 'Date' },
      { key: 'statut', label: 'Statut' },
    ],
    rows: [
      { code: 'INT-001', actif: 'PC-028', date: '2025-09-01', statut: 'Ouverte' },
      { code: 'INT-004', actif: 'SRV-07', date: '2025-09-04', statut: 'En cours' },
      { code: 'INT-012', actif: 'NET-03', date: '2025-09-08', statut: 'Clôturée' },
    ],
  },
  panne: {
    key: 'panne',
    title: 'Pannes',
    subtitle: 'Incidents signalés sur les actifs',
    columns: [
      { key: 'code', label: 'Code' },
      { key: 'actif', label: 'Actif' },
      { key: 'nature', label: 'Nature' },
      { key: 'gravite', label: 'Gravité' },
    ],
    rows: [
      { code: 'PAN-018', actif: 'PC-028', nature: 'Écran défaillant', gravite: 'Moyenne' },
      { code: 'PAN-030', actif: 'SRV-07', nature: 'Serveur lent', gravite: 'Élevée' },
      { code: 'PAN-044', actif: 'NET-03', nature: 'Perte de connexion', gravite: 'Critique' },
    ],
  },
  bordereau: {
    key: 'bordereau',
    title: 'Bordereaux',
    subtitle: 'Documents adminitratifs liés aux mouvements',
    columns: [
      { key: 'numero', label: 'Numéro' },
      { key: 'type', label: 'Type' },
      { key: 'date', label: 'Date' },
      { key: 'etat', label: 'État' },
    ],
    rows: [
      { numero: 'BR-101', type: 'Affectation', date: '2025-09-02', etat: 'Validé' },
      { numero: 'BR-210', type: 'Transfert', date: '2025-09-05', etat: 'En attente' },
      { numero: 'BR-315', type: 'Mise en service', date: '2025-09-06', etat: 'Signé' },
    ],
  },
  historique: {
    key: 'historique',
    title: 'Historique des actions',
    subtitle: 'Journal d’audit des activités',
    columns: [
      { key: 'date', label: 'Date' },
      { key: 'action', label: 'Action' },
      { key: 'utilisateur', label: 'Utilisateur' },
      { key: 'objet', label: 'Objet' },
    ],
    rows: [
      { date: '2025-09-08', action: 'Création', utilisateur: 'admin', objet: 'PC-028' },
      { date: '2025-09-07', action: 'Mise à jour', utilisateur: 'A. Diallo', objet: 'Inventaire 2025 Q2' },
      { date: '2025-09-06', action: 'Validation', utilisateur: 'K. Yao', objet: 'BR-101' },
    ],
  },
};

@Component({
  selector: 'jhi-dgi-module-list',
  standalone: true,
  templateUrl: './dgi-module-list.html',
  styleUrl: './dgi-module-list.scss',
  imports: [RouterLink, FontAwesomeModule],
})
export class DgiModuleList {
  private readonly route = inject(ActivatedRoute);

  readonly moduleId = this.route.snapshot.paramMap.get('module') ?? 'agent';
  readonly definition = catalog[this.moduleId] ?? catalog.agent;
}
