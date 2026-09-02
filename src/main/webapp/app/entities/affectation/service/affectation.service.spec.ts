import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IAffectation } from '../affectation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../affectation.test-samples';

import { AffectationService, RestAffectation } from './affectation.service';

const requireRestSample: RestAffectation = {
  ...sampleWithRequiredData,
  dateAffectation: sampleWithRequiredData.dateAffectation?.format(DATE_FORMAT),
  dateRestitution: sampleWithRequiredData.dateRestitution?.format(DATE_FORMAT),
};

describe('Affectation Service', () => {
  let service: AffectationService;
  let httpMock: HttpTestingController;
  let expectedResult: IAffectation | IAffectation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AffectationService);
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

    it('should create a Affectation', () => {
      const affectation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(affectation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Affectation', () => {
      const affectation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(affectation).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Affectation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Affectation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Affectation', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addAffectationToCollectionIfMissing', () => {
      it('should add a Affectation to an empty array', () => {
        const affectation: IAffectation = sampleWithRequiredData;
        expectedResult = service.addAffectationToCollectionIfMissing([], affectation);
        expect(expectedResult).toEqual([affectation]);
      });

      it('should not add a Affectation to an array that contains it', () => {
        const affectation: IAffectation = sampleWithRequiredData;
        const affectationCollection: IAffectation[] = [
          {
            ...affectation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAffectationToCollectionIfMissing(affectationCollection, affectation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Affectation to an array that doesn't contain it", () => {
        const affectation: IAffectation = sampleWithRequiredData;
        const affectationCollection: IAffectation[] = [sampleWithPartialData];
        expectedResult = service.addAffectationToCollectionIfMissing(affectationCollection, affectation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(affectation);
      });

      it('should add only unique Affectation to an array', () => {
        const affectationArray: IAffectation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const affectationCollection: IAffectation[] = [sampleWithRequiredData];
        expectedResult = service.addAffectationToCollectionIfMissing(affectationCollection, ...affectationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const affectation: IAffectation = sampleWithRequiredData;
        const affectation2: IAffectation = sampleWithPartialData;
        expectedResult = service.addAffectationToCollectionIfMissing([], affectation, affectation2);
        expect(expectedResult).toEqual([affectation, affectation2]);
      });

      it('should accept null and undefined values', () => {
        const affectation: IAffectation = sampleWithRequiredData;
        expectedResult = service.addAffectationToCollectionIfMissing([], null, affectation, undefined);
        expect(expectedResult).toEqual([affectation]);
      });

      it('should return initial array if no Affectation is added', () => {
        const affectationCollection: IAffectation[] = [sampleWithRequiredData];
        expectedResult = service.addAffectationToCollectionIfMissing(affectationCollection, undefined, null);
        expect(expectedResult).toEqual(affectationCollection);
      });
    });

    describe('compareAffectation', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAffectation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29609 };
        const entity2 = null;

        const compareResult1 = service.compareAffectation(entity1, entity2);
        const compareResult2 = service.compareAffectation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29609 };
        const entity2 = { id: 22469 };

        const compareResult1 = service.compareAffectation(entity1, entity2);
        const compareResult2 = service.compareAffectation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 29609 };
        const entity2 = { id: 29609 };

        const compareResult1 = service.compareAffectation(entity1, entity2);
        const compareResult2 = service.compareAffectation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
