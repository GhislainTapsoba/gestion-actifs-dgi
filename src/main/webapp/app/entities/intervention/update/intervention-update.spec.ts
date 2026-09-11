import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IPanne } from 'app/entities/panne/panne.model';
import { PanneService } from 'app/entities/panne/service/panne.service';
import { IPlanningMaintenance } from 'app/entities/planning-maintenance/planning-maintenance.model';
import { PlanningMaintenanceService } from 'app/entities/planning-maintenance/service/planning-maintenance.service';
import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';

import { InterventionFormService } from './intervention-form.service';
import { InterventionUpdate } from './intervention-update';

describe('Intervention Management Update Component', () => {
  let comp: InterventionUpdate;
  let fixture: ComponentFixture<InterventionUpdate>;
  let activatedRoute: ActivatedRoute;
  let interventionFormService: InterventionFormService;
  let interventionService: InterventionService;
  let panneService: PanneService;
  let planningMaintenanceService: PlanningMaintenanceService;

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

    fixture = TestBed.createComponent(InterventionUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    interventionFormService = TestBed.inject(InterventionFormService);
    interventionService = TestBed.inject(InterventionService);
    panneService = TestBed.inject(PanneService);
    planningMaintenanceService = TestBed.inject(PlanningMaintenanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Panne query and add missing value', () => {
      const intervention: IIntervention = { id: 29837 };
      const panne: IPanne = { id: 5747 };
      intervention.panne = panne;

      const panneCollection: IPanne[] = [{ id: 5747 }];
      vi.spyOn(panneService, 'query').mockReturnValue(of(new HttpResponse({ body: panneCollection })));
      const additionalPannes = [panne];
      const expectedCollection: IPanne[] = [...additionalPannes, ...panneCollection];
      vi.spyOn(panneService, 'addPanneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(panneService.query).toHaveBeenCalled();
      expect(panneService.addPanneToCollectionIfMissing).toHaveBeenCalledWith(
        panneCollection,
        ...additionalPannes.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.pannesSharedCollection()).toEqual(expectedCollection);
    });

    it('should call PlanningMaintenance query and add missing value', () => {
      const intervention: IIntervention = { id: 29837 };
      const plannings: IPlanningMaintenance[] = [{ id: 20630 }];
      intervention.plannings = plannings;

      const planningMaintenanceCollection: IPlanningMaintenance[] = [{ id: 20630 }];
      vi.spyOn(planningMaintenanceService, 'query').mockReturnValue(of(new HttpResponse({ body: planningMaintenanceCollection })));
      const additionalPlanningMaintenances = [...plannings];
      const expectedCollection: IPlanningMaintenance[] = [...additionalPlanningMaintenances, ...planningMaintenanceCollection];
      vi.spyOn(planningMaintenanceService, 'addPlanningMaintenanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(planningMaintenanceService.query).toHaveBeenCalled();
      expect(planningMaintenanceService.addPlanningMaintenanceToCollectionIfMissing).toHaveBeenCalledWith(
        planningMaintenanceCollection,
        ...additionalPlanningMaintenances.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.planningMaintenancesSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const intervention: IIntervention = { id: 29837 };
      const panne: IPanne = { id: 5747 };
      intervention.panne = panne;
      const planning: IPlanningMaintenance = { id: 20630 };
      intervention.plannings = [planning];

      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      expect(comp.pannesSharedCollection()).toContainEqual(panne);
      expect(comp.planningMaintenancesSharedCollection()).toContainEqual(planning);
      expect(comp.intervention).toEqual(intervention);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IIntervention>();
      const intervention = { id: 13583 };
      vi.spyOn(interventionFormService, 'getIntervention').mockReturnValue(intervention);
      vi.spyOn(interventionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(intervention);
      saveSubject.complete();

      // THEN
      expect(interventionFormService.getIntervention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(interventionService.update).toHaveBeenCalledWith(expect.objectContaining(intervention));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IIntervention>();
      const intervention = { id: 13583 };
      vi.spyOn(interventionFormService, 'getIntervention').mockReturnValue({ id: null });
      vi.spyOn(interventionService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(intervention);
      saveSubject.complete();

      // THEN
      expect(interventionFormService.getIntervention).toHaveBeenCalled();
      expect(interventionService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IIntervention>();
      const intervention = { id: 13583 };
      vi.spyOn(interventionService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ intervention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(interventionService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePanne', () => {
      it('should forward to panneService', () => {
        const entity = { id: 5747 };
        const entity2 = { id: 32103 };
        vi.spyOn(panneService, 'comparePanne');
        comp.comparePanne(entity, entity2);
        expect(panneService.comparePanne).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePlanningMaintenance', () => {
      it('should forward to planningMaintenanceService', () => {
        const entity = { id: 20630 };
        const entity2 = { id: 8418 };
        vi.spyOn(planningMaintenanceService, 'comparePlanningMaintenance');
        comp.comparePlanningMaintenance(entity, entity2);
        expect(planningMaintenanceService.comparePlanningMaintenance).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
