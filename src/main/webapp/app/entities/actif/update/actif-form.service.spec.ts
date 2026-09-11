import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../actif.test-samples';

import { ActifFormService } from './actif-form.service';

describe('Actif Form Service', () => {
  let service: ActifFormService;

  beforeEach(() => {
    service = TestBed.inject(ActifFormService);
  });

  describe('Service methods', () => {
    describe('createActifFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createActifFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeInventaire: expect.any(Object),
            designation: expect.any(Object),
            marque: expect.any(Object),
            modele: expect.any(Object),
            numeroSerie: expect.any(Object),
            codeBarre: expect.any(Object),
            type: expect.any(Object),
            etat: expect.any(Object),
            localisation: expect.any(Object),
            dateAcquisition: expect.any(Object),
            valeurAcquisition: expect.any(Object),
            categorie: expect.any(Object),
          }),
        );
      });

      it('passing IActif should create a new form with FormGroup', () => {
        const formGroup = service.createActifFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeInventaire: expect.any(Object),
            designation: expect.any(Object),
            marque: expect.any(Object),
            modele: expect.any(Object),
            numeroSerie: expect.any(Object),
            codeBarre: expect.any(Object),
            type: expect.any(Object),
            etat: expect.any(Object),
            localisation: expect.any(Object),
            dateAcquisition: expect.any(Object),
            valeurAcquisition: expect.any(Object),
            categorie: expect.any(Object),
          }),
        );
      });
    });

    describe('getActif', () => {
      it('should return NewActif for default Actif initial value', () => {
        const formGroup = service.createActifFormGroup(sampleWithNewData);

        const actif = service.getActif(formGroup);

        expect(actif).toMatchObject(sampleWithNewData);
      });

      it('should return NewActif for empty Actif initial value', () => {
        const formGroup = service.createActifFormGroup();

        const actif = service.getActif(formGroup);

        expect(actif).toMatchObject({});
      });

      it('should return IActif', () => {
        const formGroup = service.createActifFormGroup(sampleWithRequiredData);

        const actif = service.getActif(formGroup);

        expect(actif).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IActif should not enable id FormControl', () => {
        const formGroup = service.createActifFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewActif should disable id FormControl', () => {
        const formGroup = service.createActifFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
