import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { IAffectation } from '../affectation.model';
import { AffectationService } from '../service/affectation.service';

import { AffectationFormService } from './affectation-form.service';
import { AffectationUpdate } from './affectation-update';

describe('Affectation Management Update Component', () => {
  let comp: AffectationUpdate;
  let fixture: ComponentFixture<AffectationUpdate>;
  let activatedRoute: ActivatedRoute;
  let affectationFormService: AffectationFormService;
  let affectationService: AffectationService;
  let agentService: AgentService;

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

    fixture = TestBed.createComponent(AffectationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    affectationFormService = TestBed.inject(AffectationFormService);
    affectationService = TestBed.inject(AffectationService);
    agentService = TestBed.inject(AgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Agent query and add missing value', () => {
      const affectation: IAffectation = { id: 22469 };
      const agent: IAgent = { id: 25235 };
      affectation.agent = agent;

      const agentCollection: IAgent[] = [{ id: 25235 }];
      vi.spyOn(agentService, 'query').mockReturnValue(of(new HttpResponse({ body: agentCollection })));
      const additionalAgents = [agent];
      const expectedCollection: IAgent[] = [...additionalAgents, ...agentCollection];
      vi.spyOn(agentService, 'addAgentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      expect(agentService.query).toHaveBeenCalled();
      expect(agentService.addAgentToCollectionIfMissing).toHaveBeenCalledWith(
        agentCollection,
        ...additionalAgents.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.agentsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const affectation: IAffectation = { id: 22469 };
      const agent: IAgent = { id: 25235 };
      affectation.agent = agent;

      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      expect(comp.agentsSharedCollection()).toContainEqual(agent);
      expect(comp.affectation).toEqual(affectation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectation>();
      const affectation = { id: 29609 };
      vi.spyOn(affectationFormService, 'getAffectation').mockReturnValue(affectation);
      vi.spyOn(affectationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectation);
      saveSubject.complete();

      // THEN
      expect(affectationFormService.getAffectation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(affectationService.update).toHaveBeenCalledWith(expect.objectContaining(affectation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectation>();
      const affectation = { id: 29609 };
      vi.spyOn(affectationFormService, 'getAffectation').mockReturnValue({ id: null });
      vi.spyOn(affectationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectation);
      saveSubject.complete();

      // THEN
      expect(affectationFormService.getAffectation).toHaveBeenCalled();
      expect(affectationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectation>();
      const affectation = { id: 29609 };
      vi.spyOn(affectationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(affectationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAgent', () => {
      it('should forward to agentService', () => {
        const entity = { id: 25235 };
        const entity2 = { id: 18913 };
        vi.spyOn(agentService, 'compareAgent');
        comp.compareAgent(entity, entity2);
        expect(agentService.compareAgent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
