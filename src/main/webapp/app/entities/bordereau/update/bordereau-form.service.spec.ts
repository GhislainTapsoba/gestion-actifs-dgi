import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bordereau.test-samples';

import { BordereauFormService } from './bordereau-form.service';

describe('Bordereau Form Service', () => {
  let service: BordereauFormService;

  beforeEach(() => {
    service = TestBed.inject(BordereauFormService);
  });

  describe('Service methods', () => {
    describe('createBordereauFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBordereauFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            dateEmission: expect.any(Object),
            typeBordereau: expect.any(Object),
            statutValidation: expect.any(Object),
            dateValidation: expect.any(Object),
            transfert: expect.any(Object),
            affectation: expect.any(Object),
            emetteur: expect.any(Object),
          }),
        );
      });

      it('passing IBordereau should create a new form with FormGroup', () => {
        const formGroup = service.createBordereauFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            numero: expect.any(Object),
            dateEmission: expect.any(Object),
            typeBordereau: expect.any(Object),
            statutValidation: expect.any(Object),
            dateValidation: expect.any(Object),
            transfert: expect.any(Object),
            affectation: expect.any(Object),
            emetteur: expect.any(Object),
          }),
        );
      });
    });

    describe('getBordereau', () => {
      it('should return NewBordereau for default Bordereau initial value', () => {
        const formGroup = service.createBordereauFormGroup(sampleWithNewData);

        const bordereau = service.getBordereau(formGroup);

        expect(bordereau).toMatchObject(sampleWithNewData);
      });

      it('should return NewBordereau for empty Bordereau initial value', () => {
        const formGroup = service.createBordereauFormGroup();

        const bordereau = service.getBordereau(formGroup);

        expect(bordereau).toMatchObject({});
      });

      it('should return IBordereau', () => {
        const formGroup = service.createBordereauFormGroup(sampleWithRequiredData);

        const bordereau = service.getBordereau(formGroup);

        expect(bordereau).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBordereau should not enable id FormControl', () => {
        const formGroup = service.createBordereauFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBordereau should disable id FormControl', () => {
        const formGroup = service.createBordereauFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
