import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICategorieMateriel, NewCategorieMateriel } from '../categorie-materiel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICategorieMateriel for edit and NewCategorieMaterielFormGroupInput for create.
 */
type CategorieMaterielFormGroupInput = ICategorieMateriel | PartialWithRequiredKeyOf<NewCategorieMateriel>;

type CategorieMaterielFormDefaults = Pick<NewCategorieMateriel, 'id'>;

type CategorieMaterielFormGroupContent = {
  id: FormControl<ICategorieMateriel['id'] | NewCategorieMateriel['id']>;
  libelle: FormControl<ICategorieMateriel['libelle']>;
  description: FormControl<ICategorieMateriel['description']>;
};

export type CategorieMaterielFormGroup = FormGroup<CategorieMaterielFormGroupContent>;

@Service()
export class CategorieMaterielFormService {
  createCategorieMaterielFormGroup(categorieMateriel?: CategorieMaterielFormGroupInput): CategorieMaterielFormGroup {
    const categorieMaterielRawValue = {
      ...this.getFormDefaults(),
      ...(categorieMateriel ?? { id: null }),
    };

    return new FormGroup<CategorieMaterielFormGroupContent>({
      id: new FormControl(
        { value: categorieMaterielRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelle: new FormControl(categorieMaterielRawValue.libelle, {
        validators: [Validators.required],
      }),
      description: new FormControl(categorieMaterielRawValue.description),
    });
  }

  getCategorieMateriel(form: CategorieMaterielFormGroup): ICategorieMateriel | NewCategorieMateriel {
    return form.getRawValue();
  }

  resetForm(form: CategorieMaterielFormGroup, categorieMateriel: CategorieMaterielFormGroupInput): void {
    const categorieMaterielRawValue = { ...this.getFormDefaults(), ...categorieMateriel };
    form.reset({
      ...categorieMaterielRawValue,
      id: { value: categorieMaterielRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): CategorieMaterielFormDefaults {
    return {
      id: null,
    };
  }
}
