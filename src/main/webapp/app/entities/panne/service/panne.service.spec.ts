import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IPanne } from '../panne.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../panne.test-samples';

import { PanneService, RestPanne } from './panne.service';

const requireRestSample: RestPanne = {
  ...sampleWithRequiredData,
  dateDeclaration: sampleWithRequiredData.dateDeclaration?.format(DATE_FORMAT),
};

describe('Panne Service', () => {
  let service: PanneService;
  let httpMock: HttpTestingController;
  let expectedResult: IPanne | IPanne[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PanneService);
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

    it('should create a Panne', () => {
      const panne = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(panne).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Panne', () => {
      const panne = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(panne).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Panne', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Panne', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Panne', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addPanneToCollectionIfMissing', () => {
      it('should add a Panne to an empty array', () => {
        const panne: IPanne = sampleWithRequiredData;
        expectedResult = service.addPanneToCollectionIfMissing([], panne);
        expect(expectedResult).toEqual([panne]);
      });

      it('should not add a Panne to an array that contains it', () => {
        const panne: IPanne = sampleWithRequiredData;
        const panneCollection: IPanne[] = [
          {
            ...panne,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPanneToCollectionIfMissing(panneCollection, panne);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Panne to an array that doesn't contain it", () => {
        const panne: IPanne = sampleWithRequiredData;
        const panneCollection: IPanne[] = [sampleWithPartialData];
        expectedResult = service.addPanneToCollectionIfMissing(panneCollection, panne);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(panne);
      });

      it('should add only unique Panne to an array', () => {
        const panneArray: IPanne[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const panneCollection: IPanne[] = [sampleWithRequiredData];
        expectedResult = service.addPanneToCollectionIfMissing(panneCollection, ...panneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const panne: IPanne = sampleWithRequiredData;
        const panne2: IPanne = sampleWithPartialData;
        expectedResult = service.addPanneToCollectionIfMissing([], panne, panne2);
        expect(expectedResult).toEqual([panne, panne2]);
      });

      it('should accept null and undefined values', () => {
        const panne: IPanne = sampleWithRequiredData;
        expectedResult = service.addPanneToCollectionIfMissing([], null, panne, undefined);
        expect(expectedResult).toEqual([panne]);
      });

      it('should return initial array if no Panne is added', () => {
        const panneCollection: IPanne[] = [sampleWithRequiredData];
        expectedResult = service.addPanneToCollectionIfMissing(panneCollection, undefined, null);
        expect(expectedResult).toEqual(panneCollection);
      });
    });

    describe('comparePanne', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePanne(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5747 };
        const entity2 = null;

        const compareResult1 = service.comparePanne(entity1, entity2);
        const compareResult2 = service.comparePanne(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5747 };
        const entity2 = { id: 32103 };

        const compareResult1 = service.comparePanne(entity1, entity2);
        const compareResult2 = service.comparePanne(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 5747 };
        const entity2 = { id: 5747 };

        const compareResult1 = service.comparePanne(entity1, entity2);
        const compareResult2 = service.comparePanne(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
