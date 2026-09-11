import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IAffectation } from 'app/entities/affectation/affectation.model';
import { AffectationService } from 'app/entities/affectation/service/affectation.service';
import { TransfertService } from 'app/entities/transfert/service/transfert.service';
import { ITransfert } from 'app/entities/transfert/transfert.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IBordereau } from '../bordereau.model';
import { BordereauService } from '../service/bordereau.service';

import { BordereauFormService } from './bordereau-form.service';
import { BordereauUpdate } from './bordereau-update';

describe('Bordereau Management Update Component', () => {
  let comp: BordereauUpdate;
  let fixture: ComponentFixture<BordereauUpdate>;
  let activatedRoute: ActivatedRoute;
  let bordereauFormService: BordereauFormService;
  let bordereauService: BordereauService;
  let transfertService: TransfertService;
  let affectationService: AffectationService;
  let userService: UserService;

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

    fixture = TestBed.createComponent(BordereauUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bordereauFormService = TestBed.inject(BordereauFormService);
    bordereauService = TestBed.inject(BordereauService);
    transfertService = TestBed.inject(TransfertService);
    affectationService = TestBed.inject(AffectationService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Transfert query and add missing value', () => {
      const bordereau: IBordereau = { id: 8037 };
      const transfert: ITransfert = { id: 22898 };
      bordereau.transfert = transfert;

      const transfertCollection: ITransfert[] = [{ id: 22898 }];
      vi.spyOn(transfertService, 'query').mockReturnValue(of(new HttpResponse({ body: transfertCollection })));
      const additionalTransferts = [transfert];
      const expectedCollection: ITransfert[] = [...additionalTransferts, ...transfertCollection];
      vi.spyOn(transfertService, 'addTransfertToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bordereau });
      comp.ngOnInit();

      expect(transfertService.query).toHaveBeenCalled();
      expect(transfertService.addTransfertToCollectionIfMissing).toHaveBeenCalledWith(
        transfertCollection,
        ...additionalTransferts.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.transfertsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Affectation query and add missing value', () => {
      const bordereau: IBordereau = { id: 8037 };
      const affectation: IAffectation = { id: 29609 };
      bordereau.affectation = affectation;

      const affectationCollection: IAffectation[] = [{ id: 29609 }];
      vi.spyOn(affectationService, 'query').mockReturnValue(of(new HttpResponse({ body: affectationCollection })));
      const additionalAffectations = [affectation];
      const expectedCollection: IAffectation[] = [...additionalAffectations, ...affectationCollection];
      vi.spyOn(affectationService, 'addAffectationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bordereau });
      comp.ngOnInit();

      expect(affectationService.query).toHaveBeenCalled();
      expect(affectationService.addAffectationToCollectionIfMissing).toHaveBeenCalledWith(
        affectationCollection,
        ...additionalAffectations.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.affectationsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const bordereau: IBordereau = { id: 8037 };
      const emetteur: IUser = { id: 3944 };
      bordereau.emetteur = emetteur;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [emetteur];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bordereau });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bordereau: IBordereau = { id: 8037 };
      const transfert: ITransfert = { id: 22898 };
      bordereau.transfert = transfert;
      const affectation: IAffectation = { id: 29609 };
      bordereau.affectation = affectation;
      const emetteur: IUser = { id: 3944 };
      bordereau.emetteur = emetteur;

      activatedRoute.data = of({ bordereau });
      comp.ngOnInit();

      expect(comp.transfertsSharedCollection()).toContainEqual(transfert);
      expect(comp.affectationsSharedCollection()).toContainEqual(affectation);
      expect(comp.usersSharedCollection()).toContainEqual(emetteur);
      expect(comp.bordereau).toEqual(bordereau);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IBordereau>();
      const bordereau = { id: 19364 };
      vi.spyOn(bordereauFormService, 'getBordereau').mockReturnValue(bordereau);
      vi.spyOn(bordereauService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bordereau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(bordereau);
      saveSubject.complete();

      // THEN
      expect(bordereauFormService.getBordereau).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bordereauService.update).toHaveBeenCalledWith(expect.objectContaining(bordereau));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IBordereau>();
      const bordereau = { id: 19364 };
      vi.spyOn(bordereauFormService, 'getBordereau').mockReturnValue({ id: null });
      vi.spyOn(bordereauService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bordereau: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(bordereau);
      saveSubject.complete();

      // THEN
      expect(bordereauFormService.getBordereau).toHaveBeenCalled();
      expect(bordereauService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IBordereau>();
      const bordereau = { id: 19364 };
      vi.spyOn(bordereauService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bordereau });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bordereauService.update).toHaveBeenCalled();
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

    describe('compareAffectation', () => {
      it('should forward to affectationService', () => {
        const entity = { id: 29609 };
        const entity2 = { id: 22469 };
        vi.spyOn(affectationService, 'compareAffectation');
        comp.compareAffectation(entity, entity2);
        expect(affectationService.compareAffectation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
