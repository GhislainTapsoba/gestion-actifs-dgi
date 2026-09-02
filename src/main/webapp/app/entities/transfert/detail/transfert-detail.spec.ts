import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { TransfertDetail } from './transfert-detail';

describe('Transfert Management Detail Component', () => {
  let comp: TransfertDetail;
  let fixture: ComponentFixture<TransfertDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./transfert-detail').then(m => m.TransfertDetail),
              resolve: { transfert: () => of({ id: 22898 }) },
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
    fixture = TestBed.createComponent(TransfertDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load transfert on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TransfertDetail);

      // THEN
      expect(instance.transfert()).toEqual(expect.objectContaining({ id: 22898 }));
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
