import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../maintenance.test-samples';

import { MaintenanceFormService } from './maintenance-form.service';

describe('Maintenance Form Service', () => {
  let service: MaintenanceFormService;

  beforeEach(() => {
    service = TestBed.inject(MaintenanceFormService);
  });

  describe('Service methods', () => {
    describe('createMaintenanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMaintenanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeMaintenance: expect.any(Object),
            datePanne: expect.any(Object),
            statut: expect.any(Object),
            compteRendu: expect.any(Object),
            dateCloture: expect.any(Object),
            actif: expect.any(Object),
            technicien: expect.any(Object),
          }),
        );
      });

      it('passing IMaintenance should create a new form with FormGroup', () => {
        const formGroup = service.createMaintenanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            typeMaintenance: expect.any(Object),
            datePanne: expect.any(Object),
            statut: expect.any(Object),
            compteRendu: expect.any(Object),
            dateCloture: expect.any(Object),
            actif: expect.any(Object),
            technicien: expect.any(Object),
          }),
        );
      });
    });

    describe('getMaintenance', () => {
      it('should return NewMaintenance for default Maintenance initial value', () => {
        const formGroup = service.createMaintenanceFormGroup(sampleWithNewData);

        const maintenance = service.getMaintenance(formGroup);

        expect(maintenance).toMatchObject(sampleWithNewData);
      });

      it('should return NewMaintenance for empty Maintenance initial value', () => {
        const formGroup = service.createMaintenanceFormGroup();

        const maintenance = service.getMaintenance(formGroup);

        expect(maintenance).toMatchObject({});
      });

      it('should return IMaintenance', () => {
        const formGroup = service.createMaintenanceFormGroup(sampleWithRequiredData);

        const maintenance = service.getMaintenance(formGroup);

        expect(maintenance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMaintenance should not enable id FormControl', () => {
        const formGroup = service.createMaintenanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMaintenance should disable id FormControl', () => {
        const formGroup = service.createMaintenanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
