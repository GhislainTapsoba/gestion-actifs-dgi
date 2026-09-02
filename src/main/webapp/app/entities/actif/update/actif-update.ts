import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize } from 'rxjs';

import { StatutActif } from 'app/entities/enumerations/statut-actif.model';
import { TypeActif } from 'app/entities/enumerations/type-actif.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IActif } from '../actif.model';
import { ActifService } from '../service/actif.service';

import { ActifFormGroup, ActifFormService } from './actif-form.service';

@Component({
  selector: 'jhi-actif-update',
  templateUrl: './actif-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class ActifUpdate implements OnInit {
  readonly isSaving = signal(false);
  actif: IActif | null = null;
  typeActifValues = Object.keys(TypeActif);
  statutActifValues = Object.keys(StatutActif);

  protected actifService = inject(ActifService);
  protected actifFormService = inject(ActifFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ActifFormGroup = this.actifFormService.createActifFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ actif }) => {
      this.actif = actif;
      if (actif) {
        this.updateForm(actif);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const actif = this.actifFormService.getActif(this.editForm);
    if (actif.id === null) {
      this.subscribeToSaveResponse(this.actifService.create(actif));
    } else {
      this.subscribeToSaveResponse(this.actifService.update(actif));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IActif | null>): void {
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

  protected updateForm(actif: IActif): void {
    this.actif = actif;
    this.actifFormService.resetForm(this.editForm, actif);
  }
}
