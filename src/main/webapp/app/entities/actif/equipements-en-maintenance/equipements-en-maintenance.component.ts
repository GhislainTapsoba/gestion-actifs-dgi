import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IActif } from '../actif.model';
import { ActifDeleteDialog } from '../delete/actif-delete-dialog';
import { ActifService } from '../service/actif.service';

@Component({
  selector: 'jhi-equipements-en-maintenance',
  templateUrl: './equipements-en-maintenance.component.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, FormatMediumDatePipe],
})
export class EquipementsEnMaintenanceComponent implements OnInit {
  readonly actifs = signal<IActif[]>([]);
  readonly isLoading = signal(false);
  readonly searchTerm = signal('');

  protected readonly actifService = inject(ActifService);
  protected readonly modalService = inject(NgbModal);

  readonly filteredActifs = computed(() => {
    const term = this.searchTerm().trim().toLowerCase();
    const list = this.actifs();
    if (!term) {
      return list;
    }
    return list.filter(
      item =>
        item.codeInventaire?.toLowerCase().includes(term) ||
        item.designation?.toLowerCase().includes(term) ||
        item.marque?.toLowerCase().includes(term) ||
        item.modele?.toLowerCase().includes(term) ||
        item.localisation?.toLowerCase().includes(term) ||
        item.numeroSerie?.toLowerCase().includes(term),
    );
  });

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading.set(true);
    this.actifService.findEquipementsEnMaintenance().subscribe({
      next: (data: IActif[]) => {
        this.isLoading.set(false);
        this.actifs.set(data ?? []);
      },
      error: () => {
        this.isLoading.set(false);
      },
    });
  }

  trackId = (_index: number, item: IActif): number => item.id!;

  delete(actif: IActif): void {
    const modalRef = this.modalService.open(ActifDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.actif = actif;
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  imprimer(): void {
    window.print();
  }

  exporterCSV(): void {
    const headers = ['ID', 'Code Inventaire', 'Désignation', 'Marque', 'Modèle', 'N° Série', 'État', 'Localisation'];
    const rows = this.filteredActifs().map(a => [
      a.id ?? '',
      `"${(a.codeInventaire ?? '').replace(/"/g, '""')}"`,
      `"${(a.designation ?? '').replace(/"/g, '""')}"`,
      `"${(a.marque ?? '').replace(/"/g, '""')}"`,
      `"${(a.modele ?? '').replace(/"/g, '""')}"`,
      `"${(a.numeroSerie ?? '').replace(/"/g, '""')}"`,
      `"${(a.etat ?? '').replace(/"/g, '""')}"`,
      `"${(a.localisation ?? '').replace(/"/g, '""')}"`,
    ]);

    const csvContent = '\uFEFF' + [headers.join(';'), ...rows.map(r => r.join(';'))].join('\r\n');
    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.setAttribute('href', url);
    link.setAttribute('download', `equipements_en_maintenance_${new Date().toISOString().slice(0, 10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  }
}
