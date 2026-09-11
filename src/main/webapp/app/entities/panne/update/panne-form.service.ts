import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPanne, NewPanne } from '../panne.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPanne for edit and NewPanneFormGroupInput for create.
 */
type PanneFormGroupInput = IPanne | PartialWithRequiredKeyOf<NewPanne>;

type PanneFormDefaults = Pick<NewPanne, 'id'>;

type PanneFormGroupContent = {
  id: FormControl<IPanne['id'] | NewPanne['id']>;
  description: FormControl<IPanne['description']>;
  dateDeclaration: FormControl<IPanne['dateDeclaration']>;
  statutPanne: FormControl<IPanne['statutPanne']>;
  actif: FormControl<IPanne['actif']>;
};

export type PanneFormGroup = FormGroup<PanneFormGroupContent>;

@Service()
export class PanneFormService {
  createPanneFormGroup(panne?: PanneFormGroupInput): PanneFormGroup {
    const panneRawValue = {
      ...this.getFormDefaults(),
      ...(panne ?? { id: null }),
    };

    return new FormGroup<PanneFormGroupContent>({
      id: new FormControl(
        { value: panneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(panneRawValue.description, {
        validators: [Validators.required],
      }),
      dateDeclaration: new FormControl(panneRawValue.dateDeclaration, {
        validators: [Validators.required],
      }),
      statutPanne: new FormControl(panneRawValue.statutPanne, {
        validators: [Validators.required],
      }),
      actif: new FormControl(panneRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getPanne(form: PanneFormGroup): IPanne | NewPanne {
    return form.getRawValue();
  }

  resetForm(form: PanneFormGroup, panne: PanneFormGroupInput): void {
    const panneRawValue = { ...this.getFormDefaults(), ...panne };
    form.reset({
      ...panneRawValue,
      id: { value: panneRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): PanneFormDefaults {
    return {
      id: null,
    };
  }
}
