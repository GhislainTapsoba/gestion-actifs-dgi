import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAffectation, NewAffectation } from '../affectation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAffectation for edit and NewAffectationFormGroupInput for create.
 */
type AffectationFormGroupInput = IAffectation | PartialWithRequiredKeyOf<NewAffectation>;

type AffectationFormDefaults = Pick<NewAffectation, 'id'>;

type AffectationFormGroupContent = {
  id: FormControl<IAffectation['id'] | NewAffectation['id']>;
  dateAffectation: FormControl<IAffectation['dateAffectation']>;
  motif: FormControl<IAffectation['motif']>;
  dateRestitution: FormControl<IAffectation['dateRestitution']>;
  agent: FormControl<IAffectation['agent']>;
};

export type AffectationFormGroup = FormGroup<AffectationFormGroupContent>;

@Service()
export class AffectationFormService {
  createAffectationFormGroup(affectation?: AffectationFormGroupInput): AffectationFormGroup {
    const affectationRawValue = {
      ...this.getFormDefaults(),
      ...(affectation ?? { id: null }),
    };

    return new FormGroup<AffectationFormGroupContent>({
      id: new FormControl(
        { value: affectationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateAffectation: new FormControl(affectationRawValue.dateAffectation, {
        validators: [Validators.required],
      }),
      motif: new FormControl(affectationRawValue.motif),
      dateRestitution: new FormControl(affectationRawValue.dateRestitution),
      agent: new FormControl(affectationRawValue.agent, {
        validators: [Validators.required],
      }),
    });
  }

  getAffectation(form: AffectationFormGroup): IAffectation | NewAffectation {
    return form.getRawValue();
  }

  resetForm(form: AffectationFormGroup, affectation: AffectationFormGroupInput): void {
    const affectationRawValue = { ...this.getFormDefaults(), ...affectation };
    form.reset({
      ...affectationRawValue,
      id: { value: affectationRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AffectationFormDefaults {
    return {
      id: null,
    };
  }
}
