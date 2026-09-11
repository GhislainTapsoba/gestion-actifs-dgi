import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { IAffectation } from 'app/entities/affectation/affectation.model';
import { AffectationService } from 'app/entities/affectation/service/affectation.service';
import { IAffectationActif } from '../affectation-actif.model';
import { AffectationActifService } from '../service/affectation-actif.service';

import { AffectationActifFormService } from './affectation-actif-form.service';
import { AffectationActifUpdate } from './affectation-actif-update';

describe('AffectationActif Management Update Component', () => {
  let comp: AffectationActifUpdate;
  let fixture: ComponentFixture<AffectationActifUpdate>;
  let activatedRoute: ActivatedRoute;
  let affectationActifFormService: AffectationActifFormService;
  let affectationActifService: AffectationActifService;
  let affectationService: AffectationService;
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

    fixture = TestBed.createComponent(AffectationActifUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    affectationActifFormService = TestBed.inject(AffectationActifFormService);
    affectationActifService = TestBed.inject(AffectationActifService);
    affectationService = TestBed.inject(AffectationService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Affectation query and add missing value', () => {
      const affectationActif: IAffectationActif = { id: 31782 };
      const affectation: IAffectation = { id: 29609 };
      affectationActif.affectation = affectation;

      const affectationCollection: IAffectation[] = [{ id: 29609 }];
      vi.spyOn(affectationService, 'query').mockReturnValue(of(new HttpResponse({ body: affectationCollection })));
      const additionalAffectations = [affectation];
      const expectedCollection: IAffectation[] = [...additionalAffectations, ...affectationCollection];
      vi.spyOn(affectationService, 'addAffectationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectationActif });
      comp.ngOnInit();

      expect(affectationService.query).toHaveBeenCalled();
      expect(affectationService.addAffectationToCollectionIfMissing).toHaveBeenCalledWith(
        affectationCollection,
        ...additionalAffectations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.affectationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Actif query and add missing value', () => {
      const affectationActif: IAffectationActif = { id: 31782 };
      const actif: IActif = { id: 3500 };
      affectationActif.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectationActif });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const affectationActif: IAffectationActif = { id: 31782 };
      const affectation: IAffectation = { id: 29609 };
      affectationActif.affectation = affectation;
      const actif: IActif = { id: 3500 };
      affectationActif.actif = actif;

      activatedRoute.data = of({ affectationActif });
      comp.ngOnInit();

      expect(comp.affectationsSharedCollection()).toContainEqual(affectation);
      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.affectationActif).toEqual(affectationActif);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectationActif>();
      const affectationActif = { id: 7016 };
      vi.spyOn(affectationActifFormService, 'getAffectationActif').mockReturnValue(affectationActif);
      vi.spyOn(affectationActifService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectationActif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectationActif);
      saveSubject.complete();

      // THEN
      expect(affectationActifFormService.getAffectationActif).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(affectationActifService.update).toHaveBeenCalledWith(expect.objectContaining(affectationActif));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectationActif>();
      const affectationActif = { id: 7016 };
      vi.spyOn(affectationActifFormService, 'getAffectationActif').mockReturnValue({ id: null });
      vi.spyOn(affectationActifService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectationActif: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectationActif);
      saveSubject.complete();

      // THEN
      expect(affectationActifFormService.getAffectationActif).toHaveBeenCalled();
      expect(affectationActifService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectationActif>();
      const affectationActif = { id: 7016 };
      vi.spyOn(affectationActifService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectationActif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(affectationActifService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAffectation', () => {
      it('should forward to affectationService', () => {
        const entity = { id: 29609 };
        const entity2 = { id: 22469 };
        vi.spyOn(affectationService, 'compareAffectation');
        comp.compareAffectation(entity, entity2);
        expect(affectationService.compareAffectation).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
