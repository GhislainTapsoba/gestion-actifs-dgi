import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { IAffectation } from '../affectation.model';
import { AffectationService } from '../service/affectation.service';

import { AffectationFormService } from './affectation-form.service';
import { AffectationUpdate } from './affectation-update';

describe('Affectation Management Update Component', () => {
  let comp: AffectationUpdate;
  let fixture: ComponentFixture<AffectationUpdate>;
  let activatedRoute: ActivatedRoute;
  let affectationFormService: AffectationFormService;
  let affectationService: AffectationService;
  let userService: UserService;
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

    fixture = TestBed.createComponent(AffectationUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    affectationFormService = TestBed.inject(AffectationFormService);
    affectationService = TestBed.inject(AffectationService);
    userService = TestBed.inject(UserService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const affectation: IAffectation = { id: 22469 };
      const utilisateur: IUser = { id: 3944 };
      affectation.utilisateur = utilisateur;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [utilisateur];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Actif query and add missing value', () => {
      const affectation: IAffectation = { id: 22469 };
      const actif: IActif = { id: 3500 };
      affectation.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const affectation: IAffectation = { id: 22469 };
      const utilisateur: IUser = { id: 3944 };
      affectation.utilisateur = utilisateur;
      const actif: IActif = { id: 3500 };
      affectation.actif = actif;

      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(utilisateur);
      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.affectation).toEqual(affectation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectation>();
      const affectation = { id: 29609 };
      vi.spyOn(affectationFormService, 'getAffectation').mockReturnValue(affectation);
      vi.spyOn(affectationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectation);
      saveSubject.complete();

      // THEN
      expect(affectationFormService.getAffectation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(affectationService.update).toHaveBeenCalledWith(expect.objectContaining(affectation));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectation>();
      const affectation = { id: 29609 };
      vi.spyOn(affectationFormService, 'getAffectation').mockReturnValue({ id: null });
      vi.spyOn(affectationService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(affectation);
      saveSubject.complete();

      // THEN
      expect(affectationFormService.getAffectation).toHaveBeenCalled();
      expect(affectationService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IAffectation>();
      const affectation = { id: 29609 };
      vi.spyOn(affectationService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ affectation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(affectationService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('should forward to userService', () => {
        const entity = { id: 3944 };
        const entity2 = { id: 6275 };
        vi.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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
