import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ActifDetail } from './actif-detail';

describe('Actif Management Detail Component', () => {
  let comp: ActifDetail;
  let fixture: ComponentFixture<ActifDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./actif-detail').then(m => m.ActifDetail),
              resolve: { actif: () => of({ id: 3500 }) },
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
    fixture = TestBed.createComponent(ActifDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load actif on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ActifDetail);

      // THEN
      expect(instance.actif()).toEqual(expect.objectContaining({ id: 3500 }));
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
