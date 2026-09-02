import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IMaintenance } from '../maintenance.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../maintenance.test-samples';

import { MaintenanceService, RestMaintenance } from './maintenance.service';

const requireRestSample: RestMaintenance = {
  ...sampleWithRequiredData,
  datePanne: sampleWithRequiredData.datePanne?.format(DATE_FORMAT),
  dateCloture: sampleWithRequiredData.dateCloture?.format(DATE_FORMAT),
};

describe('Maintenance Service', () => {
  let service: MaintenanceService;
  let httpMock: HttpTestingController;
  let expectedResult: IMaintenance | IMaintenance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MaintenanceService);
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

    it('should create a Maintenance', () => {
      const maintenance = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(maintenance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Maintenance', () => {
      const maintenance = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(maintenance).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Maintenance', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Maintenance', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Maintenance', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addMaintenanceToCollectionIfMissing', () => {
      it('should add a Maintenance to an empty array', () => {
        const maintenance: IMaintenance = sampleWithRequiredData;
        expectedResult = service.addMaintenanceToCollectionIfMissing([], maintenance);
        expect(expectedResult).toEqual([maintenance]);
      });

      it('should not add a Maintenance to an array that contains it', () => {
        const maintenance: IMaintenance = sampleWithRequiredData;
        const maintenanceCollection: IMaintenance[] = [
          {
            ...maintenance,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMaintenanceToCollectionIfMissing(maintenanceCollection, maintenance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Maintenance to an array that doesn't contain it", () => {
        const maintenance: IMaintenance = sampleWithRequiredData;
        const maintenanceCollection: IMaintenance[] = [sampleWithPartialData];
        expectedResult = service.addMaintenanceToCollectionIfMissing(maintenanceCollection, maintenance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maintenance);
      });

      it('should add only unique Maintenance to an array', () => {
        const maintenanceArray: IMaintenance[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const maintenanceCollection: IMaintenance[] = [sampleWithRequiredData];
        expectedResult = service.addMaintenanceToCollectionIfMissing(maintenanceCollection, ...maintenanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maintenance: IMaintenance = sampleWithRequiredData;
        const maintenance2: IMaintenance = sampleWithPartialData;
        expectedResult = service.addMaintenanceToCollectionIfMissing([], maintenance, maintenance2);
        expect(expectedResult).toEqual([maintenance, maintenance2]);
      });

      it('should accept null and undefined values', () => {
        const maintenance: IMaintenance = sampleWithRequiredData;
        expectedResult = service.addMaintenanceToCollectionIfMissing([], null, maintenance, undefined);
        expect(expectedResult).toEqual([maintenance]);
      });

      it('should return initial array if no Maintenance is added', () => {
        const maintenanceCollection: IMaintenance[] = [sampleWithRequiredData];
        expectedResult = service.addMaintenanceToCollectionIfMissing(maintenanceCollection, undefined, null);
        expect(expectedResult).toEqual(maintenanceCollection);
      });
    });

    describe('compareMaintenance', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMaintenance(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17111 };
        const entity2 = null;

        const compareResult1 = service.compareMaintenance(entity1, entity2);
        const compareResult2 = service.compareMaintenance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17111 };
        const entity2 = { id: 576 };

        const compareResult1 = service.compareMaintenance(entity1, entity2);
        const compareResult2 = service.compareMaintenance(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 17111 };
        const entity2 = { id: 17111 };

        const compareResult1 = service.compareMaintenance(entity1, entity2);
        const compareResult2 = service.compareMaintenance(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
