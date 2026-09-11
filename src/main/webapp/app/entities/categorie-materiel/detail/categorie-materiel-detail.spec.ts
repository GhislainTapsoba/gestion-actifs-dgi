import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { CategorieMaterielDetail } from './categorie-materiel-detail';

describe('CategorieMateriel Management Detail Component', () => {
  let comp: CategorieMaterielDetail;
  let fixture: ComponentFixture<CategorieMaterielDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./categorie-materiel-detail').then(m => m.CategorieMaterielDetail),
              resolve: { categorieMateriel: () => of({ id: 3013 }) },
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
    fixture = TestBed.createComponent(CategorieMaterielDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load categorieMateriel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CategorieMaterielDetail);

      // THEN
      expect(instance.categorieMateriel()).toEqual(expect.objectContaining({ id: 3013 }));
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
