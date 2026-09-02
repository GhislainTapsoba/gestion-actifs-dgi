import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAffectation } from '../affectation.model';
import { AffectationService } from '../service/affectation.service';

import { AffectationFormGroup, AffectationFormService } from './affectation-form.service';

@Component({
  selector: 'jhi-affectation-update',
  templateUrl: './affectation-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class AffectationUpdate implements OnInit {
  readonly isSaving = signal(false);
  affectation: IAffectation | null = null;

  usersSharedCollection = signal<IUser[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected affectationService = inject(AffectationService);
  protected affectationFormService = inject(AffectationFormService);
  protected userService = inject(UserService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AffectationFormGroup = this.affectationFormService.createAffectationFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ affectation }) => {
      this.affectation = affectation;
      if (affectation) {
        this.updateForm(affectation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const affectation = this.affectationFormService.getAffectation(this.editForm);
    if (affectation.id === null) {
      this.subscribeToSaveResponse(this.affectationService.create(affectation));
    } else {
      this.subscribeToSaveResponse(this.affectationService.update(affectation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAffectation | null>): void {
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

  protected updateForm(affectation: IAffectation): void {
    this.affectation = affectation;
    this.affectationFormService.resetForm(this.editForm, affectation);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, affectation.utilisateur));
    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, affectation.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.affectation?.utilisateur)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.affectation?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
