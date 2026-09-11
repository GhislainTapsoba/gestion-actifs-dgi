import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IIntervention } from 'app/entities/intervention/intervention.model';
import { InterventionService } from 'app/entities/intervention/service/intervention.service';
import { IPlanningMaintenance } from '../planning-maintenance.model';
import { PlanningMaintenanceService } from '../service/planning-maintenance.service';

import { PlanningMaintenanceFormService } from './planning-maintenance-form.service';
import { PlanningMaintenanceUpdate } from './planning-maintenance-update';

describe('PlanningMaintenance Management Update Component', () => {
  let comp: PlanningMaintenanceUpdate;
  let fixture: ComponentFixture<PlanningMaintenanceUpdate>;
  let activatedRoute: ActivatedRoute;
  let planningMaintenanceFormService: PlanningMaintenanceFormService;
  let planningMaintenanceService: PlanningMaintenanceService;
  let interventionService: InterventionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    });

    fixture = TestBed.createComponent(PlanningMaintenanceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    planningMaintenanceFormService = TestBed.inject(PlanningMaintenanceFormService);
    planningMaintenanceService = TestBed.inject(PlanningMaintenanceService);
    interventionService = TestBed.inject(InterventionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Intervention query and add missing value', () => {
      const planningMaintenance: IPlanningMaintenance = { id: 8418 };
      const interventions: IIntervention[] = [{ id: 13583 }];
      planningMaintenance.interventions = interventions;

      const interventionCollection: IIntervention[] = [{ id: 13583 }];
      vi.spyOn(interventionService, 'query').mockReturnValue(of(new HttpResponse({ body: interventionCollection })));
      const additionalInterventions = [...interventions];
      const expectedCollection: IIntervention[] = [...additionalInterventions, ...interventionCollection];
      vi.spyOn(interventionService, 'addInterventionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planningMaintenance });
      comp.ngOnInit();

      expect(interventionService.query).toHaveBeenCalled();
      expect(interventionService.addInterventionToCollectionIfMissing).toHaveBeenCalledWith(
        interventionCollection,
        ...additionalInterventions.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.interventionsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const planningMaintenance: IPlanningMaintenance = { id: 8418 };
      const intervention: IIntervention = { id: 13583 };
      planningMaintenance.interventions = [intervention];

      activatedRoute.data = of({ planningMaintenance });
      comp.ngOnInit();

      expect(comp.interventionsSharedCollection()).toContainEqual(intervention);
      expect(comp.planningMaintenance).toEqual(planningMaintenance);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPlanningMaintenance>();
      const planningMaintenance = { id: 20630 };
      vi.spyOn(planningMaintenanceFormService, 'getPlanningMaintenance').mockReturnValue(planningMaintenance);
      vi.spyOn(planningMaintenanceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planningMaintenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(planningMaintenance);
      saveSubject.complete();

      // THEN
      expect(planningMaintenanceFormService.getPlanningMaintenance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(planningMaintenanceService.update).toHaveBeenCalledWith(expect.objectContaining(planningMaintenance));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPlanningMaintenance>();
      const planningMaintenance = { id: 20630 };
      vi.spyOn(planningMaintenanceFormService, 'getPlanningMaintenance').mockReturnValue({ id: null });
      vi.spyOn(planningMaintenanceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planningMaintenance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(planningMaintenance);
      saveSubject.complete();

      // THEN
      expect(planningMaintenanceFormService.getPlanningMaintenance).toHaveBeenCalled();
      expect(planningMaintenanceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPlanningMaintenance>();
      const planningMaintenance = { id: 20630 };
      vi.spyOn(planningMaintenanceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planningMaintenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(planningMaintenanceService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareIntervention', () => {
      it('should forward to interventionService', () => {
        const entity = { id: 13583 };
        const entity2 = { id: 29837 };
        vi.spyOn(interventionService, 'compareIntervention');
        comp.compareIntervention(entity, entity2);
        expect(interventionService.compareIntervention).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
