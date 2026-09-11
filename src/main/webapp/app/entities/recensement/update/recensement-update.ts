import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize } from 'rxjs';

import { StatutRecensement } from 'app/entities/enumerations/statut-recensement.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IRecensement } from '../recensement.model';
import { RecensementService } from '../service/recensement.service';

import { RecensementFormGroup, RecensementFormService } from './recensement-form.service';

@Component({
  selector: 'jhi-recensement-update',
  templateUrl: './recensement-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class RecensementUpdate implements OnInit {
  readonly isSaving = signal(false);
  recensement: IRecensement | null = null;
  statutRecensementValues = Object.keys(StatutRecensement);

  protected recensementService = inject(RecensementService);
  protected recensementFormService = inject(RecensementFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecensementFormGroup = this.recensementFormService.createRecensementFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recensement }) => {
      this.recensement = recensement;
      if (recensement) {
        this.updateForm(recensement);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const recensement = this.recensementFormService.getRecensement(this.editForm);
    if (recensement.id === null) {
      this.subscribeToSaveResponse(this.recensementService.create(recensement));
    } else {
      this.subscribeToSaveResponse(this.recensementService.update(recensement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IRecensement | null>): void {
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

  protected updateForm(recensement: IRecensement): void {
    this.recensement = recensement;
    this.recensementFormService.resetForm(this.editForm, recensement);
  }
}
