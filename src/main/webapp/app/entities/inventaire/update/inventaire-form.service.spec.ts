import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inventaire.test-samples';

import { InventaireFormService } from './inventaire-form.service';

describe('Inventaire Form Service', () => {
  let service: InventaireFormService;

  beforeEach(() => {
    service = TestBed.inject(InventaireFormService);
  });

  describe('Service methods', () => {
    describe('createInventaireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInventaireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomFichier: expect.any(Object),
            dateImport: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing IInventaire should create a new form with FormGroup', () => {
        const formGroup = service.createInventaireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomFichier: expect.any(Object),
            dateImport: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getInventaire', () => {
      it('should return NewInventaire for default Inventaire initial value', () => {
        const formGroup = service.createInventaireFormGroup(sampleWithNewData);

        const inventaire = service.getInventaire(formGroup);

        expect(inventaire).toMatchObject(sampleWithNewData);
      });

      it('should return NewInventaire for empty Inventaire initial value', () => {
        const formGroup = service.createInventaireFormGroup();

        const inventaire = service.getInventaire(formGroup);

        expect(inventaire).toMatchObject({});
      });

      it('should return IInventaire', () => {
        const formGroup = service.createInventaireFormGroup(sampleWithRequiredData);

        const inventaire = service.getInventaire(formGroup);

        expect(inventaire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInventaire should not enable id FormControl', () => {
        const formGroup = service.createInventaireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInventaire should disable id FormControl', () => {
        const formGroup = service.createInventaireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
