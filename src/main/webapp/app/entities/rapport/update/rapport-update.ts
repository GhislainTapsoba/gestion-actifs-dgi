import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AccountService } from 'app/core/auth';
import { AlertError } from 'app/shared/alert';
import { IRapport } from '../rapport.model';
import { RapportService } from '../service/rapport.service';

import { RapportFormGroup, RapportFormService } from './rapport-form.service';

@Component({
  selector: 'jhi-rapport-update',
  templateUrl: './rapport-update.html',
  imports: [FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class RapportUpdate implements OnInit {
  readonly isSaving = signal(false);
  rapport: IRapport | null = null;

  typeRapportValues = ['INVENTAIRE', 'AFFECTATION', 'TRANSFERT', 'MAINTENANCE', 'REFORME', 'PANNE', 'AUDIT'];
  formatExportValues = ['PDF', 'EXCEL', 'CSV'];

  protected rapportService = inject(RapportService);
  protected rapportFormService = inject(RapportFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected accountService = inject(AccountService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RapportFormGroup = this.rapportFormService.createRapportFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rapport }) => {
      this.rapport = rapport;
      if (rapport) {
        this.updateForm(rapport);
      } else {
        const account = this.accountService.account();
        if (account?.login) {
          this.editForm.patchValue({ generePar: account.login });
        }
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const rapport = this.rapportFormService.getRapport(this.editForm);
    if (rapport.id === null) {
      this.subscribeToSaveResponse(this.rapportService.create(rapport));
    } else {
      this.subscribeToSaveResponse(this.rapportService.update(rapport));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRapport | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(rapport: IRapport): void {
    this.rapport = rapport;
    this.rapportFormService.resetForm(this.editForm, rapport);
  }
}
