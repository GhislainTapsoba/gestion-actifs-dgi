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
import { IMaintenance } from '../maintenance.model';
import { MaintenanceService } from '../service/maintenance.service';

import { MaintenanceFormService } from './maintenance-form.service';
import { MaintenanceUpdate } from './maintenance-update';

describe('Maintenance Management Update Component', () => {
  let comp: MaintenanceUpdate;
  let fixture: ComponentFixture<MaintenanceUpdate>;
  let activatedRoute: ActivatedRoute;
  let maintenanceFormService: MaintenanceFormService;
  let maintenanceService: MaintenanceService;
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

    fixture = TestBed.createComponent(MaintenanceUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    maintenanceFormService = TestBed.inject(MaintenanceFormService);
    maintenanceService = TestBed.inject(MaintenanceService);
    userService = TestBed.inject(UserService);
    actifService = TestBed.inject(ActifService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call User query and add missing value', () => {
      const maintenance: IMaintenance = { id: 576 };
      const technicien: IUser = { id: 3944 };
      maintenance.technicien = technicien;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [technicien];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Actif query and add missing value', () => {
      const maintenance: IMaintenance = { id: 576 };
      const actif: IActif = { id: 3500 };
      maintenance.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const maintenance: IMaintenance = { id: 576 };
      const technicien: IUser = { id: 3944 };
      maintenance.technicien = technicien;
      const actif: IActif = { id: 3500 };
      maintenance.actif = actif;

      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      expect(comp.usersSharedCollection()).toContainEqual(technicien);
      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.maintenance).toEqual(maintenance);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMaintenance>();
      const maintenance = { id: 17111 };
      vi.spyOn(maintenanceFormService, 'getMaintenance').mockReturnValue(maintenance);
      vi.spyOn(maintenanceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(maintenance);
      saveSubject.complete();

      // THEN
      expect(maintenanceFormService.getMaintenance).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(maintenanceService.update).toHaveBeenCalledWith(expect.objectContaining(maintenance));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IMaintenance>();
      const maintenance = { id: 17111 };
      vi.spyOn(maintenanceFormService, 'getMaintenance').mockReturnValue({ id: null });
      vi.spyOn(maintenanceService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenance: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(maintenance);
      saveSubject.complete();

      // THEN
      expect(maintenanceFormService.getMaintenance).toHaveBeenCalled();
      expect(maintenanceService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IMaintenance>();
      const maintenance = { id: 17111 };
      vi.spyOn(maintenanceService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ maintenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(maintenanceService.update).toHaveBeenCalled();
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
