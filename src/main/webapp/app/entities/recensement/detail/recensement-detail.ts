import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IRecensement } from '../recensement.model';

@Component({
  selector: 'jhi-recensement-detail',
  templateUrl: './recensement-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class RecensementDetail {
  readonly recensement = input<IRecensement | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
