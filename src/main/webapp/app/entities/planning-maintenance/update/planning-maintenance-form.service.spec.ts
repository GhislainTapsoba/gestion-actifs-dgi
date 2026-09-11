import { beforeEach, describe, expect, it } from 'vitest';
import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../planning-maintenance.test-samples';

import { PlanningMaintenanceFormService } from './planning-maintenance-form.service';

describe('PlanningMaintenance Form Service', () => {
  let service: PlanningMaintenanceFormService;

  beforeEach(() => {
    service = TestBed.inject(PlanningMaintenanceFormService);
  });

  describe('Service methods', () => {
    describe('createPlanningMaintenanceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datePrevue: expect.any(Object),
            periodicite: expect.any(Object),
            statut: expect.any(Object),
            description: expect.any(Object),
            interventions: expect.any(Object),
          }),
        );
      });

      it('passing IPlanningMaintenance should create a new form with FormGroup', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            datePrevue: expect.any(Object),
            periodicite: expect.any(Object),
            statut: expect.any(Object),
            description: expect.any(Object),
            interventions: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlanningMaintenance', () => {
      it('should return NewPlanningMaintenance for default PlanningMaintenance initial value', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup(sampleWithNewData);

        const planningMaintenance = service.getPlanningMaintenance(formGroup);

        expect(planningMaintenance).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlanningMaintenance for empty PlanningMaintenance initial value', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup();

        const planningMaintenance = service.getPlanningMaintenance(formGroup);

        expect(planningMaintenance).toMatchObject({});
      });

      it('should return IPlanningMaintenance', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup(sampleWithRequiredData);

        const planningMaintenance = service.getPlanningMaintenance(formGroup);

        expect(planningMaintenance).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlanningMaintenance should not enable id FormControl', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlanningMaintenance should disable id FormControl', () => {
        const formGroup = service.createPlanningMaintenanceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
