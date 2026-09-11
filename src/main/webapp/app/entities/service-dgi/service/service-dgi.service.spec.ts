import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { IServiceDgi } from '../service-dgi.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../service-dgi.test-samples';

import { ServiceDgiService } from './service-dgi.service';

const requireRestSample: IServiceDgi = {
  ...sampleWithRequiredData,
};

describe('ServiceDgi Service', () => {
  let service: ServiceDgiService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceDgi | IServiceDgi[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceDgiService);
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

    it('should create a ServiceDgi', () => {
      const serviceDgi = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceDgi).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceDgi', () => {
      const serviceDgi = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceDgi).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceDgi', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceDgi', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceDgi', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addServiceDgiToCollectionIfMissing', () => {
      it('should add a ServiceDgi to an empty array', () => {
        const serviceDgi: IServiceDgi = sampleWithRequiredData;
        expectedResult = service.addServiceDgiToCollectionIfMissing([], serviceDgi);
        expect(expectedResult).toEqual([serviceDgi]);
      });

      it('should not add a ServiceDgi to an array that contains it', () => {
        const serviceDgi: IServiceDgi = sampleWithRequiredData;
        const serviceDgiCollection: IServiceDgi[] = [
          {
            ...serviceDgi,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceDgiToCollectionIfMissing(serviceDgiCollection, serviceDgi);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceDgi to an array that doesn't contain it", () => {
        const serviceDgi: IServiceDgi = sampleWithRequiredData;
        const serviceDgiCollection: IServiceDgi[] = [sampleWithPartialData];
        expectedResult = service.addServiceDgiToCollectionIfMissing(serviceDgiCollection, serviceDgi);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceDgi);
      });

      it('should add only unique ServiceDgi to an array', () => {
        const serviceDgiArray: IServiceDgi[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceDgiCollection: IServiceDgi[] = [sampleWithRequiredData];
        expectedResult = service.addServiceDgiToCollectionIfMissing(serviceDgiCollection, ...serviceDgiArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceDgi: IServiceDgi = sampleWithRequiredData;
        const serviceDgi2: IServiceDgi = sampleWithPartialData;
        expectedResult = service.addServiceDgiToCollectionIfMissing([], serviceDgi, serviceDgi2);
        expect(expectedResult).toEqual([serviceDgi, serviceDgi2]);
      });

      it('should accept null and undefined values', () => {
        const serviceDgi: IServiceDgi = sampleWithRequiredData;
        expectedResult = service.addServiceDgiToCollectionIfMissing([], null, serviceDgi, undefined);
        expect(expectedResult).toEqual([serviceDgi]);
      });

      it('should return initial array if no ServiceDgi is added', () => {
        const serviceDgiCollection: IServiceDgi[] = [sampleWithRequiredData];
        expectedResult = service.addServiceDgiToCollectionIfMissing(serviceDgiCollection, undefined, null);
        expect(expectedResult).toEqual(serviceDgiCollection);
      });
    });

    describe('compareServiceDgi', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceDgi(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3925 };
        const entity2 = null;

        const compareResult1 = service.compareServiceDgi(entity1, entity2);
        const compareResult2 = service.compareServiceDgi(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3925 };
        const entity2 = { id: 4731 };

        const compareResult1 = service.compareServiceDgi(entity1, entity2);
        const compareResult2 = service.compareServiceDgi(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 3925 };
        const entity2 = { id: 3925 };

        const compareResult1 = service.compareServiceDgi(entity1, entity2);
        const compareResult2 = service.compareServiceDgi(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
