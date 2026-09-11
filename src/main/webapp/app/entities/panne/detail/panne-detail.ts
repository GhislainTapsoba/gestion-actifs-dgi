import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPanne } from '../panne.model';

@Component({
  selector: 'jhi-panne-detail',
  templateUrl: './panne-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class PanneDetail {
  readonly panne = input<IPanne | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
