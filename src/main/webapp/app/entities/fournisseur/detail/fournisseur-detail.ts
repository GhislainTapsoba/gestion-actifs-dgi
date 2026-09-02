import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IFournisseur } from '../fournisseur.model';

@Component({
  selector: 'jhi-fournisseur-detail',
  templateUrl: './fournisseur-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class FournisseurDetail {
  readonly fournisseur = input<IFournisseur | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
