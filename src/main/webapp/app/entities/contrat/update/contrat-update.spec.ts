import { beforeEach, describe, expect, it, vi } from 'vitest';
import { HttpResponse } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { provideTranslateService } from '@ngx-translate/core';
import { Subject, from, of } from 'rxjs';

import { IActif } from 'app/entities/actif/actif.model';
import { ActifService } from 'app/entities/actif/service/actif.service';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/service/fournisseur.service';
import { IContrat } from '../contrat.model';
import { ContratService } from '../service/contrat.service';

import { ContratFormService } from './contrat-form.service';
import { ContratUpdate } from './contrat-update';

describe('Contrat Management Update Component', () => {
  let comp: ContratUpdate;
  let fixture: ComponentFixture<ContratUpdate>;
  let activatedRoute: ActivatedRoute;
  let contratFormService: ContratFormService;
  let contratService: ContratService;
  let actifService: ActifService;
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

    fixture = TestBed.createComponent(ContratUpdate);
    activatedRoute = TestBed.inject(ActivatedRoute);
    contratFormService = TestBed.inject(ContratFormService);
    contratService = TestBed.inject(ContratService);
    actifService = TestBed.inject(ActifService);
    fournisseurService = TestBed.inject(FournisseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Actif query and add missing value', () => {
      const contrat: IContrat = { id: 25812 };
      const actif: IActif = { id: 3500 };
      contrat.actif = actif;

      const actifCollection: IActif[] = [{ id: 3500 }];
      vi.spyOn(actifService, 'query').mockReturnValue(of(new HttpResponse({ body: actifCollection })));
      const additionalActifs = [actif];
      const expectedCollection: IActif[] = [...additionalActifs, ...actifCollection];
      vi.spyOn(actifService, 'addActifToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(actifService.query).toHaveBeenCalled();
      expect(actifService.addActifToCollectionIfMissing).toHaveBeenCalledWith(
        actifCollection,
        ...additionalActifs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.actifsSharedCollection()).toEqual(expectedCollection);
    });

    it('should call Fournisseur query and add missing value', () => {
      const contrat: IContrat = { id: 25812 };
      const fournisseur: IFournisseur = { id: 25678 };
      contrat.fournisseur = fournisseur;

      const fournisseurCollection: IFournisseur[] = [{ id: 25678 }];
      vi.spyOn(fournisseurService, 'query').mockReturnValue(of(new HttpResponse({ body: fournisseurCollection })));
      const additionalFournisseurs = [fournisseur];
      const expectedCollection: IFournisseur[] = [...additionalFournisseurs, ...fournisseurCollection];
      vi.spyOn(fournisseurService, 'addFournisseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(fournisseurService.query).toHaveBeenCalled();
      expect(fournisseurService.addFournisseurToCollectionIfMissing).toHaveBeenCalledWith(
        fournisseurCollection,
        ...additionalFournisseurs.map(i => expect.objectContaining(i) as typeof i),
      );
      expect(comp.fournisseursSharedCollection()).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const contrat: IContrat = { id: 25812 };
      const actif: IActif = { id: 3500 };
      contrat.actif = actif;
      const fournisseur: IFournisseur = { id: 25678 };
      contrat.fournisseur = fournisseur;

      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      expect(comp.actifsSharedCollection()).toContainEqual(actif);
      expect(comp.fournisseursSharedCollection()).toContainEqual(fournisseur);
      expect(comp.contrat).toEqual(contrat);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContrat>();
      const contrat = { id: 7637 };
      vi.spyOn(contratFormService, 'getContrat').mockReturnValue(contrat);
      vi.spyOn(contratService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contrat);
      saveSubject.complete();

      // THEN
      expect(contratFormService.getContrat).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(contratService.update).toHaveBeenCalledWith(expect.objectContaining(contrat));
      expect(comp.isSaving()).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<IContrat>();
      const contrat = { id: 7637 };
      vi.spyOn(contratFormService, 'getContrat').mockReturnValue({ id: null });
      vi.spyOn(contratService, 'create').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.next(contrat);
      saveSubject.complete();

      // THEN
      expect(contratFormService.getContrat).toHaveBeenCalled();
      expect(contratService.create).toHaveBeenCalled();
      expect(comp.isSaving()).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<IContrat>();
      const contrat = { id: 7637 };
      vi.spyOn(contratService, 'update').mockReturnValue(saveSubject);
      vi.spyOn(comp, 'previousState');
      activatedRoute.data = of({ contrat });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving()).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(contratService.update).toHaveBeenCalled();
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

    describe('compareFournisseur', () => {
      it('should forward to fournisseurService', () => {
        const entity = { id: 25678 };
        const entity2 = { id: 3552 };
        vi.spyOn(fournisseurService, 'compareFournisseur');
        comp.compareFournisseur(entity, entity2);
        expect(fournisseurService.compareFournisseur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
