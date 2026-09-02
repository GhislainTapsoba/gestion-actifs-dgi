import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { AffectationDetail } from './affectation-detail';

describe('Affectation Management Detail Component', () => {
  let comp: AffectationDetail;
  let fixture: ComponentFixture<AffectationDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./affectation-detail').then(m => m.AffectationDetail),
              resolve: { affectation: () => of({ id: 29609 }) },
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
    fixture = TestBed.createComponent(AffectationDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load affectation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AffectationDetail);

      // THEN
      expect(instance.affectation()).toEqual(expect.objectContaining({ id: 29609 }));
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
