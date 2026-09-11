import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IMaintenance, NewMaintenance } from '../maintenance.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMaintenance for edit and NewMaintenanceFormGroupInput for create.
 */
type MaintenanceFormGroupInput = IMaintenance | PartialWithRequiredKeyOf<NewMaintenance>;

type MaintenanceFormDefaults = Pick<NewMaintenance, 'id'>;

type MaintenanceFormGroupContent = {
  id: FormControl<IMaintenance['id'] | NewMaintenance['id']>;
  typeMaintenance: FormControl<IMaintenance['typeMaintenance']>;
  datePanne: FormControl<IMaintenance['datePanne']>;
  statut: FormControl<IMaintenance['statut']>;
  compteRendu: FormControl<IMaintenance['compteRendu']>;
  dateCloture: FormControl<IMaintenance['dateCloture']>;
  actif: FormControl<IMaintenance['actif']>;
  technicien: FormControl<IMaintenance['technicien']>;
};

export type MaintenanceFormGroup = FormGroup<MaintenanceFormGroupContent>;

@Service()
export class MaintenanceFormService {
  createMaintenanceFormGroup(maintenance?: MaintenanceFormGroupInput): MaintenanceFormGroup {
    const maintenanceRawValue = {
      ...this.getFormDefaults(),
      ...(maintenance ?? { id: null }),
    };

    return new FormGroup<MaintenanceFormGroupContent>({
      id: new FormControl(
        { value: maintenanceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeMaintenance: new FormControl(maintenanceRawValue.typeMaintenance, {
        validators: [Validators.required],
      }),
      datePanne: new FormControl(maintenanceRawValue.datePanne),
      statut: new FormControl(maintenanceRawValue.statut, {
        validators: [Validators.required],
      }),
      compteRendu: new FormControl(maintenanceRawValue.compteRendu),
      dateCloture: new FormControl(maintenanceRawValue.dateCloture),
      actif: new FormControl(maintenanceRawValue.actif, {
        validators: [Validators.required],
      }),
      technicien: new FormControl(maintenanceRawValue.technicien),
    });
  }

  getMaintenance(form: MaintenanceFormGroup): IMaintenance | NewMaintenance {
    return form.getRawValue();
  }

  resetForm(form: MaintenanceFormGroup, maintenance: MaintenanceFormGroupInput): void {
    const maintenanceRawValue = { ...this.getFormDefaults(), ...maintenance };
    form.reset({
      ...maintenanceRawValue,
      id: { value: maintenanceRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): MaintenanceFormDefaults {
    return {
      id: null,
    };
  }
}
