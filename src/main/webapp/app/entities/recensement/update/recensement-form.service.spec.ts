import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../recensement.test-samples';

import { RecensementFormService } from './recensement-form.service';

describe('Recensement Form Service', () => {
  let service: RecensementFormService;

  beforeEach(() => {
    service = TestBed.inject(RecensementFormService);
  });

  describe('Service methods', () => {
    describe('createRecensementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createRecensementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            statut: expect.any(Object),
          }),
        );
      });

      it('passing IRecensement should create a new form with FormGroup', () => {
        const formGroup = service.createRecensementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            statut: expect.any(Object),
          }),
        );
      });
    });

    describe('getRecensement', () => {
      it('should return NewRecensement for default Recensement initial value', () => {
        const formGroup = service.createRecensementFormGroup(sampleWithNewData);

        const recensement = service.getRecensement(formGroup);

        expect(recensement).toMatchObject(sampleWithNewData);
      });

      it('should return NewRecensement for empty Recensement initial value', () => {
        const formGroup = service.createRecensementFormGroup();

        const recensement = service.getRecensement(formGroup);

        expect(recensement).toMatchObject({});
      });

      it('should return IRecensement', () => {
        const formGroup = service.createRecensementFormGroup(sampleWithRequiredData);

        const recensement = service.getRecensement(formGroup);

        expect(recensement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IRecensement should not enable id FormControl', () => {
        const formGroup = service.createRecensementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewRecensement should disable id FormControl', () => {
        const formGroup = service.createRecensementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
