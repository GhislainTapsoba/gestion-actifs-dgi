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
  dateDemande: FormControl<ITransfert['dateDemande']>;
  statut: FormControl<ITransfert['statut']>;
  commentaireRejet: FormControl<ITransfert['commentaireRejet']>;
  dateTraitement: FormControl<ITransfert['dateTraitement']>;
  demandeur: FormControl<ITransfert['demandeur']>;
  validateur: FormControl<ITransfert['validateur']>;
  actif: FormControl<ITransfert['actif']>;
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
      dateDemande: new FormControl(transfertRawValue.dateDemande, {
        validators: [Validators.required],
      }),
      statut: new FormControl(transfertRawValue.statut, {
        validators: [Validators.required],
      }),
      commentaireRejet: new FormControl(transfertRawValue.commentaireRejet),
      dateTraitement: new FormControl(transfertRawValue.dateTraitement),
      demandeur: new FormControl(transfertRawValue.demandeur),
      validateur: new FormControl(transfertRawValue.validateur),
      actif: new FormControl(transfertRawValue.actif, {
        validators: [Validators.required],
      }),
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
