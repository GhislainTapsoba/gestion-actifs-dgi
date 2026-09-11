import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { TranslatePipe } from '@ngx-translate/core';
import { Observable, finalize, map } from 'rxjs';

import { StatutPlanning } from 'app/entities/enumerations/statut-planning.model';
import { IIntervention } from 'app/entities/intervention/intervention.model';
import { InterventionService } from 'app/entities/intervention/service/intervention.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IPlanningMaintenance } from '../planning-maintenance.model';
import { PlanningMaintenanceService } from '../service/planning-maintenance.service';

import { PlanningMaintenanceFormGroup, PlanningMaintenanceFormService } from './planning-maintenance-form.service';

@Component({
  selector: 'jhi-planning-maintenance-update',
  templateUrl: './planning-maintenance-update.html',
  imports: [TranslateDirective, TranslatePipe, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class PlanningMaintenanceUpdate implements OnInit {
  readonly isSaving = signal(false);
  planningMaintenance: IPlanningMaintenance | null = null;
  statutPlanningValues = Object.keys(StatutPlanning);

  interventionsSharedCollection = signal<IIntervention[]>([]);

  protected planningMaintenanceService = inject(PlanningMaintenanceService);
  protected planningMaintenanceFormService = inject(PlanningMaintenanceFormService);
  protected interventionService = inject(InterventionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlanningMaintenanceFormGroup = this.planningMaintenanceFormService.createPlanningMaintenanceFormGroup();

  compareIntervention = (o1: IIntervention | null, o2: IIntervention | null): boolean =>
    this.interventionService.compareIntervention(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planningMaintenance }) => {
      this.planningMaintenance = planningMaintenance;
      if (planningMaintenance) {
        this.updateForm(planningMaintenance);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const planningMaintenance = this.planningMaintenanceFormService.getPlanningMaintenance(this.editForm);
    if (planningMaintenance.id === null) {
      this.subscribeToSaveResponse(this.planningMaintenanceService.create(planningMaintenance));
    } else {
      this.subscribeToSaveResponse(this.planningMaintenanceService.update(planningMaintenance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IPlanningMaintenance | null>): void {
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

  protected updateForm(planningMaintenance: IPlanningMaintenance): void {
    this.planningMaintenance = planningMaintenance;
    this.planningMaintenanceFormService.resetForm(this.editForm, planningMaintenance);

    this.interventionsSharedCollection.update(interventions =>
      this.interventionService.addInterventionToCollectionIfMissing<IIntervention>(
        interventions,
        ...(planningMaintenance.interventions ?? []),
      ),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.interventionService
      .query()
      .pipe(map((res: HttpResponse<IIntervention[]>) => res.body ?? []))
      .pipe(
        map((interventions: IIntervention[]) =>
          this.interventionService.addInterventionToCollectionIfMissing<IIntervention>(
            interventions,
            ...(this.planningMaintenance?.interventions ?? []),
          ),
        ),
      )
      .subscribe((interventions: IIntervention[]) => this.interventionsSharedCollection.set(interventions));
  }
}
