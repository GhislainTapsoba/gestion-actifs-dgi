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
import { EtatMateriel } from 'app/entities/enumerations/etat-materiel.model';
import { IRecensement } from 'app/entities/recensement/recensement.model';
import { RecensementService } from 'app/entities/recensement/service/recensement.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IEquipementRecensement } from '../equipement-recensement.model';
import { EquipementRecensementService } from '../service/equipement-recensement.service';

import { EquipementRecensementFormGroup, EquipementRecensementFormService } from './equipement-recensement-form.service';

@Component({
  selector: 'jhi-equipement-recensement-update',
  templateUrl: './equipement-recensement-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class EquipementRecensementUpdate implements OnInit {
  readonly isSaving = signal(false);
  equipementRecensement: IEquipementRecensement | null = null;
  etatMaterielValues = Object.keys(EtatMateriel);

  recensementsSharedCollection = signal<IRecensement[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected equipementRecensementService = inject(EquipementRecensementService);
  protected equipementRecensementFormService = inject(EquipementRecensementFormService);
  protected recensementService = inject(RecensementService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EquipementRecensementFormGroup = this.equipementRecensementFormService.createEquipementRecensementFormGroup();

  compareRecensement = (o1: IRecensement | null, o2: IRecensement | null): boolean => this.recensementService.compareRecensement(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ equipementRecensement }) => {
      this.equipementRecensement = equipementRecensement;
      if (equipementRecensement) {
        this.updateForm(equipementRecensement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const equipementRecensement = this.equipementRecensementFormService.getEquipementRecensement(this.editForm);
    if (equipementRecensement.id === null) {
      this.subscribeToSaveResponse(this.equipementRecensementService.create(equipementRecensement));
    } else {
      this.subscribeToSaveResponse(this.equipementRecensementService.update(equipementRecensement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IEquipementRecensement | null>): void {
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

  protected updateForm(equipementRecensement: IEquipementRecensement): void {
    this.equipementRecensement = equipementRecensement;
    this.equipementRecensementFormService.resetForm(this.editForm, equipementRecensement);

    this.recensementsSharedCollection.update(recensements =>
      this.recensementService.addRecensementToCollectionIfMissing<IRecensement>(recensements, equipementRecensement.recensement),
    );
    this.actifsSharedCollection.update(actifs =>
      this.actifService.addActifToCollectionIfMissing<IActif>(actifs, equipementRecensement.actif),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.recensementService
      .query()
      .pipe(map((res: HttpResponse<IRecensement[]>) => res.body ?? []))
      .pipe(
        map((recensements: IRecensement[]) =>
          this.recensementService.addRecensementToCollectionIfMissing<IRecensement>(recensements, this.equipementRecensement?.recensement),
        ),
      )
      .subscribe((recensements: IRecensement[]) => this.recensementsSharedCollection.set(recensements));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.equipementRecensement?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
