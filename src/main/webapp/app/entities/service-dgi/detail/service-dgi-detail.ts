import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IServiceDgi } from '../service-dgi.model';

@Component({
  selector: 'jhi-service-dgi-detail',
  templateUrl: './service-dgi-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink],
})
export class ServiceDgiDetail {
  readonly serviceDgi = input<IServiceDgi | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
