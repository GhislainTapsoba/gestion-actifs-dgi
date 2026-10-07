import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AccountService } from 'app/core/auth';
import { LoginService } from 'app/login/login.service';
import { SidebarStateService } from './sidebar-state.service';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  exact?: boolean;
  roles?: string[];
}

@Component({
  selector: 'jhi-sidebar',
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
  imports: [RouterLink, RouterLinkActive, FontAwesomeModule],
})
export default class Sidebar {
  readonly account = inject(AccountService).account;
  private readonly sidebarState = inject(SidebarStateService);
  readonly collapsed = this.sidebarState.collapsed;

  private readonly loginService = inject(LoginService);
  private readonly router = inject(Router);

  readonly mainItems: NavItem[] = [
    {
      label: 'Tableau de bord',
      icon: 'home',
      route: '/',
      exact: true,
      roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE', 'ROLE_AGENT'],
    },
    {
      label: 'Tous les actifs',
      icon: 'desktop',
      route: '/actif',
      roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE', 'ROLE_AGENT'],
    },
    {
      label: 'Imports inventaire',
      icon: 'file-upload',
      route: '/inventaire',
      roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'],
    },
    { label: 'Catégories', icon: 'tags', route: '/categorie-materiel', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Affectations', icon: 'users', route: '/affectation', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Transferts', icon: 'exchange-alt', route: '/transfert', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Maintenance', icon: 'tools', route: '/maintenance', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
  ];

  readonly incidentItems: NavItem[] = [
    {
      label: 'Pannes et signalements',
      icon: 'exclamation-triangle',
      route: '/panne',
      roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE', 'ROLE_AGENT'],
    },
    { label: 'Interventions', icon: 'wrench', route: '/intervention', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    {
      label: 'Plannings',
      icon: 'calendar-alt',
      route: '/planning-maintenance',
      roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'],
    },
    { label: 'Recensements', icon: 'clipboard-list', route: '/recensement', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
  ];

  readonly gestionItems: NavItem[] = [
    { label: 'Agents', icon: 'user-tie', route: '/agent', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Services DGI', icon: 'building', route: '/service-dgi', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Bordereaux', icon: 'file-invoice', route: '/bordereau', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Fournisseurs', icon: 'truck', route: '/fournisseur', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Contrats', icon: 'file-contract', route: '/contrat', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Historique', icon: 'history', route: '/historique-action', roles: ['ROLE_ADMIN', 'ROLE_RESPONSABLE'] },
    { label: 'Rapports', icon: 'file-alt', route: '/rapport', roles: ['ROLE_ADMIN', 'ROLE_RESPONSABLE'] },
  ];

  readonly adminItems: NavItem[] = [
    { label: 'Utilisateurs', icon: 'users-cog', route: '/user-management' },
    { label: 'Métriques', icon: 'tachometer-alt', route: '/admin/metrics' },
    { label: 'Logs', icon: 'tasks', route: '/admin/logs' },
  ];

  toggleCollapse(): void {
    this.sidebarState.toggle();
  }

  isVisible(item: NavItem): boolean {
    if (!item.roles) return true;
    const authorities = this.account()?.authorities ?? [];
    return item.roles.some(r => authorities.includes(r));
  }

  hasVisibleItems(items: NavItem[]): boolean {
    return items.some(item => this.isVisible(item));
  }

  getItemLabel(item: NavItem): string {
    if (item.route === '/actif' && this.isAgentOnly()) {
      return 'Mes équipements';
    }
    return item.label;
  }

  isAgentOnly(): boolean {
    const authorities = this.account()?.authorities ?? [];
    return (
      authorities.includes('ROLE_AGENT') &&
      !authorities.includes('ROLE_ADMIN') &&
      !authorities.includes('ROLE_TECHNICIEN') &&
      !authorities.includes('ROLE_RESPONSABLE')
    );
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['']);
  }

  get userInitials(): string {
    const login = this.account()?.login ?? '';
    return login.slice(0, 2).toUpperCase();
  }

  get roleLabel(): string {
    const authorities = this.account()?.authorities ?? [];
    if (authorities.includes('ROLE_ADMIN')) return 'Administrateur';
    if (authorities.includes('ROLE_TECHNICIEN')) return 'Technicien';
    if (authorities.includes('ROLE_RESPONSABLE')) return 'Responsable Service';
    if (authorities.includes('ROLE_AGENT')) return 'Agent';
    return '';
  }
}
