import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../affectation.test-samples';

import { AffectationFormService } from './affectation-form.service';

describe('Affectation Form Service', () => {
  let service: AffectationFormService;

  beforeEach(() => {
    service = TestBed.inject(AffectationFormService);
  });

  describe('Service methods', () => {
    describe('createAffectationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAffectationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAffectation: expect.any(Object),
            motif: expect.any(Object),
            dateRestitution: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });

      it('passing IAffectation should create a new form with FormGroup', () => {
        const formGroup = service.createAffectationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateAffectation: expect.any(Object),
            motif: expect.any(Object),
            dateRestitution: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });
    });

    describe('getAffectation', () => {
      it('should return NewAffectation for default Affectation initial value', () => {
        const formGroup = service.createAffectationFormGroup(sampleWithNewData);

        const affectation = service.getAffectation(formGroup);

        expect(affectation).toMatchObject(sampleWithNewData);
      });

      it('should return NewAffectation for empty Affectation initial value', () => {
        const formGroup = service.createAffectationFormGroup();

        const affectation = service.getAffectation(formGroup);

        expect(affectation).toMatchObject({});
      });

      it('should return IAffectation', () => {
        const formGroup = service.createAffectationFormGroup(sampleWithRequiredData);

        const affectation = service.getAffectation(formGroup);

        expect(affectation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAffectation should not enable id FormControl', () => {
        const formGroup = service.createAffectationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAffectation should disable id FormControl', () => {
        const formGroup = service.createAffectationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
