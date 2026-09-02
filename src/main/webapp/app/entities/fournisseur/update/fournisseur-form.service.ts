import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IFournisseur, NewFournisseur } from '../fournisseur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFournisseur for edit and NewFournisseurFormGroupInput for create.
 */
type FournisseurFormGroupInput = IFournisseur | PartialWithRequiredKeyOf<NewFournisseur>;

type FournisseurFormDefaults = Pick<NewFournisseur, 'id'>;

type FournisseurFormGroupContent = {
  id: FormControl<IFournisseur['id'] | NewFournisseur['id']>;
  nom: FormControl<IFournisseur['nom']>;
  contact: FormControl<IFournisseur['contact']>;
  email: FormControl<IFournisseur['email']>;
  telephone: FormControl<IFournisseur['telephone']>;
};

export type FournisseurFormGroup = FormGroup<FournisseurFormGroupContent>;

@Service()
export class FournisseurFormService {
  createFournisseurFormGroup(fournisseur?: FournisseurFormGroupInput): FournisseurFormGroup {
    const fournisseurRawValue = {
      ...this.getFormDefaults(),
      ...(fournisseur ?? { id: null }),
    };

    return new FormGroup<FournisseurFormGroupContent>({
      id: new FormControl(
        { value: fournisseurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(fournisseurRawValue.nom, {
        validators: [Validators.required],
      }),
      contact: new FormControl(fournisseurRawValue.contact),
      email: new FormControl(fournisseurRawValue.email),
      telephone: new FormControl(fournisseurRawValue.telephone),
    });
  }

  getFournisseur(form: FournisseurFormGroup): IFournisseur | NewFournisseur {
    return form.getRawValue();
  }

  resetForm(form: FournisseurFormGroup, fournisseur: FournisseurFormGroupInput): void {
    const fournisseurRawValue = { ...this.getFormDefaults(), ...fournisseur };
    form.reset({
      ...fournisseurRawValue,
      id: { value: fournisseurRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): FournisseurFormDefaults {
    return {
      id: null,
    };
  }
}
