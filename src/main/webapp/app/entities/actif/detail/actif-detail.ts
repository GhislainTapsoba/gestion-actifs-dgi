import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IActif } from '../actif.model';

@Component({
  selector: 'jhi-actif-detail',
  templateUrl: './actif-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class ActifDetail {
  readonly actif = input<IActif | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
