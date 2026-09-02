import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { DataUtils, EventManager, EventWithContent, FileLoadError } from 'app/core/util';
import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { StatutMaintenance } from 'app/entities/enumerations/statut-maintenance.model';
import { TypeMaintenance } from 'app/entities/enumerations/type-maintenance.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError, AlertErrorModel } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IMaintenance } from '../maintenance.model';
import { MaintenanceService } from '../service/maintenance.service';

import { MaintenanceFormGroup, MaintenanceFormService } from './maintenance-form.service';

@Component({
  selector: 'jhi-maintenance-update',
  templateUrl: './maintenance-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class MaintenanceUpdate implements OnInit {
  readonly isSaving = signal(false);
  maintenance: IMaintenance | null = null;
  typeMaintenanceValues = Object.keys(TypeMaintenance);
  statutMaintenanceValues = Object.keys(StatutMaintenance);

  usersSharedCollection = signal<IUser[]>([]);
  actifsSharedCollection = signal<IActif[]>([]);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected maintenanceService = inject(MaintenanceService);
  protected maintenanceFormService = inject(MaintenanceFormService);
  protected userService = inject(UserService);
  protected actifService = inject(ActifService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MaintenanceFormGroup = this.maintenanceFormService.createMaintenanceFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareActif = (o1: IActif | null, o2: IActif | null): boolean => this.actifService.compareActif(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maintenance }) => {
      this.maintenance = maintenance;
      if (maintenance) {
        this.updateForm(maintenance);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertErrorModel>('gestionActifsDgiApp.error', { ...err, key: `error.file.${err.key}` }),
        ),
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const maintenance = this.maintenanceFormService.getMaintenance(this.editForm);
    if (maintenance.id === null) {
      this.subscribeToSaveResponse(this.maintenanceService.create(maintenance));
    } else {
      this.subscribeToSaveResponse(this.maintenanceService.update(maintenance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IMaintenance | null>): void {
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

  protected updateForm(maintenance: IMaintenance): void {
    this.maintenance = maintenance;
    this.maintenanceFormService.resetForm(this.editForm, maintenance);

    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, maintenance.technicien));
    this.actifsSharedCollection.update(actifs => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, maintenance.actif));
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.maintenance?.technicien)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));

    this.actifService
      .query()
      .pipe(map((res: HttpResponse<IActif[]>) => res.body ?? []))
      .pipe(map((actifs: IActif[]) => this.actifService.addActifToCollectionIfMissing<IActif>(actifs, this.maintenance?.actif)))
      .subscribe((actifs: IActif[]) => this.actifsSharedCollection.set(actifs));
  }
}
