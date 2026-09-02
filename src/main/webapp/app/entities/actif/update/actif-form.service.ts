import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IActif, NewActif } from '../actif.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IActif for edit and NewActifFormGroupInput for create.
 */
type ActifFormGroupInput = IActif | PartialWithRequiredKeyOf<NewActif>;

type ActifFormDefaults = Pick<NewActif, 'id'>;

type ActifFormGroupContent = {
  id: FormControl<IActif['id'] | NewActif['id']>;
  identifiantUnique: FormControl<IActif['identifiantUnique']>;
  codeBarreQR: FormControl<IActif['codeBarreQR']>;
  type: FormControl<IActif['type']>;
  etat: FormControl<IActif['etat']>;
  localisation: FormControl<IActif['localisation']>;
  dateAcquisition: FormControl<IActif['dateAcquisition']>;
};

export type ActifFormGroup = FormGroup<ActifFormGroupContent>;

@Service()
export class ActifFormService {
  createActifFormGroup(actif?: ActifFormGroupInput): ActifFormGroup {
    const actifRawValue = {
      ...this.getFormDefaults(),
      ...(actif ?? { id: null }),
    };

    return new FormGroup<ActifFormGroupContent>({
      id: new FormControl(
        { value: actifRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identifiantUnique: new FormControl(actifRawValue.identifiantUnique, {
        validators: [Validators.required],
      }),
      codeBarreQR: new FormControl(actifRawValue.codeBarreQR),
      type: new FormControl(actifRawValue.type, {
        validators: [Validators.required],
      }),
      etat: new FormControl(actifRawValue.etat, {
        validators: [Validators.required],
      }),
      localisation: new FormControl(actifRawValue.localisation),
      dateAcquisition: new FormControl(actifRawValue.dateAcquisition),
    });
  }

  getActif(form: ActifFormGroup): IActif | NewActif {
    return form.getRawValue();
  }

  resetForm(form: ActifFormGroup, actif: ActifFormGroupInput): void {
    const actifRawValue = { ...this.getFormDefaults(), ...actif };
    form.reset({
      ...actifRawValue,
      id: { value: actifRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ActifFormDefaults {
    return {
      id: null,
    };
  }
}
