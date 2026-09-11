import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ICategorieMateriel } from '../categorie-materiel.model';
import { CategorieMaterielService } from '../service/categorie-materiel.service';

import { CategorieMaterielFormService } from './categorie-materiel-form.service';
import { CategorieMaterielUpdate } from './categorie-materiel-update';

describe('CategorieMateriel Management Update Component', () => {
  let comp: CategorieMaterielUpdate;
  let fixture: ComponentFixture<CategorieMaterielUpdate>;
  let activatedRoute: ActivatedRoute;
  let categorieMaterielFormService: CategorieMaterielFormService;
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

    fixture = TestBed.createComponent(CategorieMaterielUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categorieMaterielFormService = TestBed.inject(CategorieMaterielFormService);
    categorieMaterielService = TestBed.inject(CategorieMaterielService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const categorieMateriel: ICategorieMateriel = { id: 1809 };

      activatedRoute.data = of({ categorieMateriel });
      comp.ngOnInit();

      expect(comp.categorieMateriel).toEqual(categorieMateriel);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICategorieMateriel>();
      const categorieMateriel = { id: 3013 };
      vi.spyOn(categorieMaterielFormService, 'getCategorieMateriel').mockReturnValue(categorieMateriel);
      vi.spyOn(categorieMaterielService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categorieMateriel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(categorieMateriel);
      saveSubject.complete();

      // THEN
      expect(categorieMaterielFormService.getCategorieMateriel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(categorieMaterielService.update).toHaveBeenCalledWith(expect.objectContaining(categorieMateriel));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ICategorieMateriel>();
      const categorieMateriel = { id: 3013 };
      vi.spyOn(categorieMaterielFormService, 'getCategorieMateriel').mockReturnValue({ id: null });
      vi.spyOn(categorieMaterielService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categorieMateriel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(categorieMateriel);
      saveSubject.complete();

      // THEN
      expect(categorieMaterielFormService.getCategorieMateriel).toHaveBeenCalled();
      expect(categorieMaterielService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ICategorieMateriel>();
      const categorieMateriel = { id: 3013 };
      vi.spyOn(categorieMaterielService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categorieMateriel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categorieMaterielService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
