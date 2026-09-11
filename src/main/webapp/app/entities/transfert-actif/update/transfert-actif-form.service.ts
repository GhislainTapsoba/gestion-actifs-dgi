import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITransfertActif, NewTransfertActif } from '../transfert-actif.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransfertActif for edit and NewTransfertActifFormGroupInput for create.
 */
type TransfertActifFormGroupInput = ITransfertActif | PartialWithRequiredKeyOf<NewTransfertActif>;

type TransfertActifFormDefaults = Pick<NewTransfertActif, 'id'>;

type TransfertActifFormGroupContent = {
  id: FormControl<ITransfertActif['id'] | NewTransfertActif['id']>;
  observation: FormControl<ITransfertActif['observation']>;
  transfert: FormControl<ITransfertActif['transfert']>;
  actif: FormControl<ITransfertActif['actif']>;
};

export type TransfertActifFormGroup = FormGroup<TransfertActifFormGroupContent>;

@Service()
export class TransfertActifFormService {
  createTransfertActifFormGroup(transfertActif?: TransfertActifFormGroupInput): TransfertActifFormGroup {
    const transfertActifRawValue = {
      ...this.getFormDefaults(),
      ...(transfertActif ?? { id: null }),
    };

    return new FormGroup<TransfertActifFormGroupContent>({
      id: new FormControl(
        { value: transfertActifRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      observation: new FormControl(transfertActifRawValue.observation),
      transfert: new FormControl(transfertActifRawValue.transfert, {
        validators: [Validators.required],
      }),
      actif: new FormControl(transfertActifRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getTransfertActif(form: TransfertActifFormGroup): ITransfertActif | NewTransfertActif {
    return form.getRawValue();
  }

  resetForm(form: TransfertActifFormGroup, transfertActif: TransfertActifFormGroupInput): void {
    const transfertActifRawValue = { ...this.getFormDefaults(), ...transfertActif };
    form.reset({
      ...transfertActifRawValue,
      id: { value: transfertActifRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TransfertActifFormDefaults {
    return {
      id: null,
    };
  }
}
