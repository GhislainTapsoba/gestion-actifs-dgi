import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEquipementRecensement, NewEquipementRecensement } from '../equipement-recensement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEquipementRecensement for edit and NewEquipementRecensementFormGroupInput for create.
 */
type EquipementRecensementFormGroupInput = IEquipementRecensement | PartialWithRequiredKeyOf<NewEquipementRecensement>;

type EquipementRecensementFormDefaults = Pick<NewEquipementRecensement, 'id' | 'anomalieConstatee'>;

type EquipementRecensementFormGroupContent = {
  id: FormControl<IEquipementRecensement['id'] | NewEquipementRecensement['id']>;
  etatConstate: FormControl<IEquipementRecensement['etatConstate']>;
  dateConstat: FormControl<IEquipementRecensement['dateConstat']>;
  emplacementConstate: FormControl<IEquipementRecensement['emplacementConstate']>;
  anomalieConstatee: FormControl<IEquipementRecensement['anomalieConstatee']>;
  recensement: FormControl<IEquipementRecensement['recensement']>;
  actif: FormControl<IEquipementRecensement['actif']>;
};

export type EquipementRecensementFormGroup = FormGroup<EquipementRecensementFormGroupContent>;

@Service()
export class EquipementRecensementFormService {
  createEquipementRecensementFormGroup(equipementRecensement?: EquipementRecensementFormGroupInput): EquipementRecensementFormGroup {
    const equipementRecensementRawValue = {
      ...this.getFormDefaults(),
      ...(equipementRecensement ?? { id: null }),
    };

    return new FormGroup<EquipementRecensementFormGroupContent>({
      id: new FormControl(
        { value: equipementRecensementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      etatConstate: new FormControl(equipementRecensementRawValue.etatConstate, {
        validators: [Validators.required],
      }),
      dateConstat: new FormControl(equipementRecensementRawValue.dateConstat, {
        validators: [Validators.required],
      }),
      emplacementConstate: new FormControl(equipementRecensementRawValue.emplacementConstate),
      anomalieConstatee: new FormControl(equipementRecensementRawValue.anomalieConstatee),
      recensement: new FormControl(equipementRecensementRawValue.recensement, {
        validators: [Validators.required],
      }),
      actif: new FormControl(equipementRecensementRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getEquipementRecensement(form: EquipementRecensementFormGroup): IEquipementRecensement | NewEquipementRecensement {
    return form.getRawValue();
  }

  resetForm(form: EquipementRecensementFormGroup, equipementRecensement: EquipementRecensementFormGroupInput): void {
    const equipementRecensementRawValue = { ...this.getFormDefaults(), ...equipementRecensement };
    form.reset({
      ...equipementRecensementRawValue,
      id: { value: equipementRecensementRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): EquipementRecensementFormDefaults {
    return {
      id: null,
      anomalieConstatee: false,
    };
  }
}
