import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { EquipementRecensementDetail } from './equipement-recensement-detail';

describe('EquipementRecensement Management Detail Component', () => {
  let comp: EquipementRecensementDetail;
  let fixture: ComponentFixture<EquipementRecensementDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./equipement-recensement-detail').then(m => m.EquipementRecensementDetail),
              resolve: { equipementRecensement: () => of({ id: 7919 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    });
    const library = TestBed.inject(FaIconLibrary);
    library.addIcons(faArrowLeft);
    library.addIcons(faPencilAlt);
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EquipementRecensementDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load equipementRecensement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EquipementRecensementDetail);

      // THEN
      expect(instance.equipementRecensement()).toEqual(expect.objectContaining({ id: 7919 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      vi.spyOn(globalThis.history, 'back');
      comp.previousState();
      expect(globalThis.history.back).toHaveBeenCalled();
    });
  });
});
