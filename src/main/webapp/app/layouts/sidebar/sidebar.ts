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
    { label: 'Tableau de bord', icon: 'home', route: '/', exact: true },
    { label: 'Inventaire', icon: 'desktop', route: '/actif' },
    { label: 'Catégories', icon: 'tags', route: '/categorie-materiel', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN'] },
    { label: 'Affectations', icon: 'users', route: '/affectation' },
    { label: 'Transferts', icon: 'exchange-alt', route: '/transfert' },
    { label: 'Maintenance', icon: 'tools', route: '/maintenance' },
  ];

  readonly incidentItems: NavItem[] = [
    { label: 'Pannes', icon: 'exclamation-triangle', route: '/panne' },
    { label: 'Interventions', icon: 'wrench', route: '/intervention' },
    { label: 'Plannings', icon: 'calendar-alt', route: '/planning-maintenance', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN'] },
    { label: 'Recensements', icon: 'clipboard-list', route: '/recensement', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN'] },
  ];

  readonly gestionItems: NavItem[] = [
    { label: 'Agents', icon: 'user-tie', route: '/agent', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Services DGI', icon: 'building', route: '/service-dgi', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Bordereaux', icon: 'file-invoice', route: '/bordereau', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN', 'ROLE_RESPONSABLE'] },
    { label: 'Fournisseurs', icon: 'truck', route: '/fournisseur', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN'] },
    { label: 'Contrats', icon: 'file-contract', route: '/contrat', roles: ['ROLE_ADMIN', 'ROLE_TECHNICIEN'] },
    { label: 'Historique', icon: 'history', route: '/historique-action', roles: ['ROLE_ADMIN'] },
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
    if (authorities.includes('ROLE_RESPONSABLE')) return 'Responsable';
    if (authorities.includes('ROLE_AGENT')) return 'Agent';
    return '';
  }
}
