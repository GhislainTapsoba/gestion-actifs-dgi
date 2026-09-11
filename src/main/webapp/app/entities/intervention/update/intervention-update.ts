import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { StatutIntervention } from 'app/entities/enumerations/statut-intervention.model';
import { TypeIntervention } from 'app/entities/enumerations/type-intervention.model';
import { IPanne } from 'app/entities/panne/panne.model';
import { PanneService } from 'app/entities/panne/service/panne.service';
import { IPlanningMaintenance } from 'app/entities/planning-maintenance/planning-maintenance.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';

import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';

import { InterventionFormGroup, InterventionFormService } from './intervention-form.service';
import { PlanningMaintenanceService } from 'app/entities/planning-maintenance/service/planning-maintenance.service';

@Component({
  selector: 'jhi-intervention-update',
  templateUrl: './intervention-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class InterventionUpdate implements OnInit {
  readonly isSaving = signal(false);
  intervention: IIntervention | null = null;
  typeInterventionValues = Object.keys(TypeIntervention);
  statutInterventionValues = Object.keys(StatutIntervention);

  pannesSharedCollection = signal<IPanne[]>([]);
  planningMaintenancesSharedCollection = signal<IPlanningMaintenance[]>([]);

  protected interventionService = inject(InterventionService);
  protected interventionFormService = inject(InterventionFormService);
  protected panneService = inject(PanneService);
  protected planningMaintenanceService = inject(PlanningMaintenanceService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InterventionFormGroup = this.interventionFormService.createInterventionFormGroup();

  comparePanne = (o1: IPanne | null, o2: IPanne | null): boolean => this.panneService.comparePanne(o1, o2);

  comparePlanningMaintenance = (o1: IPlanningMaintenance | null, o2: IPlanningMaintenance | null): boolean =>
    this.planningMaintenanceService.comparePlanningMaintenance(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ intervention }) => {
      this.intervention = intervention;
      if (intervention) {
        this.updateForm(intervention);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const intervention = this.interventionFormService.getIntervention(this.editForm);
    if (intervention.id === null) {
      this.subscribeToSaveResponse(this.interventionService.create(intervention));
    } else {
      this.subscribeToSaveResponse(this.interventionService.update(intervention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IIntervention | null>): void {
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

  protected updateForm(intervention: IIntervention): void {
    this.intervention = intervention;
    this.interventionFormService.resetForm(this.editForm, intervention);

    this.pannesSharedCollection.update(pannes => this.panneService.addPanneToCollectionIfMissing<IPanne>(pannes, intervention.panne));
    this.planningMaintenancesSharedCollection.update(planningMaintenances =>
      this.planningMaintenanceService.addPlanningMaintenanceToCollectionIfMissing<IPlanningMaintenance>(
        planningMaintenances,
        ...(intervention.plannings ?? []),
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.panneService
      .query()
      .pipe(map((res: HttpResponse<IPanne[]>) => res.body ?? []))
      .pipe(map((pannes: IPanne[]) => this.panneService.addPanneToCollectionIfMissing<IPanne>(pannes, this.intervention?.panne)))
      .subscribe((pannes: IPanne[]) => this.pannesSharedCollection.set(pannes));

    this.planningMaintenanceService
      .query()
      .pipe(map((res: HttpResponse<IPlanningMaintenance[]>) => res.body ?? []))
      .pipe(
        map((planningMaintenances: IPlanningMaintenance[]) =>
          this.planningMaintenanceService.addPlanningMaintenanceToCollectionIfMissing<IPlanningMaintenance>(
            planningMaintenances,
            ...(this.intervention?.plannings ?? []),
          ),
        ),
      )
      .subscribe((planningMaintenances: IPlanningMaintenance[]) => this.planningMaintenancesSharedCollection.set(planningMaintenances));
  }
}
