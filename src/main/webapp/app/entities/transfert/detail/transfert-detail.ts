import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { ITransfert } from '../transfert.model';

@Component({
  selector: 'jhi-transfert-detail',
  templateUrl: './transfert-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class TransfertDetail {
  readonly transfert = input<ITransfert | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
