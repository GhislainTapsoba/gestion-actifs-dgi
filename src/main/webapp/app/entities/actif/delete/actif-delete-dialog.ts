import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IActif } from '../actif.model';
import { ActifService } from '../service/actif.service';

@Component({
  templateUrl: './actif-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class ActifDeleteDialog {
  actif?: IActif;

  protected readonly actifService = inject(ActifService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.actifService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
