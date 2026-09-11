import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { TransfertService } from 'app/entities/transfert/service/transfert.service';
import { ITransfert } from 'app/entities/transfert/transfert.model';
import { TransfertActifService } from '../service/transfert-actif.service';
import { ITransfertActif } from '../transfert-actif.model';

import { TransfertActifFormService } from './transfert-actif-form.service';
import { TransfertActifUpdate } from './transfert-actif-update';

describe('TransfertActif Management Update Component', () => {
  let comp: TransfertActifUpdate;
  let fixture: ComponentFixture<TransfertActifUpdate>;
  let activatedRoute: ActivatedRoute;
  let transfertActifFormService: TransfertActifFormService;
  let transfertActifService: TransfertActifService;
  let transfertService: TransfertService;
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

    fixture = TestBed.createComponent(TransfertActifUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transfertActifFormService = TestBed.inject(TransfertActifFormService);
    transfertActifService = TestBed.inject(TransfertActifService);
    transfertService = TestBed.inject(TransfertService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Transfert query and add missing value', () => {
      const transfertActif: ITransfertActif = { id: 3986 };
      const transfert: ITransfert = { id: 22898 };
      transfertActif.transfert = transfert;

      const transfertCollection: ITransfert[] = [{ id: 22898 }];
      vi.spyOn(transfertService, 'query').mockReturnValue(of(new HttpResponse({ body: transfertCollection })));
      const additionalTransferts = [transfert];
      const expectedCollection: ITransfert[] = [...additionalTransferts, ...transfertCollection];
      vi.spyOn(transfertService, 'addTransfertToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transfertActif });
      comp.ngOnInit();

      expect(transfertService.query).toHaveBeenCalled();
      expect(transfertService.addTransfertToCollectionIfMissing).toHaveBeenCalledWith(
        transfertCollection,
        ...additionalTransferts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.transfertsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Actif query and add missing value', () => {
      const transfertActif: ITransfertActif = { id: 3986 };
      const actif: IActif = { id: 3500 };
      transfertActif.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transfertActif });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const transfertActif: ITransfertActif = { id: 3986 };
      const transfert: ITransfert = { id: 22898 };
      transfertActif.transfert = transfert;
      const actif: IActif = { id: 3500 };
      transfertActif.actif = actif;

      activatedRoute.data = of({ transfertActif });
      comp.ngOnInit();

      expect(comp.transfertsSharedCollection()).toContainEqual(transfert);
      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.transfertActif).toEqual(transfertActif);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransfertActif>();
      const transfertActif = { id: 4253 };
      vi.spyOn(transfertActifFormService, 'getTransfertActif').mockReturnValue(transfertActif);
      vi.spyOn(transfertActifService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfertActif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transfertActif);
      saveSubject.complete();

      // THEN
      expect(transfertActifFormService.getTransfertActif).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transfertActifService.update).toHaveBeenCalledWith(expect.objectContaining(transfertActif));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransfertActif>();
      const transfertActif = { id: 4253 };
      vi.spyOn(transfertActifFormService, 'getTransfertActif').mockReturnValue({ id: null });
      vi.spyOn(transfertActifService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfertActif: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transfertActif);
      saveSubject.complete();

      // THEN
      expect(transfertActifFormService.getTransfertActif).toHaveBeenCalled();
      expect(transfertActifService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITransfertActif>();
      const transfertActif = { id: 4253 };
      vi.spyOn(transfertActifService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfertActif });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transfertActifService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTransfert', () => {
      it('should forward to transfertService', () => {
        const entity = { id: 22898 };
        const entity2 = { id: 14214 };
        vi.spyOn(transfertService, 'compareTransfert');
        comp.compareTransfert(entity, entity2);
        expect(transfertService.compareTransfert).toHaveBeenCalledWith(entity, entity2);
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
