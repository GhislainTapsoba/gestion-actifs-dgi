import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAffectationActif } from '../affectation-actif.model';

@Component({
  selector: 'jhi-affectation-actif-detail',
  templateUrl: './affectation-actif-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class AffectationActifDetail {
  readonly affectationActif = input<IAffectationActif | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
