import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../transfert.test-samples';

import { TransfertFormService } from './transfert-form.service';

describe('Transfert Form Service', () => {
  let service: TransfertFormService;

  beforeEach(() => {
    service = TestBed.inject(TransfertFormService);
  });

  describe('Service methods', () => {
    describe('createTransfertFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTransfertFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateTransfert: expect.any(Object),
            statut: expect.any(Object),
            commentaireRejet: expect.any(Object),
            dateTraitement: expect.any(Object),
            serviceOrigine: expect.any(Object),
            serviceDestinataire: expect.any(Object),
            demandeur: expect.any(Object),
            validateur: expect.any(Object),
          }),
        );
      });

      it('passing ITransfert should create a new form with FormGroup', () => {
        const formGroup = service.createTransfertFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateTransfert: expect.any(Object),
            statut: expect.any(Object),
            commentaireRejet: expect.any(Object),
            dateTraitement: expect.any(Object),
            serviceOrigine: expect.any(Object),
            serviceDestinataire: expect.any(Object),
            demandeur: expect.any(Object),
            validateur: expect.any(Object),
          }),
        );
      });
    });

    describe('getTransfert', () => {
      it('should return NewTransfert for default Transfert initial value', () => {
        const formGroup = service.createTransfertFormGroup(sampleWithNewData);

        const transfert = service.getTransfert(formGroup);

        expect(transfert).toMatchObject(sampleWithNewData);
      });

      it('should return NewTransfert for empty Transfert initial value', () => {
        const formGroup = service.createTransfertFormGroup();

        const transfert = service.getTransfert(formGroup);

        expect(transfert).toMatchObject({});
      });

      it('should return ITransfert', () => {
        const formGroup = service.createTransfertFormGroup(sampleWithRequiredData);

        const transfert = service.getTransfert(formGroup);

        expect(transfert).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITransfert should not enable id FormControl', () => {
        const formGroup = service.createTransfertFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTransfert should disable id FormControl', () => {
        const formGroup = service.createTransfertFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
