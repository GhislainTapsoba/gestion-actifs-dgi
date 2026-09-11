import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBordereau, NewBordereau } from '../bordereau.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBordereau for edit and NewBordereauFormGroupInput for create.
 */
type BordereauFormGroupInput = IBordereau | PartialWithRequiredKeyOf<NewBordereau>;

type BordereauFormDefaults = Pick<NewBordereau, 'id'>;

type BordereauFormGroupContent = {
  id: FormControl<IBordereau['id'] | NewBordereau['id']>;
  numero: FormControl<IBordereau['numero']>;
  dateEmission: FormControl<IBordereau['dateEmission']>;
  typeBordereau: FormControl<IBordereau['typeBordereau']>;
  statutValidation: FormControl<IBordereau['statutValidation']>;
  dateValidation: FormControl<IBordereau['dateValidation']>;
  transfert: FormControl<IBordereau['transfert']>;
  affectation: FormControl<IBordereau['affectation']>;
  emetteur: FormControl<IBordereau['emetteur']>;
};

export type BordereauFormGroup = FormGroup<BordereauFormGroupContent>;

@Service()
export class BordereauFormService {
  createBordereauFormGroup(bordereau?: BordereauFormGroupInput): BordereauFormGroup {
    const bordereauRawValue = {
      ...this.getFormDefaults(),
      ...(bordereau ?? { id: null }),
    };

    return new FormGroup<BordereauFormGroupContent>({
      id: new FormControl(
        { value: bordereauRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      numero: new FormControl(bordereauRawValue.numero, {
        validators: [Validators.required],
      }),
      dateEmission: new FormControl(bordereauRawValue.dateEmission, {
        validators: [Validators.required],
      }),
      typeBordereau: new FormControl(bordereauRawValue.typeBordereau, {
        validators: [Validators.required],
      }),
      statutValidation: new FormControl(bordereauRawValue.statutValidation, {
        validators: [Validators.required],
      }),
      dateValidation: new FormControl(bordereauRawValue.dateValidation),
      transfert: new FormControl(bordereauRawValue.transfert),
      affectation: new FormControl(bordereauRawValue.affectation),
      emetteur: new FormControl(bordereauRawValue.emetteur, {
        validators: [Validators.required],
      }),
    });
  }

  getBordereau(form: BordereauFormGroup): IBordereau | NewBordereau {
    return form.getRawValue();
  }

  resetForm(form: BordereauFormGroup, bordereau: BordereauFormGroupInput): void {
    const bordereauRawValue = { ...this.getFormDefaults(), ...bordereau };
    form.reset({
      ...bordereauRawValue,
      id: { value: bordereauRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): BordereauFormDefaults {
    return {
      id: null,
    };
  }
}
