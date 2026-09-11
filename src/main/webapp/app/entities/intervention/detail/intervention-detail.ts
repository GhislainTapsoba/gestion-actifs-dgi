import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IIntervention } from '../intervention.model';

@Component({
  selector: 'jhi-intervention-detail',
  templateUrl: './intervention-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class InterventionDetail {
  readonly intervention = input<IIntervention | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
