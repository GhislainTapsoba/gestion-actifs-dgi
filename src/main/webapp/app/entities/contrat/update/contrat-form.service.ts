import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IContrat, NewContrat } from '../contrat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IContrat for edit and NewContratFormGroupInput for create.
 */
type ContratFormGroupInput = IContrat | PartialWithRequiredKeyOf<NewContrat>;

type ContratFormDefaults = Pick<NewContrat, 'id'>;

type ContratFormGroupContent = {
  id: FormControl<IContrat['id'] | NewContrat['id']>;
  typeContrat: FormControl<IContrat['typeContrat']>;
  reference: FormControl<IContrat['reference']>;
  dateDebut: FormControl<IContrat['dateDebut']>;
  dateFin: FormControl<IContrat['dateFin']>;
  actif: FormControl<IContrat['actif']>;
  fournisseur: FormControl<IContrat['fournisseur']>;
};

export type ContratFormGroup = FormGroup<ContratFormGroupContent>;

@Service()
export class ContratFormService {
  createContratFormGroup(contrat?: ContratFormGroupInput): ContratFormGroup {
    const contratRawValue = {
      ...this.getFormDefaults(),
      ...(contrat ?? { id: null }),
    };

    return new FormGroup<ContratFormGroupContent>({
      id: new FormControl(
        { value: contratRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      typeContrat: new FormControl(contratRawValue.typeContrat, {
        validators: [Validators.required],
      }),
      reference: new FormControl(contratRawValue.reference),
      dateDebut: new FormControl(contratRawValue.dateDebut),
      dateFin: new FormControl(contratRawValue.dateFin, {
        validators: [Validators.required],
      }),
      actif: new FormControl(contratRawValue.actif),
      fournisseur: new FormControl(contratRawValue.fournisseur, {
        validators: [Validators.required],
      }),
    });
  }

  getContrat(form: ContratFormGroup): IContrat | NewContrat {
    return form.getRawValue();
  }

  resetForm(form: ContratFormGroup, contrat: ContratFormGroupInput): void {
    const contratRawValue = { ...this.getFormDefaults(), ...contrat };
    form.reset({
      ...contratRawValue,
      id: { value: contratRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ContratFormDefaults {
    return {
      id: null,
    };
  }
}
