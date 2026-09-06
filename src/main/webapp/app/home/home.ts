import { Component, inject, signal, computed, effect } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AccountService } from 'app/core/auth';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { forkJoin } from 'rxjs';

interface KpiCard {
  label: string;
  value: number | null;
  icon: string;
  color: 'blue' | 'green' | 'orange' | 'red' | 'purple';
  routerLink: string;
  trend?: string;
}

interface ChartBar {
  label: string;
  value: number;
  max: number;
  color: string;
}

@Component({
  selector: 'jhi-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [RouterLink, FontAwesomeModule],
})
export default class Home {
  public readonly account = inject(AccountService).account;
  private readonly router = inject(Router);
  private readonly http = inject(HttpClient);

  kpis = signal<KpiCard[]>([]);
  chartBars = signal<ChartBar[]>([]);
  loading = signal(false);
  private loaded = false;

  readonly isAgentOnly = computed(() => {
    const a = this.account()?.authorities ?? [];
    return a.includes('ROLE_AGENT') && !a.includes('ROLE_ADMIN') && !a.includes('ROLE_TECHNICIEN') && !a.includes('ROLE_RESPONSABLE');
  });
  readonly isAdmin = computed(() => this.account()?.authorities?.includes('ROLE_ADMIN') ?? false);
  readonly isResponsable = computed(() => this.account()?.authorities?.includes('ROLE_RESPONSABLE') ?? false);

  // Accès nommés pour les alertes (uniquement utilisés dans la vue non-agent)
  readonly kpiTransferts = computed(() => this.kpis().find(k => k.label === 'Transferts en attente') ?? null);
  readonly kpiEnMaintenance = computed(() => this.kpis().find(k => k.label === 'En maintenance') ?? null);
  readonly kpiInterventions = computed(() => this.kpis().find(k => k.label === 'Interventions ouvertes') ?? null);

  readonly greeting = computed(() => {
    const h = new Date().getHours();
    if (h < 12) return 'Bonjour';
    if (h < 18) return 'Bon après-midi';
    return 'Bonsoir';
  });

  readonly availabilityPercent = computed(() => {
    const service = this.chartBars().find(bar => bar.label === 'En service')?.value ?? 0;
    const total = this.chartBars().reduce((sum, bar) => sum + bar.value, 0) || 0;
    if (!total) return 0;
    return Math.round((service / total) * 100);
  });

  readonly maintenancePercent = computed(() => {
    const maintenance = this.kpis().find(kpi => kpi.label === 'En maintenance')?.value ?? 0;
    const total = this.kpis().find(kpi => kpi.label === 'Total des actifs')?.value ?? 0;
    if (!total) return 0;
    return Math.round((Number(maintenance) / Number(total)) * 100);
  });

  readonly pendingTransfers = computed(() => this.kpis().find(kpi => kpi.label === 'Transferts en attente')?.value ?? 0);

  readonly today = new Date().toLocaleDateString('fr-FR', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });

  constructor() {
    effect(() => {
      if (this.account() && !this.loaded) {
        this.loadDashboard();
      }
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  private loadDashboard(): void {
    this.loaded = true;
    this.loading.set(true);

    forkJoin({
      totalActifs: this.http.get<number>('/api/actifs/count'),
      enMaintenance: this.http.get<number>('/api/actifs/count', { params: new HttpParams().set('etat.equals', 'EN_MAINTENANCE') }),
      transfertsAttente: this.http.get<number>('/api/transferts/count', { params: new HttpParams().set('statut.equals', 'EN_ATTENTE') }),
      maintenancesOuvertes: this.http.get<number>('/api/maintenances/count', {
        params: new HttpParams().set('statut.in', 'OUVERTE,EN_COURS'),
      }),
      actifsDispo: this.http.get<number>('/api/actifs/count', { params: new HttpParams().set('etat.equals', 'EN_SERVICE') }),
    }).subscribe({
      next: data => {
        if (this.isAgentOnly()) {
          this.kpis.set([
            { label: 'Mes actifs affectés', value: data.totalActifs, icon: 'desktop', color: 'blue', routerLink: '/actif' },
            {
              label: 'Mes demandes en attente',
              value: data.transfertsAttente,
              icon: 'exchange-alt',
              color: 'orange',
              routerLink: '/transfert',
            },
            { label: 'Mes signalements', value: data.maintenancesOuvertes, icon: 'tools', color: 'red', routerLink: '/maintenance' },
          ]);
        } else {
          this.kpis.set([
            {
              label: 'Total des actifs',
              value: data.totalActifs,
              icon: 'desktop',
              color: 'blue',
              routerLink: '/actif',
              trend: 'Parc complet',
            },
            {
              label: 'Actifs en service',
              value: data.actifsDispo,
              icon: 'check-circle',
              color: 'green',
              routerLink: '/actif',
              trend: 'Disponibles',
            },
            {
              label: 'En maintenance',
              value: data.enMaintenance,
              icon: 'tools',
              color: 'orange',
              routerLink: '/actif',
              trend: 'Indisponibles',
            },
            {
              label: 'Transferts en attente',
              value: data.transfertsAttente,
              icon: 'exchange-alt',
              color: 'red',
              routerLink: '/transfert',
              trend: 'À valider',
            },
            {
              label: 'Interventions ouvertes',
              value: data.maintenancesOuvertes,
              icon: 'wrench',
              color: 'purple',
              routerLink: '/maintenance',
              trend: 'En cours',
            },
          ]);

          const total = data.totalActifs || 1;
          this.chartBars.set([
            { label: 'En service', value: data.actifsDispo, max: total, color: '#22c55e' },
            { label: 'En maintenance', value: data.enMaintenance, max: total, color: '#f59e0b' },
            { label: 'Autres', value: Math.max(0, total - data.actifsDispo - data.enMaintenance), max: total, color: '#94a3b8' },
          ]);
        }
        this.loading.set(false);
      },
      error: () => {
        this.loaded = false;
        this.loading.set(false);
      },
    });
  }

  barWidth(bar: ChartBar): string {
    if (!bar.max) return '0%';
    return Math.round((bar.value / bar.max) * 100) + '%';
  }

  barPercent(bar: ChartBar): number {
    if (!bar.max) return 0;
    return Math.round((bar.value / bar.max) * 100);
  }
}
