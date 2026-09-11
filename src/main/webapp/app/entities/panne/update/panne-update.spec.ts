import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { IPanne } from '../panne.model';
import { PanneService } from '../service/panne.service';

import { PanneFormService } from './panne-form.service';
import { PanneUpdate } from './panne-update';

describe('Panne Management Update Component', () => {
  let comp: PanneUpdate;
  let fixture: ComponentFixture<PanneUpdate>;
  let activatedRoute: ActivatedRoute;
  let panneFormService: PanneFormService;
  let panneService: PanneService;
  let actifService: ActifService;

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

    fixture = TestBed.createComponent(PanneUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    panneFormService = TestBed.inject(PanneFormService);
    panneService = TestBed.inject(PanneService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Actif query and add missing value', () => {
      const panne: IPanne = { id: 32103 };
      const actif: IActif = { id: 3500 };
      panne.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ panne });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const panne: IPanne = { id: 32103 };
      const actif: IActif = { id: 3500 };
      panne.actif = actif;

      activatedRoute.data = of({ panne });
      comp.ngOnInit();

      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.panne).toEqual(panne);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPanne>();
      const panne = { id: 5747 };
      vi.spyOn(panneFormService, 'getPanne').mockReturnValue(panne);
      vi.spyOn(panneService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ panne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(panne);
      saveSubject.complete();

      // THEN
      expect(panneFormService.getPanne).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(panneService.update).toHaveBeenCalledWith(expect.objectContaining(panne));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IPanne>();
      const panne = { id: 5747 };
      vi.spyOn(panneFormService, 'getPanne').mockReturnValue({ id: null });
      vi.spyOn(panneService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ panne: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(panne);
      saveSubject.complete();

      // THEN
      expect(panneFormService.getPanne).toHaveBeenCalled();
      expect(panneService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IPanne>();
      const panne = { id: 5747 };
      vi.spyOn(panneService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ panne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(panneService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareActif', () => {
      it('should forward to actifService', () => {
        const entity = { id: 3500 };
        const entity2 = { id: 21468 };
        vi.spyOn(actifService, 'compareActif');
        comp.compareActif(entity, entity2);
        expect(actifService.compareActif).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
