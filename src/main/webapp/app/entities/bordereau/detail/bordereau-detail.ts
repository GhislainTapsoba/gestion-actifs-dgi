import { Component, inject, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IBordereau } from '../bordereau.model';
import { BordereauPdfService } from '../service/bordereau-pdf.service';

@Component({
  selector: 'jhi-bordereau-detail',
  templateUrl: './bordereau-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class BordereauDetail {
  readonly bordereau = input<IBordereau | null>(null);

  protected readonly bordereauPdfService = inject(BordereauPdfService);

  imprimer(): void {
    const b = this.bordereau();
    if (b) {
      this.bordereauPdfService.imprimerBordereau(b);
    }
  }

  previousState(): void {
    globalThis.history.back();
  }
}
