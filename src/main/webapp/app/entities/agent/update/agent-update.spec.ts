import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServiceDgiService } from 'app/entities/service-dgi/service/service-dgi.service';
import { IServiceDgi } from 'app/entities/service-dgi/service-dgi.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IAgent } from '../agent.model';
import { AgentService } from '../service/agent.service';

import { AgentFormService } from './agent-form.service';
import { AgentUpdate } from './agent-update';

describe('Agent Management Update Component', () => {
  let comp: AgentUpdate;
  let fixture: ComponentFixture<AgentUpdate>;
  let activatedRoute: ActivatedRoute;
  let agentFormService: AgentFormService;
  let agentService: AgentService;
  let serviceDgiService: ServiceDgiService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(AgentUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agentFormService = TestBed.inject(AgentFormService);
    agentService = TestBed.inject(AgentService);
    serviceDgiService = TestBed.inject(ServiceDgiService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ServiceDgi query and add missing value', () => {
      const agent: IAgent = { id: 18913 };
      const service: IServiceDgi = { id: 3925 };
      agent.service = service;

      const serviceDgiCollection: IServiceDgi[] = [{ id: 3925 }];
      vi.spyOn(serviceDgiService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceDgiCollection })));
      const additionalServiceDgis = [service];
      const expectedCollection: IServiceDgi[] = [...additionalServiceDgis, ...serviceDgiCollection];
      vi.spyOn(serviceDgiService, 'addServiceDgiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(serviceDgiService.query).toHaveBeenCalled();
      expect(serviceDgiService.addServiceDgiToCollectionIfMissing).toHaveBeenCalledWith(
        serviceDgiCollection,
        ...additionalServiceDgis.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviceDgisSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const agent: IAgent = { id: 18913 };
      const utilisateur: IUser = { id: 3944 };
      agent.utilisateur = utilisateur;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [utilisateur];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const agent: IAgent = { id: 18913 };
      const service: IServiceDgi = { id: 3925 };
      agent.service = service;
      const utilisateur: IUser = { id: 3944 };
      agent.utilisateur = utilisateur;

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(comp.serviceDgisSharedCollection()).toContainEqual(service);
      expect(comp.usersSharedCollection()).toContainEqual(utilisateur);
      expect(comp.agent).toEqual(agent);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAgent>();
      const agent = { id: 25235 };
      vi.spyOn(agentFormService, 'getAgent').mockReturnValue(agent);
      vi.spyOn(agentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(agent);
      saveSubject.complete();

      // THEN
      expect(agentFormService.getAgent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agentService.update).toHaveBeenCalledWith(expect.objectContaining(agent));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAgent>();
      const agent = { id: 25235 };
      vi.spyOn(agentFormService, 'getAgent').mockReturnValue({ id: null });
      vi.spyOn(agentService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(agent);
      saveSubject.complete();

      // THEN
      expect(agentFormService.getAgent).toHaveBeenCalled();
      expect(agentService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAgent>();
      const agent = { id: 25235 };
      vi.spyOn(agentService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agentService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceDgi', () => {
      it('should forward to serviceDgiService', () => {
        const entity = { id: 3925 };
        const entity2 = { id: 4731 };
        vi.spyOn(serviceDgiService, 'compareServiceDgi');
        comp.compareServiceDgi(entity, entity2);
        expect(serviceDgiService.compareServiceDgi).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
