import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITransfert, NewTransfert } from '../transfert.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITransfert for edit and NewTransfertFormGroupInput for create.
 */
type TransfertFormGroupInput = ITransfert | PartialWithRequiredKeyOf<NewTransfert>;

type TransfertFormDefaults = Pick<NewTransfert, 'id'>;

type TransfertFormGroupContent = {
  id: FormControl<ITransfert['id'] | NewTransfert['id']>;
  dateTransfert: FormControl<ITransfert['dateTransfert']>;
  statut: FormControl<ITransfert['statut']>;
  commentaireRejet: FormControl<ITransfert['commentaireRejet']>;
  dateTraitement: FormControl<ITransfert['dateTraitement']>;
  serviceOrigine: FormControl<ITransfert['serviceOrigine']>;
  serviceDestinataire: FormControl<ITransfert['serviceDestinataire']>;
  demandeur: FormControl<ITransfert['demandeur']>;
  validateur: FormControl<ITransfert['validateur']>;
};

export type TransfertFormGroup = FormGroup<TransfertFormGroupContent>;

@Service()
export class TransfertFormService {
  createTransfertFormGroup(transfert?: TransfertFormGroupInput): TransfertFormGroup {
    const transfertRawValue = {
      ...this.getFormDefaults(),
      ...(transfert ?? { id: null }),
    };

    return new FormGroup<TransfertFormGroupContent>({
      id: new FormControl(
        { value: transfertRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateTransfert: new FormControl(transfertRawValue.dateTransfert, {
        validators: [Validators.required],
      }),
      statut: new FormControl(transfertRawValue.statut, {
        validators: [Validators.required],
      }),
      commentaireRejet: new FormControl(transfertRawValue.commentaireRejet),
      dateTraitement: new FormControl(transfertRawValue.dateTraitement),
      serviceOrigine: new FormControl(transfertRawValue.serviceOrigine, {
        validators: [Validators.required],
      }),
      serviceDestinataire: new FormControl(transfertRawValue.serviceDestinataire, {
        validators: [Validators.required],
      }),
      demandeur: new FormControl(transfertRawValue.demandeur),
      validateur: new FormControl(transfertRawValue.validateur),
    });
  }

  getTransfert(form: TransfertFormGroup): ITransfert | NewTransfert {
    return form.getRawValue();
  }

  resetForm(form: TransfertFormGroup, transfert: TransfertFormGroupInput): void {
    const transfertRawValue = { ...this.getFormDefaults(), ...transfert };
    form.reset({
      ...transfertRawValue,
      id: { value: transfertRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): TransfertFormDefaults {
    return {
      id: null,
    };
  }
}
