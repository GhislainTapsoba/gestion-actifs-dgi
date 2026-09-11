import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAffectationActif, NewAffectationActif } from '../affectation-actif.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAffectationActif for edit and NewAffectationActifFormGroupInput for create.
 */
type AffectationActifFormGroupInput = IAffectationActif | PartialWithRequiredKeyOf<NewAffectationActif>;

type AffectationActifFormDefaults = Pick<NewAffectationActif, 'id'>;

type AffectationActifFormGroupContent = {
  id: FormControl<IAffectationActif['id'] | NewAffectationActif['id']>;
  observation: FormControl<IAffectationActif['observation']>;
  statut: FormControl<IAffectationActif['statut']>;
  affectation: FormControl<IAffectationActif['affectation']>;
  actif: FormControl<IAffectationActif['actif']>;
};

export type AffectationActifFormGroup = FormGroup<AffectationActifFormGroupContent>;

@Service()
export class AffectationActifFormService {
  createAffectationActifFormGroup(affectationActif?: AffectationActifFormGroupInput): AffectationActifFormGroup {
    const affectationActifRawValue = {
      ...this.getFormDefaults(),
      ...(affectationActif ?? { id: null }),
    };

    return new FormGroup<AffectationActifFormGroupContent>({
      id: new FormControl(
        { value: affectationActifRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      observation: new FormControl(affectationActifRawValue.observation),
      statut: new FormControl(affectationActifRawValue.statut, {
        validators: [Validators.required],
      }),
      affectation: new FormControl(affectationActifRawValue.affectation, {
        validators: [Validators.required],
      }),
      actif: new FormControl(affectationActifRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getAffectationActif(form: AffectationActifFormGroup): IAffectationActif | NewAffectationActif {
    return form.getRawValue();
  }

  resetForm(form: AffectationActifFormGroup, affectationActif: AffectationActifFormGroupInput): void {
    const affectationActifRawValue = { ...this.getFormDefaults(), ...affectationActif };
    form.reset({
      ...affectationActifRawValue,
      id: { value: affectationActifRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): AffectationActifFormDefaults {
    return {
      id: null,
    };
  }
}
