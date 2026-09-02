import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TransfertService } from '../service/transfert.service';
import { ITransfert } from '../transfert.model';

@Component({
  templateUrl: './transfert-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class TransfertDeleteDialog {
  transfert?: ITransfert;

  protected readonly transfertService = inject(TransfertService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transfertService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
