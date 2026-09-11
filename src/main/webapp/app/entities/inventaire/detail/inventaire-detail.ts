import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IInventaire } from '../inventaire.model';

@Component({
  selector: 'jhi-inventaire-detail',
  templateUrl: './inventaire-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class InventaireDetail {
  readonly inventaire = input<IInventaire | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
