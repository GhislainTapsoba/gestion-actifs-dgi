import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../affectation-actif.test-samples';

import { AffectationActifFormService } from './affectation-actif-form.service';

describe('AffectationActif Form Service', () => {
  let service: AffectationActifFormService;

  beforeEach(() => {
    service = TestBed.inject(AffectationActifFormService);
  });

  describe('Service methods', () => {
    describe('createAffectationActifFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAffectationActifFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            observation: expect.any(Object),
            statut: expect.any(Object),
            affectation: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing IAffectationActif should create a new form with FormGroup', () => {
        const formGroup = service.createAffectationActifFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            observation: expect.any(Object),
            statut: expect.any(Object),
            affectation: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getAffectationActif', () => {
      it('should return NewAffectationActif for default AffectationActif initial value', () => {
        const formGroup = service.createAffectationActifFormGroup(sampleWithNewData);

        const affectationActif = service.getAffectationActif(formGroup);

        expect(affectationActif).toMatchObject(sampleWithNewData);
      });

      it('should return NewAffectationActif for empty AffectationActif initial value', () => {
        const formGroup = service.createAffectationActifFormGroup();

        const affectationActif = service.getAffectationActif(formGroup);

        expect(affectationActif).toMatchObject({});
      });

      it('should return IAffectationActif', () => {
        const formGroup = service.createAffectationActifFormGroup(sampleWithRequiredData);

        const affectationActif = service.getAffectationActif(formGroup);

        expect(affectationActif).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAffectationActif should not enable id FormControl', () => {
        const formGroup = service.createAffectationActifFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAffectationActif should disable id FormControl', () => {
        const formGroup = service.createAffectationActifFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
