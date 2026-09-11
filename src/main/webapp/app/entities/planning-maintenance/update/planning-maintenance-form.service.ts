import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPlanningMaintenance, NewPlanningMaintenance } from '../planning-maintenance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlanningMaintenance for edit and NewPlanningMaintenanceFormGroupInput for create.
 */
type PlanningMaintenanceFormGroupInput = IPlanningMaintenance | PartialWithRequiredKeyOf<NewPlanningMaintenance>;

type PlanningMaintenanceFormDefaults = Pick<NewPlanningMaintenance, 'id' | 'interventions'>;

type PlanningMaintenanceFormGroupContent = {
  id: FormControl<IPlanningMaintenance['id'] | NewPlanningMaintenance['id']>;
  datePrevue: FormControl<IPlanningMaintenance['datePrevue']>;
  periodicite: FormControl<IPlanningMaintenance['periodicite']>;
  statut: FormControl<IPlanningMaintenance['statut']>;
  description: FormControl<IPlanningMaintenance['description']>;
  interventions: FormControl<IPlanningMaintenance['interventions']>;
};

export type PlanningMaintenanceFormGroup = FormGroup<PlanningMaintenanceFormGroupContent>;

@Service()
export class PlanningMaintenanceFormService {
  createPlanningMaintenanceFormGroup(planningMaintenance?: PlanningMaintenanceFormGroupInput): PlanningMaintenanceFormGroup {
    const planningMaintenanceRawValue = {
      ...this.getFormDefaults(),
      ...(planningMaintenance ?? { id: null }),
    };

    return new FormGroup<PlanningMaintenanceFormGroupContent>({
      id: new FormControl(
        { value: planningMaintenanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      datePrevue: new FormControl(planningMaintenanceRawValue.datePrevue, {
        validators: [Validators.required],
      }),
      periodicite: new FormControl(planningMaintenanceRawValue.periodicite),
      statut: new FormControl(planningMaintenanceRawValue.statut, {
        validators: [Validators.required],
      }),
      description: new FormControl(planningMaintenanceRawValue.description),
      interventions: new FormControl(planningMaintenanceRawValue.interventions ?? []),
    });
  }

  getPlanningMaintenance(form: PlanningMaintenanceFormGroup): IPlanningMaintenance | NewPlanningMaintenance {
    return form.getRawValue();
  }

  resetForm(form: PlanningMaintenanceFormGroup, planningMaintenance: PlanningMaintenanceFormGroupInput): void {
    const planningMaintenanceRawValue = { ...this.getFormDefaults(), ...planningMaintenance };
    form.reset({
      ...planningMaintenanceRawValue,
      id: { value: planningMaintenanceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PlanningMaintenanceFormDefaults {
    return {
      id: null,
      interventions: [],
    };
  }
}
