import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { IAffectation } from 'app/entities/affectation/affectation.model';
import { AffectationService } from 'app/entities/affectation/service/affectation.service';
import { TypeBordereau } from 'app/entities/enumerations/type-bordereau.model';
import { TransfertService } from 'app/entities/transfert/service/transfert.service';
import { ITransfert } from 'app/entities/transfert/transfert.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IBordereau } from '../bordereau.model';
import { BordereauService } from '../service/bordereau.service';

import { BordereauFormGroup, BordereauFormService } from './bordereau-form.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { StatutBordereau } from 'app/entities/enumerations/statut-bordereau.model';

@Component({
  selector: 'jhi-bordereau-update',
  templateUrl: './bordereau-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class BordereauUpdate implements OnInit {
  readonly isSaving = signal(false);
  bordereau: IBordereau | null = null;
  typeBordereauValues = Object.keys(TypeBordereau);
  statutBordereauValues = Object.keys(StatutBordereau);

  transfertsSharedCollection = signal<ITransfert[]>([]);
  affectationsSharedCollection = signal<IAffectation[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected bordereauService = inject(BordereauService);
  protected bordereauFormService = inject(BordereauFormService);
  protected transfertService = inject(TransfertService);
  protected affectationService = inject(AffectationService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BordereauFormGroup = this.bordereauFormService.createBordereauFormGroup();

  compareTransfert = (o1: ITransfert | null, o2: ITransfert | null): boolean => this.transfertService.compareTransfert(o1, o2);

  compareAffectation = (o1: IAffectation | null, o2: IAffectation | null): boolean => this.affectationService.compareAffectation(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bordereau }) => {
      this.bordereau = bordereau;
      if (bordereau) {
        this.updateForm(bordereau);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const bordereau = this.bordereauFormService.getBordereau(this.editForm);
    if (bordereau.id === null) {
      this.subscribeToSaveResponse(this.bordereauService.create(bordereau));
    } else {
      this.subscribeToSaveResponse(this.bordereauService.update(bordereau));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IBordereau | null>): void {
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

  protected updateForm(bordereau: IBordereau): void {
    this.bordereau = bordereau;
    this.bordereauFormService.resetForm(this.editForm, bordereau);

    this.transfertsSharedCollection.update(transferts =>
      this.transfertService.addTransfertToCollectionIfMissing<ITransfert>(transferts, bordereau.transfert),
    );
    this.affectationsSharedCollection.update(affectations =>
      this.affectationService.addAffectationToCollectionIfMissing<IAffectation>(affectations, bordereau.affectation),
    );
    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, bordereau.emetteur));
  }

  protected loadRelationshipsOptions(): void {
    this.transfertService
      .query()
      .pipe(map((res: HttpResponse<ITransfert[]>) => res.body ?? []))
      .pipe(
        map((transferts: ITransfert[]) =>
          this.transfertService.addTransfertToCollectionIfMissing<ITransfert>(transferts, this.bordereau?.transfert),
        ),
      )
      .subscribe((transferts: ITransfert[]) => this.transfertsSharedCollection.set(transferts));

    this.affectationService
      .query()
      .pipe(map((res: HttpResponse<IAffectation[]>) => res.body ?? []))
      .pipe(
        map((affectations: IAffectation[]) =>
          this.affectationService.addAffectationToCollectionIfMissing<IAffectation>(affectations, this.bordereau?.affectation),
        ),
      )
      .subscribe((affectations: IAffectation[]) => this.affectationsSharedCollection.set(affectations));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.bordereau?.emetteur)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
