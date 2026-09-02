import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

@Component({
  templateUrl: './fournisseur-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class FournisseurDeleteDialog {
  fournisseur?: IFournisseur;

  protected readonly fournisseurService = inject(FournisseurService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fournisseurService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
