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
import { TypeContrat } from 'app/entities/enumerations/type-contrat.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IContrat } from '../contrat.model';
import { ContratService } from '../service/contrat.service';

import { ContratFormGroup, ContratFormService } from './contrat-form.service';

@Component({
  selector: 'jhi-contrat-update',
  templateUrl: './contrat-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class ContratUpdate implements OnInit {
  readonly isSaving = signal(false);
  contrat: IContrat | null = null;
  typeContratValues = Object.keys(TypeContrat);

  fournisseursSharedCollection = signal<IFournisseur[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected contratService = inject(ContratService);
  protected contratFormService = inject(ContratFormService);
  protected fournisseurService = inject(FournisseurService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ContratFormGroup = this.contratFormService.createContratFormGroup();

  compareFournisseur = (o1: IFournisseur | null, o2: IFournisseur | null): boolean => this.fournisseurService.compareFournisseur(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contrat }) => {
      this.contrat = contrat;
      if (contrat) {
        this.updateForm(contrat);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const contrat = this.contratFormService.getContrat(this.editForm);
    if (contrat.id === null) {
      this.subscribeToSaveResponse(this.contratService.create(contrat));
    } else {
      this.subscribeToSaveResponse(this.contratService.update(contrat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IContrat | null>): void {
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

  protected updateForm(contrat: IContrat): void {
    this.contrat = contrat;
    this.contratFormService.resetForm(this.editForm, contrat);

    this.fournisseursSharedCollection.update(fournisseurs =>
      this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, contrat.fournisseur),
    );
    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, contrat.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.fournisseurService
      .query()
      .pipe(map((res: HttpResponse<IFournisseur[]>) => res.body ?? []))
      .pipe(
        map((fournisseurs: IFournisseur[]) =>
          this.fournisseurService.addFournisseurToCollectionIfMissing<IFournisseur>(fournisseurs, this.contrat?.fournisseur),
        ),
      )
      .subscribe((fournisseurs: IFournisseur[]) => this.fournisseursSharedCollection.set(fournisseurs));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.contrat?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
