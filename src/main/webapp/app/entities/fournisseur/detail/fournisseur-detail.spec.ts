import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { FournisseurDetail } from './fournisseur-detail';

describe('Fournisseur Management Detail Component', () => {
  let comp: FournisseurDetail;
  let fixture: ComponentFixture<FournisseurDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./fournisseur-detail').then(m => m.FournisseurDetail),
              resolve: { fournisseur: () => of({ id: 25678 }) },
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
    fixture = TestBed.createComponent(FournisseurDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load fournisseur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FournisseurDetail);

      // THEN
      expect(instance.fournisseur()).toEqual(expect.objectContaining({ id: 25678 }));
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
