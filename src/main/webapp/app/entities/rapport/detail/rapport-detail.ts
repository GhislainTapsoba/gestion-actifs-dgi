import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IRapport } from '../rapport.model';

@Component({
  selector: 'jhi-rapport-detail',
  templateUrl: './rapport-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, RouterLink, FormatMediumDatetimePipe],
})
export class RapportDetail {
  readonly rapport = input<IRapport | null>(null);

  previousState(): void {
    globalThis.history.back();
  }

  imprimer(): void {
    window.print();
  }
}
