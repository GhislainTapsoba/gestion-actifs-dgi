import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServiceDgiService } from '../service/service-dgi.service';
import { IServiceDgi } from '../service-dgi.model';

import { ServiceDgiFormService } from './service-dgi-form.service';
import { ServiceDgiUpdate } from './service-dgi-update';

describe('ServiceDgi Management Update Component', () => {
  let comp: ServiceDgiUpdate;
  let fixture: ComponentFixture<ServiceDgiUpdate>;
  let activatedRoute: ActivatedRoute;
  let serviceDgiFormService: ServiceDgiFormService;
  let serviceDgiService: ServiceDgiService;

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

    fixture = TestBed.createComponent(ServiceDgiUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceDgiFormService = TestBed.inject(ServiceDgiFormService);
    serviceDgiService = TestBed.inject(ServiceDgiService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const serviceDgi: IServiceDgi = { id: 4731 };

      activatedRoute.data = of({ serviceDgi });
      comp.ngOnInit();

      expect(comp.serviceDgi).toEqual(serviceDgi);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServiceDgi>();
      const serviceDgi = { id: 3925 };
      vi.spyOn(serviceDgiFormService, 'getServiceDgi').mockReturnValue(serviceDgi);
      vi.spyOn(serviceDgiService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceDgi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(serviceDgi);
      saveSubject.complete();

      // THEN
      expect(serviceDgiFormService.getServiceDgi).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceDgiService.update).toHaveBeenCalledWith(expect.objectContaining(serviceDgi));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IServiceDgi>();
      const serviceDgi = { id: 3925 };
      vi.spyOn(serviceDgiFormService, 'getServiceDgi').mockReturnValue({ id: null });
      vi.spyOn(serviceDgiService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceDgi: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(serviceDgi);
      saveSubject.complete();

      // THEN
      expect(serviceDgiFormService.getServiceDgi).toHaveBeenCalled();
      expect(serviceDgiService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IServiceDgi>();
      const serviceDgi = { id: 3925 };
      vi.spyOn(serviceDgiService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceDgi });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceDgiService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
