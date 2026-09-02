import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IContrat } from '../contrat.model';

@Component({
  selector: 'jhi-contrat-detail',
  templateUrl: './contrat-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class ContratDetail {
  readonly contrat = input<IContrat | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
