import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../equipement-recensement.test-samples';

import { EquipementRecensementFormService } from './equipement-recensement-form.service';

describe('EquipementRecensement Form Service', () => {
  let service: EquipementRecensementFormService;

  beforeEach(() => {
    service = TestBed.inject(EquipementRecensementFormService);
  });

  describe('Service methods', () => {
    describe('createEquipementRecensementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEquipementRecensementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etatConstate: expect.any(Object),
            dateConstat: expect.any(Object),
            emplacementConstate: expect.any(Object),
            anomalieConstatee: expect.any(Object),
            recensement: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing IEquipementRecensement should create a new form with FormGroup', () => {
        const formGroup = service.createEquipementRecensementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            etatConstate: expect.any(Object),
            dateConstat: expect.any(Object),
            emplacementConstate: expect.any(Object),
            anomalieConstatee: expect.any(Object),
            recensement: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getEquipementRecensement', () => {
      it('should return NewEquipementRecensement for default EquipementRecensement initial value', () => {
        const formGroup = service.createEquipementRecensementFormGroup(sampleWithNewData);

        const equipementRecensement = service.getEquipementRecensement(formGroup);

        expect(equipementRecensement).toMatchObject(sampleWithNewData);
      });

      it('should return NewEquipementRecensement for empty EquipementRecensement initial value', () => {
        const formGroup = service.createEquipementRecensementFormGroup();

        const equipementRecensement = service.getEquipementRecensement(formGroup);

        expect(equipementRecensement).toMatchObject({});
      });

      it('should return IEquipementRecensement', () => {
        const formGroup = service.createEquipementRecensementFormGroup(sampleWithRequiredData);

        const equipementRecensement = service.getEquipementRecensement(formGroup);

        expect(equipementRecensement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEquipementRecensement should not enable id FormControl', () => {
        const formGroup = service.createEquipementRecensementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEquipementRecensement should disable id FormControl', () => {
        const formGroup = service.createEquipementRecensementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
