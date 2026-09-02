import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAffectation } from '../affectation.model';
import { AffectationService } from '../service/affectation.service';

@Component({
  templateUrl: './affectation-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class AffectationDeleteDialog {
  affectation?: IAffectation;

  protected readonly affectationService = inject(AffectationService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.affectationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
