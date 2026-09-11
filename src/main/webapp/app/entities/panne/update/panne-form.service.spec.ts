import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../panne.test-samples';

import { PanneFormService } from './panne-form.service';

describe('Panne Form Service', () => {
  let service: PanneFormService;

  beforeEach(() => {
    service = TestBed.inject(PanneFormService);
  });

  describe('Service methods', () => {
    describe('createPanneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPanneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            dateDeclaration: expect.any(Object),
            statutPanne: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing IPanne should create a new form with FormGroup', () => {
        const formGroup = service.createPanneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            description: expect.any(Object),
            dateDeclaration: expect.any(Object),
            statutPanne: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getPanne', () => {
      it('should return NewPanne for default Panne initial value', () => {
        const formGroup = service.createPanneFormGroup(sampleWithNewData);

        const panne = service.getPanne(formGroup);

        expect(panne).toMatchObject(sampleWithNewData);
      });

      it('should return NewPanne for empty Panne initial value', () => {
        const formGroup = service.createPanneFormGroup();

        const panne = service.getPanne(formGroup);

        expect(panne).toMatchObject({});
      });

      it('should return IPanne', () => {
        const formGroup = service.createPanneFormGroup(sampleWithRequiredData);

        const panne = service.getPanne(formGroup);

        expect(panne).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPanne should not enable id FormControl', () => {
        const formGroup = service.createPanneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPanne should disable id FormControl', () => {
        const formGroup = service.createPanneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
