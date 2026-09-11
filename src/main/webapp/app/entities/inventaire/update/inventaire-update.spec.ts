import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { IInventaire } from '../inventaire.model';
import { InventaireService } from '../service/inventaire.service';

import { InventaireFormService } from './inventaire-form.service';
import { InventaireUpdate } from './inventaire-update';

describe('Inventaire Management Update Component', () => {
  let comp: InventaireUpdate;
  let fixture: ComponentFixture<InventaireUpdate>;
  let activatedRoute: ActivatedRoute;
  let inventaireFormService: InventaireFormService;
  let inventaireService: InventaireService;
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

    fixture = TestBed.createComponent(InventaireUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inventaireFormService = TestBed.inject(InventaireFormService);
    inventaireService = TestBed.inject(InventaireService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Actif query and add missing value', () => {
      const inventaire: IInventaire = { id: 8000 };
      const actif: IActif = { id: 3500 };
      inventaire.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inventaire: IInventaire = { id: 8000 };
      const actif: IActif = { id: 3500 };
      inventaire.actif = actif;

      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.inventaire).toEqual(inventaire);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventaire>();
      const inventaire = { id: 3857 };
      vi.spyOn(inventaireFormService, 'getInventaire').mockReturnValue(inventaire);
      vi.spyOn(inventaireService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventaire);
      saveSubject.complete();

      // THEN
      expect(inventaireFormService.getInventaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inventaireService.update).toHaveBeenCalledWith(expect.objectContaining(inventaire));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IInventaire>();
      const inventaire = { id: 3857 };
      vi.spyOn(inventaireFormService, 'getInventaire').mockReturnValue({ id: null });
      vi.spyOn(inventaireService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(inventaire);
      saveSubject.complete();

      // THEN
      expect(inventaireFormService.getInventaire).toHaveBeenCalled();
      expect(inventaireService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IInventaire>();
      const inventaire = { id: 3857 };
      vi.spyOn(inventaireService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inventaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inventaireService.update).toHaveBeenCalled();
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
