$path = "src\main\webapp\app\home\home.ts"
$content = @'
import { Component, OnInit, inject, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { AccountService } from 'app/core/auth';
import { TranslateDirective } from 'app/shared/language';

interface DashboardCard {
  label: string;
  value: number | null;
  routerLink: string;
  accent: 'neutral' | 'warning';
}

@Component({
  selector: 'jhi-home',
  templateUrl: './home.html',
  styleUrl: './home.scss',
  imports: [TranslateDirective, RouterLink],
})
export default class Home implements OnInit {
  public readonly account = inject(AccountService).account;
  private readonly router = inject(Router);
  private readonly http = inject(HttpClient);

  isAgentOnly = false;
  cards = signal<DashboardCard[]>([]);

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnInit(): void {
    const authorities = this.account()?.authorities ?? [];
    this.isAgentOnly =
      authorities.includes('ROLE_AGENT') &&
      !authorities.includes('ROLE_ADMIN') &&
      !authorities.includes('ROLE_TECHNICIEN') &&
      !authorities.includes('ROLE_RESPONSABLE');

    if (this.account()) {
      this.loadStats();
    }
  }

  private loadStats(): void {
    const actifLabel = this.isAgentOnly ? 'Mes actifs' : 'Total des actifs';
    const transfertLabel = this.isAgentOnly ? 'Mes demandes en attente' : 'Transferts en attente de validation';
    const maintenanceLabel = this.isAgentOnly ? 'Mes signalements en cours' : 'Maintenances en cours';

    this.cards.set([
      { label: actifLabel, value: null, routerLink: '/actif', accent: 'neutral' },
      { label: 'Actifs en maintenance', value: null, routerLink: '/actif', accent: 'warning' },
      { label: transfertLabel, value: null, routerLink: '/transfert', accent: 'warning' },
      { label: maintenanceLabel, value: null, routerLink: '/maintenance', accent: 'neutral' },
    ]);

    this.http.get<number>('/api/actifs/count').subscribe(count => this.updateCard(0, count));

    this.http
      .get<number>('/api/actifs/count', { params: new HttpParams().set('etat.equals', 'EN_MAINTENANCE') })
      .subscribe(count => this.updateCard(1, count));

    this.http
      .get<number>('/api/transferts/count', { params: new HttpParams().set('statut.equals', 'EN_ATTENTE') })
      .subscribe(count => this.updateCard(2, count));

    this.http
      .get<number>('/api/maintenances/count', { params: new HttpParams().set('statut.in', 'OUVERTE,EN_COURS') })
      .subscribe(count => this.updateCard(3, count));
  }

  private updateCard(index: number, value: number): void {
    const updated = [...this.cards()];
    updated[index] = { ...updated[index], value };
    this.cards.set(updated);
  }
}
'@
Set-Content -Path $path -Value $content -Encoding UTF8
Write-Host "OK : home.ts remplace par le nouveau dashboard" -ForegroundColor Green
