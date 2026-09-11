import { Component, input } from '@angular/core';
import { RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { Alert, AlertError } from 'app/shared/alert';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TranslateDirective } from 'app/shared/language';
import { IPlanningMaintenance } from '../planning-maintenance.model';

@Component({
  selector: 'jhi-planning-maintenance-detail',
  templateUrl: './planning-maintenance-detail.html',
  imports: [FontAwesomeModule, Alert, AlertError, TranslateDirective, RouterLink, FormatMediumDatePipe],
})
export class PlanningMaintenanceDetail {
  readonly planningMaintenance = input<IPlanningMaintenance | null>(null);

  previousState(): void {
    globalThis.history.back();
  }
}
