import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { IAffectation } from 'app/entities/affectation/affectation.model';
import { AffectationService } from 'app/entities/affectation/service/affectation.service';
import { StatutAffectation } from 'app/entities/enumerations/statut-affectation.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAffectationActif } from '../affectation-actif.model';
import { AffectationActifService } from '../service/affectation-actif.service';

import { AffectationActifFormGroup, AffectationActifFormService } from './affectation-actif-form.service';

@Component({
  selector: 'jhi-affectation-actif-update',
  templateUrl: './affectation-actif-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AffectationActifUpdate implements OnInit {
  readonly isSaving = signal(false);
  affectationActif: IAffectationActif | null = null;
  statutAffectationValues = Object.keys(StatutAffectation);

  affectationsSharedCollection = signal<IAffectation[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected affectationActifService = inject(AffectationActifService);
  protected affectationActifFormService = inject(AffectationActifFormService);
  protected affectationService = inject(AffectationService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AffectationActifFormGroup = this.affectationActifFormService.createAffectationActifFormGroup();

  compareAffectation = (o1: IAffectation | null, o2: IAffectation | null): boolean => this.affectationService.compareAffectation(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ affectationActif }) => {
      this.affectationActif = affectationActif;
      if (affectationActif) {
        this.updateForm(affectationActif);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const affectationActif = this.affectationActifFormService.getAffectationActif(this.editForm);
    if (affectationActif.id === null) {
      this.subscribeToSaveResponse(this.affectationActifService.create(affectationActif));
    } else {
      this.subscribeToSaveResponse(this.affectationActifService.update(affectationActif));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAffectationActif | null>): void {
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

  protected updateForm(affectationActif: IAffectationActif): void {
    this.affectationActif = affectationActif;
    this.affectationActifFormService.resetForm(this.editForm, affectationActif);

    this.affectationsSharedCollection.update(affectations =>
      this.affectationService.addAffectationToCollectionIfMissing<IAffectation>(affectations, affectationActif.affectation),
    );
    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, affectationActif.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.affectationService
      .query()
      .pipe(map((res: HttpResponse<IAffectation[]>) => res.body ?? []))
      .pipe(
        map((affectations: IAffectation[]) =>
          this.affectationService.addAffectationToCollectionIfMissing<IAffectation>(affectations, this.affectationActif?.affectation),
        ),
      )
      .subscribe((affectations: IAffectation[]) => this.affectationsSharedCollection.set(affectations));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.affectationActif?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
