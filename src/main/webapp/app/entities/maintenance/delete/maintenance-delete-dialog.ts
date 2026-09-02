import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap/modal';

import { ITEM_DELETED_EVENT } from 'app/config';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IMaintenance } from '../maintenance.model';
import { MaintenanceService } from '../service/maintenance.service';

@Component({
  templateUrl: './maintenance-delete-dialog.html',
  imports: [TranslateDirective, FormsModule, FontAwesomeModule, AlertError],
})
export class MaintenanceDeleteDialog {
  maintenance?: IMaintenance;

  protected readonly maintenanceService = inject(MaintenanceService);
  protected readonly activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.maintenanceService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
