import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../categorie-materiel.test-samples';

import { CategorieMaterielFormService } from './categorie-materiel-form.service';

describe('CategorieMateriel Form Service', () => {
  let service: CategorieMaterielFormService;

  beforeEach(() => {
    service = TestBed.inject(CategorieMaterielFormService);
  });

  describe('Service methods', () => {
    describe('createCategorieMaterielFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCategorieMaterielFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });

      it('passing ICategorieMateriel should create a new form with FormGroup', () => {
        const formGroup = service.createCategorieMaterielFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelle: expect.any(Object),
            description: expect.any(Object),
          }),
        );
      });
    });

    describe('getCategorieMateriel', () => {
      it('should return NewCategorieMateriel for default CategorieMateriel initial value', () => {
        const formGroup = service.createCategorieMaterielFormGroup(sampleWithNewData);

        const categorieMateriel = service.getCategorieMateriel(formGroup);

        expect(categorieMateriel).toMatchObject(sampleWithNewData);
      });

      it('should return NewCategorieMateriel for empty CategorieMateriel initial value', () => {
        const formGroup = service.createCategorieMaterielFormGroup();

        const categorieMateriel = service.getCategorieMateriel(formGroup);

        expect(categorieMateriel).toMatchObject({});
      });

      it('should return ICategorieMateriel', () => {
        const formGroup = service.createCategorieMaterielFormGroup(sampleWithRequiredData);

        const categorieMateriel = service.getCategorieMateriel(formGroup);

        expect(categorieMateriel).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICategorieMateriel should not enable id FormControl', () => {
        const formGroup = service.createCategorieMaterielFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCategorieMateriel should disable id FormControl', () => {
        const formGroup = service.createCategorieMaterielFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
