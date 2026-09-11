import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ICategorieMateriel } from '../categorie-materiel.model';

@Component({
  selector: 'jhi-categorie-materiel-detail',
  templateUrl: './categorie-materiel-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class CategorieMaterielDetail {
  readonly categorieMateriel = input<ICategorieMateriel | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
