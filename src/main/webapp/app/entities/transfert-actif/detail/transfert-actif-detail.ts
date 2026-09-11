import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ITransfertActif } from '../transfert-actif.model';

@Component({
  selector: 'jhi-transfert-actif-detail',
  templateUrl: './transfert-actif-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class TransfertActifDetail {
  readonly transfertActif = input<ITransfertActif | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
