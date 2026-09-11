import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { AccountService } from 'app/core/auth';
import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { TransfertService } from '../service/transfert.service';
import { ITransfert } from '../transfert.model';

@Component({
  selector: 'jhi-transfert-detail',
  templateUrl: './transfert-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class TransfertDetail {
  readonly transfert = input<ITransfert | null>(null);

  protected readonly accountService = inject(AccountService);
  protected readonly transfertService = inject(TransfertService);

  canValidate(): boolean {
    return this.accountService.hasAnyAuthority(['ROLE_ADMIN', 'ROLE_RESPONSABLE']);
  }

  valider(): void {
    const t = this.transfert();
    if (!t?.id) return;
    if (confirm(`Confirmez-vous la validation du transfert #${t.id} ?`)) {
      this.transfertService.valider(t.id).subscribe({
        next: () => {
          this.previousState();
        },
      });
    }
  }

  rejeter(): void {
    const t = this.transfert();
    if (!t?.id) return;
    const motif = prompt(`Veuillez saisir le motif du rejet pour le transfert #${t.id} :`);
    if (motif === null) return;
    if (!motif.trim()) {
      alert('Le motif du rejet est obligatoire.');
      return;
    }
    this.transfertService.rejeter(t.id, motif.trim()).subscribe({
      next: () => {
        this.previousState();
      },
    });
  }

  previousState(): void {
    globalThis.history.back();
  }
}
