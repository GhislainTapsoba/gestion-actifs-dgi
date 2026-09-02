import { beforeEach, describe, expect, it, vi } from 'vitest';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

import { FournisseurFormService } from './fournisseur-form.service';
import { FournisseurUpdate } from './fournisseur-update';

describe('Fournisseur Management Update Component', () => {
  let comp: FournisseurUpdate;
  let fixture: ComponentFixture<FournisseurUpdate>;
  let activatedRoute: ActivatedRoute;
  let fournisseurFormService: FournisseurFormService;
  let fournisseurService: FournisseurService;

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

    fixture = TestBed.createComponent(FournisseurUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    fournisseurFormService = TestBed.inject(FournisseurFormService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const fournisseur: IFournisseur = { id: 3552 };

      activatedRoute.data = of({ fournisseur });
      comp.ngOnInit();

      expect(comp.fournisseur).toEqual(fournisseur);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFournisseur>();
      const fournisseur = { id: 25678 };
      vi.spyOn(fournisseurFormService, 'getFournisseur').mockReturnValue(fournisseur);
      vi.spyOn(fournisseurService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fournisseur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fournisseur);
      saveSubject.complete();

      // THEN
      expect(fournisseurFormService.getFournisseur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(fournisseurService.update).toHaveBeenCalledWith(expect.objectContaining(fournisseur));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IFournisseur>();
      const fournisseur = { id: 25678 };
      vi.spyOn(fournisseurFormService, 'getFournisseur').mockReturnValue({ id: null });
      vi.spyOn(fournisseurService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fournisseur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(fournisseur);
      saveSubject.complete();

      // THEN
      expect(fournisseurFormService.getFournisseur).toHaveBeenCalled();
      expect(fournisseurService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IFournisseur>();
      const fournisseur = { id: 25678 };
      vi.spyOn(fournisseurService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ fournisseur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(fournisseurService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
