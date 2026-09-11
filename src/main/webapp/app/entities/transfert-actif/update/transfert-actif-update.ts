import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { TransfertService } from 'app/entities/transfert/service/transfert.service';
import { ITransfert } from 'app/entities/transfert/transfert.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { TransfertActifService } from '../service/transfert-actif.service';
import { ITransfertActif } from '../transfert-actif.model';

import { TransfertActifFormGroup, TransfertActifFormService } from './transfert-actif-form.service';

@Component({
  selector: 'jhi-transfert-actif-update',
  templateUrl: './transfert-actif-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class TransfertActifUpdate implements OnInit {
  readonly isSaving = signal(false);
  transfertActif: ITransfertActif | null = null;

  transfertsSharedCollection = signal<ITransfert[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected transfertActifService = inject(TransfertActifService);
  protected transfertActifFormService = inject(TransfertActifFormService);
  protected transfertService = inject(TransfertService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransfertActifFormGroup = this.transfertActifFormService.createTransfertActifFormGroup();

  compareTransfert = (o1: ITransfert | null, o2: ITransfert | null): boolean => this.transfertService.compareTransfert(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transfertActif }) => {
      this.transfertActif = transfertActif;
      if (transfertActif) {
        this.updateForm(transfertActif);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const transfertActif = this.transfertActifFormService.getTransfertActif(this.editForm);
    if (transfertActif.id === null) {
      this.subscribeToSaveResponse(this.transfertActifService.create(transfertActif));
    } else {
      this.subscribeToSaveResponse(this.transfertActifService.update(transfertActif));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITransfertActif | null>): void {
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

  protected updateForm(transfertActif: ITransfertActif): void {
    this.transfertActif = transfertActif;
    this.transfertActifFormService.resetForm(this.editForm, transfertActif);

    this.transfertsSharedCollection.update(transferts =>
      this.transfertService.addTransfertToCollectionIfMissing<ITransfert>(transferts, transfertActif.transfert),
    );
    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, transfertActif.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.transfertService
      .query()
      .pipe(map((res: HttpResponse<ITransfert[]>) => res.body ?? []))
      .pipe(
        map((transferts: ITransfert[]) =>
          this.transfertService.addTransfertToCollectionIfMissing<ITransfert>(transferts, this.transfertActif?.transfert),
        ),
      )
      .subscribe((transferts: ITransfert[]) => this.transfertsSharedCollection.set(transferts));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.transfertActif?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
