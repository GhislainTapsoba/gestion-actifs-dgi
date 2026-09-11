import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../intervention.test-samples';

import { InterventionFormService } from './intervention-form.service';

describe('Intervention Form Service', () => {
  let service: InterventionFormService;

  beforeEach(() => {
    service = TestBed.inject(InterventionFormService);
  });

  describe('Service methods', () => {
    describe('createInterventionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInterventionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDeclaration: expect.any(Object),
            typeIntervention: expect.any(Object),
            statut: expect.any(Object),
            description: expect.any(Object),
            panne: expect.any(Object),
            plannings: expect.any(Object),
          }),
        );
      });

      it('passing IIntervention should create a new form with FormGroup', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDeclaration: expect.any(Object),
            typeIntervention: expect.any(Object),
            statut: expect.any(Object),
            description: expect.any(Object),
            panne: expect.any(Object),
            plannings: expect.any(Object),
          }),
        );
      });
    });

    describe('getIntervention', () => {
      it('should return NewIntervention for default Intervention initial value', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithNewData);

        const intervention = service.getIntervention(formGroup);

        expect(intervention).toMatchObject(sampleWithNewData);
      });

      it('should return NewIntervention for empty Intervention initial value', () => {
        const formGroup = service.createInterventionFormGroup();

        const intervention = service.getIntervention(formGroup);

        expect(intervention).toMatchObject({});
      });

      it('should return IIntervention', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithRequiredData);

        const intervention = service.getIntervention(formGroup);

        expect(intervention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IIntervention should not enable id FormControl', () => {
        const formGroup = service.createInterventionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewIntervention should disable id FormControl', () => {
        const formGroup = service.createInterventionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
