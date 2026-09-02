import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IAffectation } from '../affectation.model';

@Component({
  selector: 'jhi-affectation-detail',
  templateUrl: './affectation-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class AffectationDetail {
  readonly affectation = input<IAffectation | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
