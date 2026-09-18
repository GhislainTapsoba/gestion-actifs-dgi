import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { IRapport, NewRapport } from '../rapport.model';

type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

type RapportFormGroupInput = IRapport | PartialWithRequiredKeyOf<NewRapport>;

type RapportFormDefaults = Pick<NewRapport, 'id'>;

type RapportFormGroupContent = {
  id: FormControl<IRapport['id'] | NewRapport['id']>;
  titre: FormControl<IRapport['titre']>;
  typeRapport: FormControl<IRapport['typeRapport']>;
  description: FormControl<IRapport['description']>;
  cheminFichier: FormControl<IRapport['cheminFichier']>;
  dateGeneration: FormControl<IRapport['dateGeneration']>;
  generePar: FormControl<IRapport['generePar']>;
  formatExport: FormControl<IRapport['formatExport']>;
  parametres: FormControl<IRapport['parametres']>;
};

export type RapportFormGroup = FormGroup<RapportFormGroupContent>;

@Service()
export class RapportFormService {
  createRapportFormGroup(rapport?: RapportFormGroupInput): RapportFormGroup {
    const rapportRawValue = {
      ...this.getFormDefaults(),
      ...(rapport ?? { id: null }),
    };

    return new FormGroup<RapportFormGroupContent>({
      id: new FormControl(
        { value: rapportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titre: new FormControl(rapportRawValue.titre, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      typeRapport: new FormControl(rapportRawValue.typeRapport ?? 'INVENTAIRE', {
        validators: [Validators.required],
      }),
      description: new FormControl(rapportRawValue.description),
      cheminFichier: new FormControl(rapportRawValue.cheminFichier),
      dateGeneration: new FormControl(rapportRawValue.dateGeneration ?? dayjs()),
      generePar: new FormControl(rapportRawValue.generePar),
      formatExport: new FormControl(rapportRawValue.formatExport ?? 'PDF', {
        validators: [Validators.required],
      }),
      parametres: new FormControl(rapportRawValue.parametres),
    });
  }

  getRapport(form: RapportFormGroup): IRapport | NewRapport {
    return form.getRawValue();
  }

  resetForm(form: RapportFormGroup, rapport: RapportFormGroupInput): void {
    const rapportRawValue = { ...this.getFormDefaults(), ...rapport };
    form.reset({
      ...rapportRawValue,
      id: { value: rapportRawValue.id, disabled: true },
    } as any);
  }

  protected getFormDefaults(): RapportFormDefaults {
    return {
      id: null,
    };
  }
}
