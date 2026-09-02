import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { ContratDetail } from './contrat-detail';

describe('Contrat Management Detail Component', () => {
  let comp: ContratDetail;
  let fixture: ComponentFixture<ContratDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./contrat-detail').then(m => m.ContratDetail),
              resolve: { contrat: () => of({ id: 7637 }) },
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
    fixture = TestBed.createComponent(ContratDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load contrat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ContratDetail);

      // THEN
      expect(instance.contrat()).toEqual(expect.objectContaining({ id: 7637 }));
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
