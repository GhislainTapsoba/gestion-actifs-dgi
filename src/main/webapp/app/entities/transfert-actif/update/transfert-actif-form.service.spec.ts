import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transfert-actif.test-samples';

import { TransfertActifFormService } from './transfert-actif-form.service';

describe('TransfertActif Form Service', () => {
  let service: TransfertActifFormService;

  beforeEach(() => {
    service = TestBed.inject(TransfertActifFormService);
  });

  describe('Service methods', () => {
    describe('createTransfertActifFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransfertActifFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            observation: expect.any(Object),
            transfert: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing ITransfertActif should create a new form with FormGroup', () => {
        const formGroup = service.createTransfertActifFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            observation: expect.any(Object),
            transfert: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransfertActif', () => {
      it('should return NewTransfertActif for default TransfertActif initial value', () => {
        const formGroup = service.createTransfertActifFormGroup(sampleWithNewData);

        const transfertActif = service.getTransfertActif(formGroup);

        expect(transfertActif).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransfertActif for empty TransfertActif initial value', () => {
        const formGroup = service.createTransfertActifFormGroup();

        const transfertActif = service.getTransfertActif(formGroup);

        expect(transfertActif).toMatchObject({});
      });

      it('should return ITransfertActif', () => {
        const formGroup = service.createTransfertActifFormGroup(sampleWithRequiredData);

        const transfertActif = service.getTransfertActif(formGroup);

        expect(transfertActif).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransfertActif should not enable id FormControl', () => {
        const formGroup = service.createTransfertActifFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransfertActif should disable id FormControl', () => {
        const formGroup = service.createTransfertActifFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
