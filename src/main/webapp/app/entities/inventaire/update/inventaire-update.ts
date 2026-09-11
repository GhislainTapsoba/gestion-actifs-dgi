import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IInventaire } from '../inventaire.model';
import { InventaireService } from '../service/inventaire.service';

import { InventaireFormGroup, InventaireFormService } from './inventaire-form.service';

@Component({
  selector: 'jhi-inventaire-update',
  templateUrl: './inventaire-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class InventaireUpdate implements OnInit {
  readonly isSaving = signal(false);
  inventaire: IInventaire | null = null;

  actifsSharedCollection = signal<IActif[]>([]);

  protected inventaireService = inject(InventaireService);
  protected inventaireFormService = inject(InventaireFormService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InventaireFormGroup = this.inventaireFormService.createInventaireFormGroup();

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inventaire }) => {
      this.inventaire = inventaire;
      if (inventaire) {
        this.updateForm(inventaire);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const inventaire = this.inventaireFormService.getInventaire(this.editForm);
    if (inventaire.id === null) {
      this.subscribeToSaveResponse(this.inventaireService.create(inventaire));
    } else {
      this.subscribeToSaveResponse(this.inventaireService.update(inventaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IInventaire | null>): void {
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

  protected updateForm(inventaire: IInventaire): void {
    this.inventaire = inventaire;
    this.inventaireFormService.resetForm(this.editForm, inventaire);

    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, inventaire.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.inventaire?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
