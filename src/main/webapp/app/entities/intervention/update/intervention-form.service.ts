import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IIntervention, NewIntervention } from '../intervention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IIntervention for edit and NewInterventionFormGroupInput for create.
 */
type InterventionFormGroupInput = IIntervention | PartialWithRequiredKeyOf<NewIntervention>;

type InterventionFormDefaults = Pick<NewIntervention, 'id' | 'plannings'>;

type InterventionFormGroupContent = {
  id: FormControl<IIntervention['id'] | NewIntervention['id']>;
  dateDeclaration: FormControl<IIntervention['dateDeclaration']>;
  typeIntervention: FormControl<IIntervention['typeIntervention']>;
  statut: FormControl<IIntervention['statut']>;
  description: FormControl<IIntervention['description']>;
  panne: FormControl<IIntervention['panne']>;
  plannings: FormControl<IIntervention['plannings']>;
};

export type InterventionFormGroup = FormGroup<InterventionFormGroupContent>;

@Service()
export class InterventionFormService {
  createInterventionFormGroup(intervention?: InterventionFormGroupInput): InterventionFormGroup {
    const interventionRawValue = {
      ...this.getFormDefaults(),
      ...(intervention ?? { id: null }),
    };

    return new FormGroup<InterventionFormGroupContent>({
      id: new FormControl(
        { value: interventionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDeclaration: new FormControl(interventionRawValue.dateDeclaration, {
        validators: [Validators.required],
      }),
      typeIntervention: new FormControl(interventionRawValue.typeIntervention, {
        validators: [Validators.required],
      }),
      statut: new FormControl(interventionRawValue.statut, {
        validators: [Validators.required],
      }),
      description: new FormControl(interventionRawValue.description),
      panne: new FormControl(interventionRawValue.panne, {
        validators: [Validators.required],
      }),
      plannings: new FormControl(interventionRawValue.plannings ?? []),
    });
  }

  getIntervention(form: InterventionFormGroup): IIntervention | NewIntervention {
    return form.getRawValue();
  }

  resetForm(form: InterventionFormGroup, intervention: InterventionFormGroupInput): void {
    const interventionRawValue = { ...this.getFormDefaults(), ...intervention };
    form.reset({
      ...interventionRawValue,
      id: { value: interventionRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InterventionFormDefaults {
    return {
      id: null,
      plannings: [],
    };
  }
}
