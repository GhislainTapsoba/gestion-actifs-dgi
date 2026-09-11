import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { ServiceDgiService } from 'app/entities/service-dgi/service/service-dgi.service';
import { IServiceDgi } from 'app/entities/service-dgi/service-dgi.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { TransfertService } from '../service/transfert.service';
import { ITransfert } from '../transfert.model';

import { TransfertFormService } from './transfert-form.service';
import { TransfertUpdate } from './transfert-update';

describe('Transfert Management Update Component', () => {
  let comp: TransfertUpdate;
  let fixture: ComponentFixture<TransfertUpdate>;
  let activatedRoute: ActivatedRoute;
  let transfertFormService: TransfertFormService;
  let transfertService: TransfertService;
  let serviceDgiService: ServiceDgiService;
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

    fixture = TestBed.createComponent(TransfertUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    transfertFormService = TestBed.inject(TransfertFormService);
    transfertService = TestBed.inject(TransfertService);
    serviceDgiService = TestBed.inject(ServiceDgiService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call ServiceDgi query and add missing value', () => {
      const transfert: ITransfert = { id: 14214 };
      const serviceOrigine: IServiceDgi = { id: 3925 };
      transfert.serviceOrigine = serviceOrigine;
      const serviceDestinataire: IServiceDgi = { id: 3925 };
      transfert.serviceDestinataire = serviceDestinataire;

      const serviceDgiCollection: IServiceDgi[] = [{ id: 3925 }];
      vi.spyOn(serviceDgiService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceDgiCollection })));
      const additionalServiceDgis = [serviceOrigine, serviceDestinataire];
      const expectedCollection: IServiceDgi[] = [...additionalServiceDgis, ...serviceDgiCollection];
      vi.spyOn(serviceDgiService, 'addServiceDgiToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      expect(serviceDgiService.query).toHaveBeenCalled();
      expect(serviceDgiService.addServiceDgiToCollectionIfMissing).toHaveBeenCalledWith(
        serviceDgiCollection,
        ...additionalServiceDgis.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.serviceDgisSharedCollection()).toEqual(expectedCollection);
    });

    it('should call User query and add missing value', () => {
      const transfert: ITransfert = { id: 14214 };
      const demandeur: IUser = { id: 3944 };
      transfert.demandeur = demandeur;
      const validateur: IUser = { id: 3944 };
      transfert.validateur = validateur;

      const userCollection: IUser[] = [{ id: 3944 }];
      vi.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [demandeur, validateur];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      vi.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.usersSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const transfert: ITransfert = { id: 14214 };
      const serviceOrigine: IServiceDgi = { id: 3925 };
      transfert.serviceOrigine = serviceOrigine;
      const serviceDestinataire: IServiceDgi = { id: 3925 };
      transfert.serviceDestinataire = serviceDestinataire;
      const demandeur: IUser = { id: 3944 };
      transfert.demandeur = demandeur;
      const validateur: IUser = { id: 3944 };
      transfert.validateur = validateur;

      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      expect(comp.serviceDgisSharedCollection()).toContainEqual(serviceOrigine);
      expect(comp.serviceDgisSharedCollection()).toContainEqual(serviceDestinataire);
      expect(comp.usersSharedCollection()).toContainEqual(demandeur);
      expect(comp.usersSharedCollection()).toContainEqual(validateur);
      expect(comp.transfert).toEqual(transfert);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransfert>();
      const transfert = { id: 22898 };
      vi.spyOn(transfertFormService, 'getTransfert').mockReturnValue(transfert);
      vi.spyOn(transfertService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transfert);
      saveSubject.complete();

      // THEN
      expect(transfertFormService.getTransfert).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(transfertService.update).toHaveBeenCalledWith(expect.objectContaining(transfert));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<ITransfert>();
      const transfert = { id: 22898 };
      vi.spyOn(transfertFormService, 'getTransfert').mockReturnValue({ id: null });
      vi.spyOn(transfertService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfert: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(transfert);
      saveSubject.complete();

      // THEN
      expect(transfertFormService.getTransfert).toHaveBeenCalled();
      expect(transfertService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<ITransfert>();
      const transfert = { id: 22898 };
      vi.spyOn(transfertService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ transfert });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(transfertService.update).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceDgi', () => {
      it('should forward to serviceDgiService', () => {
        const entity = { id: 3925 };
        const entity2 = { id: 4731 };
        vi.spyOn(serviceDgiService, 'compareServiceDgi');
        comp.compareServiceDgi(entity, entity2);
        expect(serviceDgiService.compareServiceDgi).toHaveBeenCalledWith(entity, entity2);
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
