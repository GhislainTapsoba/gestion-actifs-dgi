import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRecensement, NewRecensement } from '../recensement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecensement for edit and NewRecensementFormGroupInput for create.
 */
type RecensementFormGroupInput = IRecensement | PartialWithRequiredKeyOf<NewRecensement>;

type RecensementFormDefaults = Pick<NewRecensement, 'id'>;

type RecensementFormGroupContent = {
  id: FormControl<IRecensement['id'] | NewRecensement['id']>;
  dateDebut: FormControl<IRecensement['dateDebut']>;
  dateFin: FormControl<IRecensement['dateFin']>;
  statut: FormControl<IRecensement['statut']>;
};

export type RecensementFormGroup = FormGroup<RecensementFormGroupContent>;

@Service()
export class RecensementFormService {
  createRecensementFormGroup(recensement?: RecensementFormGroupInput): RecensementFormGroup {
    const recensementRawValue = {
      ...this.getFormDefaults(),
      ...(recensement ?? { id: null }),
    };

    return new FormGroup<RecensementFormGroupContent>({
      id: new FormControl(
        { value: recensementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDebut: new FormControl(recensementRawValue.dateDebut, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(recensementRawValue.dateFin),
      statut: new FormControl(recensementRawValue.statut, {
        validators: [Validators.required],
      }),
    });
  }

  getRecensement(form: RecensementFormGroup): IRecensement | NewRecensement {
    return form.getRawValue();
  }

  resetForm(form: RecensementFormGroup, recensement: RecensementFormGroupInput): void {
    const recensementRawValue = { ...this.getFormDefaults(), ...recensement };
    form.reset({
      ...recensementRawValue,
      id: { value: recensementRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): RecensementFormDefaults {
    return {
      id: null,
    };
  }
}
