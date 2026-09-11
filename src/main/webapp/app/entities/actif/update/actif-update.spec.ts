import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICategorieMateriel } from 'app/entities/categorie-materiel/categorie-materiel.model';
import { CategorieMaterielService } from 'app/entities/categorie-materiel/service/categorie-materiel.service';
import { IActif } from '../actif.model';
import { ActifService } from '../service/actif.service';

import { ActifFormService } from './actif-form.service';
import { ActifUpdate } from './actif-update';

describe('Actif Management Update Component', () => {
  let comp: ActifUpdate;
  let fixture: ComponentFixture<ActifUpdate>;
  let activatedRoute: ActivatedRoute;
  let actifFormService: ActifFormService;
  let actifService: ActifService;
  let categorieMaterielService: CategorieMaterielService;

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

    fixture = TestBed.createComponent(ActifUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    actifFormService = TestBed.inject(ActifFormService);
    actifService = TestBed.inject(ActifService);
    categorieMaterielService = TestBed.inject(CategorieMaterielService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CategorieMateriel query and add missing value', () => {
      const actif: IActif = { id: 21468 };
      const categorie: ICategorieMateriel = { id: 3013 };
      actif.categorie = categorie;

      const categorieMaterielCollection: ICategorieMateriel[] = [{ id: 3013 }];
      vi.spyOn(categorieMaterielService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieMaterielCollection })));
      const additionalCategorieMateriels = [categorie];
      const expectedCollection: ICategorieMateriel[] = [...additionalCategorieMateriels, ...categorieMaterielCollection];
      vi.spyOn(categorieMaterielService, 'addCategorieMaterielToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ actif });
      comp.ngOnInit();

      expect(categorieMaterielService.query).toHaveBeenCalled();
      expect(categorieMaterielService.addCategorieMaterielToCollectionIfMissing).toHaveBeenCalledWith(
        categorieMaterielCollection,
        ...additionalCategorieMateriels.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.categorieMaterielsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const actif: IActif = { id: 21468 };
      const categorie: ICategorieMateriel = { id: 3013 };
      actif.categorie = categorie;

      activatedRoute.data = of({ actif });
      comp.ngOnInit();

      expect(comp.categorieMaterielsSharedCollection()).toContainEqual(categorie);
      expect(comp.actif).toEqual(actif);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IActif>();
      const actif = { id: 3500 };
      vi.spyOn(actifFormService, 'getActif').mockReturnValue(actif);
      vi.spyOn(actifService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(actif);
      saveSubject.complete();

      // THEN
      expect(actifFormService.getActif).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(actifService.update).toHaveBeenCalledWith(expect.objectContaining(actif));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IActif>();
      const actif = { id: 3500 };
      vi.spyOn(actifFormService, 'getActif').mockReturnValue({ id: null });
      vi.spyOn(actifService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actif: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(actif);
      saveSubject.complete();

      // THEN
      expect(actifFormService.getActif).toHaveBeenCalled();
      expect(actifService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IActif>();
      const actif = { id: 3500 };
      vi.spyOn(actifService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ actif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(actifService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCategorieMateriel', () => {
      it('should forward to categorieMaterielService', () => {
        const entity = { id: 3013 };
        const entity2 = { id: 1809 };
        vi.spyOn(categorieMaterielService, 'compareCategorieMateriel');
        comp.compareCategorieMateriel(entity, entity2);
        expect(categorieMaterielService.compareCategorieMateriel).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
