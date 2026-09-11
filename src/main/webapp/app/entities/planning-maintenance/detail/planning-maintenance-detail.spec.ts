import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';

import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faArrowLeft, faPencilAlt } from '@fortawesome/free-solid-svg-icons';
import { provideTranslateService } from '@ngx-translate/core';
import { of } from 'rxjs';

import { PlanningMaintenanceDetail } from './planning-maintenance-detail';

describe('PlanningMaintenance Management Detail Component', () => {
  let comp: PlanningMaintenanceDetail;
  let fixture: ComponentFixture<PlanningMaintenanceDetail>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideTranslateService(),
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./planning-maintenance-detail').then(m => m.PlanningMaintenanceDetail),
              resolve: { planningMaintenance: () => of({ id: 20630 }) },
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
    fixture = TestBed.createComponent(PlanningMaintenanceDetail);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load planningMaintenance on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlanningMaintenanceDetail);

      // THEN
      expect(instance.planningMaintenance()).toEqual(expect.objectContaining({ id: 20630 }));
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
