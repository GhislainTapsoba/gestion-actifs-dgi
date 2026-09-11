import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../service-dgi.test-samples';

import { ServiceDgiFormService } from './service-dgi-form.service';

describe('ServiceDgi Form Service', () => {
  let service: ServiceDgiFormService;

  beforeEach(() => {
    service = TestBed.inject(ServiceDgiFormService);
  });

  describe('Service methods', () => {
    describe('createServiceDgiFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createServiceDgiFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomService: expect.any(Object),
            chefService: expect.any(Object),
          }),
        );
      });

      it('passing IServiceDgi should create a new form with FormGroup', () => {
        const formGroup = service.createServiceDgiFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomService: expect.any(Object),
            chefService: expect.any(Object),
          }),
        );
      });
    });

    describe('getServiceDgi', () => {
      it('should return NewServiceDgi for default ServiceDgi initial value', () => {
        const formGroup = service.createServiceDgiFormGroup(sampleWithNewData);

        const serviceDgi = service.getServiceDgi(formGroup);

        expect(serviceDgi).toMatchObject(sampleWithNewData);
      });

      it('should return NewServiceDgi for empty ServiceDgi initial value', () => {
        const formGroup = service.createServiceDgiFormGroup();

        const serviceDgi = service.getServiceDgi(formGroup);

        expect(serviceDgi).toMatchObject({});
      });

      it('should return IServiceDgi', () => {
        const formGroup = service.createServiceDgiFormGroup(sampleWithRequiredData);

        const serviceDgi = service.getServiceDgi(formGroup);

        expect(serviceDgi).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IServiceDgi should not enable id FormControl', () => {
        const formGroup = service.createServiceDgiFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewServiceDgi should disable id FormControl', () => {
        const formGroup = service.createServiceDgiFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
