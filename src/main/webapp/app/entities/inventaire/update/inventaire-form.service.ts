import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInventaire, NewInventaire } from '../inventaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInventaire for edit and NewInventaireFormGroupInput for create.
 */
type InventaireFormGroupInput = IInventaire | PartialWithRequiredKeyOf<NewInventaire>;

type InventaireFormDefaults = Pick<NewInventaire, 'id'>;

type InventaireFormGroupContent = {
  id: FormControl<IInventaire['id'] | NewInventaire['id']>;
  nomFichier: FormControl<IInventaire['nomFichier']>;
  dateImport: FormControl<IInventaire['dateImport']>;
  actif: FormControl<IInventaire['actif']>;
};

export type InventaireFormGroup = FormGroup<InventaireFormGroupContent>;

@Service()
export class InventaireFormService {
  createInventaireFormGroup(inventaire?: InventaireFormGroupInput): InventaireFormGroup {
    const inventaireRawValue = {
      ...this.getFormDefaults(),
      ...(inventaire ?? { id: null }),
    };

    return new FormGroup<InventaireFormGroupContent>({
      id: new FormControl(
        { value: inventaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomFichier: new FormControl(inventaireRawValue.nomFichier, {
        validators: [Validators.required],
      }),
      dateImport: new FormControl(inventaireRawValue.dateImport, {
        validators: [Validators.required],
      }),
      actif: new FormControl(inventaireRawValue.actif),
    });
  }

  getInventaire(form: InventaireFormGroup): IInventaire | NewInventaire {
    return form.getRawValue();
  }

  resetForm(form: InventaireFormGroup, inventaire: InventaireFormGroupInput): void {
    const inventaireRawValue = { ...this.getFormDefaults(), ...inventaire };
    form.reset({
      ...inventaireRawValue,
      id: { value: inventaireRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): InventaireFormDefaults {
    return {
      id: null,
    };
  }
}
