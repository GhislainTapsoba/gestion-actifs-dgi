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
import { StatutTransfert } from 'app/entities/enumerations/statut-transfert.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { TransfertService } from '../service/transfert.service';
import { ITransfert } from '../transfert.model';

import { TransfertFormGroup, TransfertFormService } from './transfert-form.service';

@Component({
  selector: 'jhi-transfert-update',
  templateUrl: './transfert-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class TransfertUpdate implements OnInit {
  readonly isSaving = signal(false);
  transfert: ITransfert | null = null;
  statutTransfertValues = Object.keys(StatutTransfert);

  usersSharedCollection = signal<IUser[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected transfertService = inject(TransfertService);
  protected transfertFormService = inject(TransfertFormService);
  protected userService = inject(UserService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransfertFormGroup = this.transfertFormService.createTransfertFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transfert }) => {
      this.transfert = transfert;
      if (transfert) {
        this.updateForm(transfert);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const transfert = this.transfertFormService.getTransfert(this.editForm);
    if (transfert.id === null) {
      this.subscribeToSaveResponse(this.transfertService.create(transfert));
    } else {
      this.subscribeToSaveResponse(this.transfertService.update(transfert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<ITransfert | null>): void {
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

  protected updateForm(transfert: ITransfert): void {
    this.transfert = transfert;
    this.transfertFormService.resetForm(this.editForm, transfert);

    this.usersSharedCollection.update(users =>
      this.userService.addUserToCollectionIfMissing<IUser>(users, transfert.demandeur, transfert.validateur),
    );
    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, transfert.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.transfert?.demandeur, this.transfert?.validateur),
        ),
      )
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.transfert?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
