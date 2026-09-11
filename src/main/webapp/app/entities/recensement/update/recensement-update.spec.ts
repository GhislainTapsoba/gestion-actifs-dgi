import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IRecensement } from '../recensement.model';
import { RecensementService } from '../service/recensement.service';

import { RecensementFormService } from './recensement-form.service';
import { RecensementUpdate } from './recensement-update';

describe('Recensement Management Update Component', () => {
  let comp: RecensementUpdate;
  let fixture: ComponentFixture<RecensementUpdate>;
  let activatedRoute: ActivatedRoute;
  let recensementFormService: RecensementFormService;
  let recensementService: RecensementService;

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

    fixture = TestBed.createComponent(RecensementUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recensementFormService = TestBed.inject(RecensementFormService);
    recensementService = TestBed.inject(RecensementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const recensement: IRecensement = { id: 15985 };

      activatedRoute.data = of({ recensement });
      comp.ngOnInit();

      expect(comp.recensement).toEqual(recensement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRecensement>();
      const recensement = { id: 26788 };
      vi.spyOn(recensementFormService, 'getRecensement').mockReturnValue(recensement);
      vi.spyOn(recensementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recensement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(recensement);
      saveSubject.complete();

      // THEN
      expect(recensementFormService.getRecensement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recensementService.update).toHaveBeenCalledWith(expect.objectContaining(recensement));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IRecensement>();
      const recensement = { id: 26788 };
      vi.spyOn(recensementFormService, 'getRecensement').mockReturnValue({ id: null });
      vi.spyOn(recensementService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recensement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(recensement);
      saveSubject.complete();

      // THEN
      expect(recensementFormService.getRecensement).toHaveBeenCalled();
      expect(recensementService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IRecensement>();
      const recensement = { id: 26788 };
      vi.spyOn(recensementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recensement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recensementService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
