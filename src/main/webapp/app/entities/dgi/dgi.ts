import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { HttpClient } from '@angular/common/http';
import { forkJoin } from 'rxjs';

interface DgiModule {
  key: string;
  label: string;
  description: string;
  icon: string;
  accent: 'blue' | 'green' | 'orange' | 'purple' | 'red';
  route: string;
  count: number | null;
}

@Component({
  selector: 'jhi-dgi-dashboard',
  standalone: true,
  templateUrl: './dgi.html',
  styleUrl: './dgi.scss',
  imports: [RouterLink, FontAwesomeModule],
})
export class DgiDashboard implements OnInit {
  private readonly http = inject(HttpClient);

  readonly modules = signal<DgiModule[]>([
    {
      key: 'categorie',
      label: 'Catégories',
      description: 'Classification du parc matériel',
      icon: 'tags',
      accent: 'blue',
      route: '/categorie-materiel',
      count: null,
    },
    {
      key: 'agent',
      label: 'Agents',
      description: 'Agents DGI et leurs affectations',
      icon: 'user-tie',
      accent: 'green',
      route: '/agent',
      count: null,
    },
    {
      key: 'service',
      label: 'Services DGI',
      description: 'Structures et directions',
      icon: 'building',
      accent: 'orange',
      route: '/service-dgi',
      count: null,
    },
    {
      key: 'panne',
      label: 'Pannes',
      description: 'Anomalies et incidents signalés',
      icon: 'exclamation-triangle',
      accent: 'red',
      route: '/panne',
      count: null,
    },
    {
      key: 'intervention',
      label: 'Interventions',
      description: 'Interventions techniques',
      icon: 'wrench',
      accent: 'blue',
      route: '/intervention',
      count: null,
    },
    {
      key: 'planning',
      label: 'Plannings',
      description: 'Planification des maintenances',
      icon: 'calendar-alt',
      accent: 'purple',
      route: '/planning-maintenance',
      count: null,
    },
    {
      key: 'recensement',
      label: 'Recensements',
      description: "Campagnes d'inventaire physique",
      icon: 'clipboard-list',
      accent: 'green',
      route: '/recensement',
      count: null,
    },
    {
      key: 'bordereau',
      label: 'Bordereaux',
      description: 'Documents justificatifs',
      icon: 'file-invoice',
      accent: 'orange',
      route: '/bordereau',
      count: null,
    },
    {
      key: 'inventaire',
      label: 'Inventaires',
      description: "Imports de fichiers d'inventaire",
      icon: 'database',
      accent: 'purple',
      route: '/inventaire',
      count: null,
    },
    {
      key: 'historique',
      label: 'Historique',
      description: 'Journal de traçabilité des actions',
      icon: 'history',
      accent: 'red',
      route: '/historique-action',
      count: null,
    },
  ]);

  ngOnInit(): void {
    forkJoin({
      categories: this.http.get<number>('/api/categorie-materiels/count'),
      agents: this.http.get<number>('/api/agents/count'),
      services: this.http.get<number>('/api/service-dgis/count'),
      pannes: this.http.get<number>('/api/pannes/count'),
      interventions: this.http.get<number>('/api/interventions/count'),
      plannings: this.http.get<number>('/api/planning-maintenances/count'),
      recensements: this.http.get<number>('/api/recensements/count'),
      bordereaux: this.http.get<number>('/api/bordereaux/count'),
      inventaires: this.http.get<number>('/api/inventaires/count'),
      historiques: this.http.get<number>('/api/historique-actions/count'),
    }).subscribe({
      next: data => {
        const counts: Record<string, number> = {
          categorie: data.categories,
          agent: data.agents,
          service: data.services,
          panne: data.pannes,
          intervention: data.interventions,
          planning: data.plannings,
          recensement: data.recensements,
          bordereau: data.bordereaux,
          inventaire: data.inventaires,
          historique: data.historiques,
        };
        this.modules.update(mods => mods.map(m => ({ ...m, count: counts[m.key] ?? null })));
      },
      error: () => {
        /* comptes restent null */
      },
    });
  }
}
