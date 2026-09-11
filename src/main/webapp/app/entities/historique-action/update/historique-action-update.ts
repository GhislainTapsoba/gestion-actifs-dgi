import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { TypeMouvement } from 'app/entities/enumerations/type-mouvement.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IHistoriqueAction } from '../historique-action.model';
import { HistoriqueActionService } from '../service/historique-action.service';

import { HistoriqueActionFormGroup, HistoriqueActionFormService } from './historique-action-form.service';

@Component({
  selector: 'jhi-historique-action-update',
  templateUrl: './historique-action-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class HistoriqueActionUpdate implements OnInit {
  readonly isSaving = signal(false);
  historiqueAction: IHistoriqueAction | null = null;
  typeMouvementValues = Object.keys(TypeMouvement);

  usersSharedCollection = signal<IUser[]>([]);

  protected historiqueActionService = inject(HistoriqueActionService);
  protected historiqueActionFormService = inject(HistoriqueActionFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HistoriqueActionFormGroup = this.historiqueActionFormService.createHistoriqueActionFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historiqueAction }) => {
      this.historiqueAction = historiqueAction;
      if (historiqueAction) {
        this.updateForm(historiqueAction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const historiqueAction = this.historiqueActionFormService.getHistoriqueAction(this.editForm);
    if (historiqueAction.id === null) {
      this.subscribeToSaveResponse(this.historiqueActionService.create(historiqueAction));
    } else {
      this.subscribeToSaveResponse(this.historiqueActionService.update(historiqueAction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IHistoriqueAction | null>): void {
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

  protected updateForm(historiqueAction: IHistoriqueAction): void {
    this.historiqueAction = historiqueAction;
    this.historiqueActionFormService.resetForm(this.editForm, historiqueAction);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, historiqueAction.utilisateur));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.historiqueAction?.utilisateur)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
