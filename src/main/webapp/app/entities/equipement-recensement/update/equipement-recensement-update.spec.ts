import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { IRecensement } from 'app/entities/recensement/recensement.model';
import { RecensementService } from 'app/entities/recensement/service/recensement.service';
import { IEquipementRecensement } from '../equipement-recensement.model';
import { EquipementRecensementService } from '../service/equipement-recensement.service';

import { EquipementRecensementFormService } from './equipement-recensement-form.service';
import { EquipementRecensementUpdate } from './equipement-recensement-update';

describe('EquipementRecensement Management Update Component', () => {
  let comp: EquipementRecensementUpdate;
  let fixture: ComponentFixture<EquipementRecensementUpdate>;
  let activatedRoute: ActivatedRoute;
  let equipementRecensementFormService: EquipementRecensementFormService;
  let equipementRecensementService: EquipementRecensementService;
  let recensementService: RecensementService;
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

    fixture = TestBed.createComponent(EquipementRecensementUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    equipementRecensementFormService = TestBed.inject(EquipementRecensementFormService);
    equipementRecensementService = TestBed.inject(EquipementRecensementService);
    recensementService = TestBed.inject(RecensementService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Recensement query and add missing value', () => {
      const equipementRecensement: IEquipementRecensement = { id: 3026 };
      const recensement: IRecensement = { id: 26788 };
      equipementRecensement.recensement = recensement;

      const recensementCollection: IRecensement[] = [{ id: 26788 }];
      vi.spyOn(recensementService, 'query').mockReturnValue(of(new HttpResponse({ body: recensementCollection })));
      const additionalRecensements = [recensement];
      const expectedCollection: IRecensement[] = [...additionalRecensements, ...recensementCollection];
      vi.spyOn(recensementService, 'addRecensementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equipementRecensement });
      comp.ngOnInit();

      expect(recensementService.query).toHaveBeenCalled();
      expect(recensementService.addRecensementToCollectionIfMissing).toHaveBeenCalledWith(
        recensementCollection,
        ...additionalRecensements.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.recensementsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Actif query and add missing value', () => {
      const equipementRecensement: IEquipementRecensement = { id: 3026 };
      const actif: IActif = { id: 3500 };
      equipementRecensement.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equipementRecensement });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const equipementRecensement: IEquipementRecensement = { id: 3026 };
      const recensement: IRecensement = { id: 26788 };
      equipementRecensement.recensement = recensement;
      const actif: IActif = { id: 3500 };
      equipementRecensement.actif = actif;

      activatedRoute.data = of({ equipementRecensement });
      comp.ngOnInit();

      expect(comp.recensementsSharedCollection()).toContainEqual(recensement);
      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.equipementRecensement).toEqual(equipementRecensement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEquipementRecensement>();
      const equipementRecensement = { id: 7919 };
      vi.spyOn(equipementRecensementFormService, 'getEquipementRecensement').mockReturnValue(equipementRecensement);
      vi.spyOn(equipementRecensementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipementRecensement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(equipementRecensement);
      saveSubject.complete();

      // THEN
      expect(equipementRecensementFormService.getEquipementRecensement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(equipementRecensementService.update).toHaveBeenCalledWith(expect.objectContaining(equipementRecensement));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IEquipementRecensement>();
      const equipementRecensement = { id: 7919 };
      vi.spyOn(equipementRecensementFormService, 'getEquipementRecensement').mockReturnValue({ id: null });
      vi.spyOn(equipementRecensementService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipementRecensement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(equipementRecensement);
      saveSubject.complete();

      // THEN
      expect(equipementRecensementFormService.getEquipementRecensement).toHaveBeenCalled();
      expect(equipementRecensementService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IEquipementRecensement>();
      const equipementRecensement = { id: 7919 };
      vi.spyOn(equipementRecensementService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equipementRecensement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(equipementRecensementService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRecensement', () => {
      it('should forward to recensementService', () => {
        const entity = { id: 26788 };
        const entity2 = { id: 15985 };
        vi.spyOn(recensementService, 'compareRecensement');
        comp.compareRecensement(entity, entity2);
        expect(recensementService.compareRecensement).toHaveBeenCalledWith(entity, entity2);
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
