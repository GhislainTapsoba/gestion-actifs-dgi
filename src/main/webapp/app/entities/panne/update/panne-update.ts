import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { StatutPanne } from 'app/entities/enumerations/statut-panne.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPanne } from '../panne.model';
import { PanneService } from '../service/panne.service';

import { PanneFormGroup, PanneFormService } from './panne-form.service';

@Component({
  selector: 'jhi-panne-update',
  templateUrl: './panne-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PanneUpdate implements OnInit {
  readonly isSaving = signal(false);
  panne: IPanne | null = null;
  statutPanneValues = Object.keys(StatutPanne);

  actifsSharedCollection = signal<IActif[]>([]);

  protected panneService = inject(PanneService);
  protected panneFormService = inject(PanneFormService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PanneFormGroup = this.panneFormService.createPanneFormGroup();

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ panne }) => {
      this.panne = panne;
      if (panne) {
        this.updateForm(panne);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const panne = this.panneFormService.getPanne(this.editForm);
    if (panne.id === null) {
      this.subscribeToSaveResponse(this.panneService.create(panne));
    } else {
      this.subscribeToSaveResponse(this.panneService.update(panne));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPanne | null>): void {
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

  protected updateForm(panne: IPanne): void {
    this.panne = panne;
    this.panneFormService.resetForm(this.editForm, panne);

    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, panne.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.panne?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
