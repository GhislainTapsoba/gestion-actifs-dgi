import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { StatutTransfert } from 'app/entities/enumerations/statut-transfert.model';
import { ServiceDgiService } from 'app/entities/service-dgi/service/service-dgi.service';
import { IServiceDgi } from 'app/entities/service-dgi/service-dgi.model';
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

  serviceDgisSharedCollection = signal<IServiceDgi[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected transfertService = inject(TransfertService);
  protected transfertFormService = inject(TransfertFormService);
  protected serviceDgiService = inject(ServiceDgiService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TransfertFormGroup = this.transfertFormService.createTransfertFormGroup();

  compareServiceDgi = (o1: IServiceDgi | null, o2: IServiceDgi | null): boolean => this.serviceDgiService.compareServiceDgi(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

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

    this.serviceDgisSharedCollection.update(serviceDgis =>
      this.serviceDgiService.addServiceDgiToCollectionIfMissing<IServiceDgi>(
        serviceDgis,
        transfert.serviceOrigine,
        transfert.serviceDestinataire,
      ),
    );
    this.usersSharedCollection.update(users =>
      this.userService.addUserToCollectionIfMissing<IUser>(users, transfert.demandeur, transfert.validateur),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceDgiService
      .query()
      .pipe(map((res: HttpResponse<IServiceDgi[]>) => res.body ?? []))
      .pipe(
        map((serviceDgis: IServiceDgi[]) =>
          this.serviceDgiService.addServiceDgiToCollectionIfMissing<IServiceDgi>(
            serviceDgis,
            this.transfert?.serviceOrigine,
            this.transfert?.serviceDestinataire,
          ),
        ),
      )
      .subscribe((serviceDgis: IServiceDgi[]) => this.serviceDgisSharedCollection.set(serviceDgis));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(
        map((users: IUser[]) =>
          this.userService.addUserToCollectionIfMissing<IUser>(users, this.transfert?.demandeur, this.transfert?.validateur),
        ),
      )
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
