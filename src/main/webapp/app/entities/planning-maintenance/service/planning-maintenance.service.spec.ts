import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IPlanningMaintenance } from '../planning-maintenance.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../planning-maintenance.test-samples';

import { PlanningMaintenanceService, RestPlanningMaintenance } from './planning-maintenance.service';

const requireRestSample: RestPlanningMaintenance = {
  ...sampleWithRequiredData,
  datePrevue: sampleWithRequiredData.datePrevue?.format(DATE_FORMAT),
};

describe('PlanningMaintenance Service', () => {
  let service: PlanningMaintenanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlanningMaintenance | IPlanningMaintenance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlanningMaintenanceService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PlanningMaintenance', () => {
      const planningMaintenance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(planningMaintenance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlanningMaintenance', () => {
      const planningMaintenance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(planningMaintenance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlanningMaintenance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlanningMaintenance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlanningMaintenance', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPlanningMaintenanceToCollectionIfMissing', () => {
      it('should add a PlanningMaintenance to an empty array', () => {
        const planningMaintenance: IPlanningMaintenance = sampleWithRequiredData;
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing([], planningMaintenance);
        expect(expectedResult).toEqual([planningMaintenance]);
      });

      it('should not add a PlanningMaintenance to an array that contains it', () => {
        const planningMaintenance: IPlanningMaintenance = sampleWithRequiredData;
        const planningMaintenanceCollection: IPlanningMaintenance[] = [
          {
            ...planningMaintenance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing(planningMaintenanceCollection, planningMaintenance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlanningMaintenance to an array that doesn't contain it", () => {
        const planningMaintenance: IPlanningMaintenance = sampleWithRequiredData;
        const planningMaintenanceCollection: IPlanningMaintenance[] = [sampleWithPartialData];
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing(planningMaintenanceCollection, planningMaintenance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planningMaintenance);
      });

      it('should add only unique PlanningMaintenance to an array', () => {
        const planningMaintenanceArray: IPlanningMaintenance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const planningMaintenanceCollection: IPlanningMaintenance[] = [sampleWithRequiredData];
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing(planningMaintenanceCollection, ...planningMaintenanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const planningMaintenance: IPlanningMaintenance = sampleWithRequiredData;
        const planningMaintenance2: IPlanningMaintenance = sampleWithPartialData;
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing([], planningMaintenance, planningMaintenance2);
        expect(expectedResult).toEqual([planningMaintenance, planningMaintenance2]);
      });

      it('should accept null and undefined values', () => {
        const planningMaintenance: IPlanningMaintenance = sampleWithRequiredData;
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing([], null, planningMaintenance, undefined);
        expect(expectedResult).toEqual([planningMaintenance]);
      });

      it('should return initial array if no PlanningMaintenance is added', () => {
        const planningMaintenanceCollection: IPlanningMaintenance[] = [sampleWithRequiredData];
        expectedResult = service.addPlanningMaintenanceToCollectionIfMissing(planningMaintenanceCollection, undefined, null);
        expect(expectedResult).toEqual(planningMaintenanceCollection);
      });
    });

    describe('comparePlanningMaintenance', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlanningMaintenance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 20630 };
        const entity2 = null;

        const compareResult1 = service.comparePlanningMaintenance(entity1, entity2);
        const compareResult2 = service.comparePlanningMaintenance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 20630 };
        const entity2 = { id: 8418 };

        const compareResult1 = service.comparePlanningMaintenance(entity1, entity2);
        const compareResult2 = service.comparePlanningMaintenance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 20630 };
        const entity2 = { id: 20630 };

        const compareResult1 = service.comparePlanningMaintenance(entity1, entity2);
        const compareResult2 = service.comparePlanningMaintenance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
