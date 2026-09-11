import { Service } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IServiceDgi, NewServiceDgi } from '../service-dgi.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IServiceDgi for edit and NewServiceDgiFormGroupInput for create.
 */
type ServiceDgiFormGroupInput = IServiceDgi | PartialWithRequiredKeyOf<NewServiceDgi>;

type ServiceDgiFormDefaults = Pick<NewServiceDgi, 'id'>;

type ServiceDgiFormGroupContent = {
  id: FormControl<IServiceDgi['id'] | NewServiceDgi['id']>;
  nomService: FormControl<IServiceDgi['nomService']>;
  chefService: FormControl<IServiceDgi['chefService']>;
};

export type ServiceDgiFormGroup = FormGroup<ServiceDgiFormGroupContent>;

@Service()
export class ServiceDgiFormService {
  createServiceDgiFormGroup(serviceDgi?: ServiceDgiFormGroupInput): ServiceDgiFormGroup {
    const serviceDgiRawValue = {
      ...this.getFormDefaults(),
      ...(serviceDgi ?? { id: null }),
    };

    return new FormGroup<ServiceDgiFormGroupContent>({
      id: new FormControl(
        { value: serviceDgiRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomService: new FormControl(serviceDgiRawValue.nomService, {
        validators: [Validators.required],
      }),
      chefService: new FormControl(serviceDgiRawValue.chefService),
    });
  }

  getServiceDgi(form: ServiceDgiFormGroup): IServiceDgi | NewServiceDgi {
    return form.getRawValue();
  }

  resetForm(form: ServiceDgiFormGroup, serviceDgi: ServiceDgiFormGroupInput): void {
    const serviceDgiRawValue = { ...this.getFormDefaults(), ...serviceDgi };
    form.reset({
      ...serviceDgiRawValue,
      id: { value: serviceDgiRawValue.id, disabled: true },
    });
  }

  private getFormDefaults(): ServiceDgiFormDefaults {
    return {
      id: null,
    };
  }
}
