import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IBordereau } from '../bordereau.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bordereau.test-samples';

import { BordereauService, RestBordereau } from './bordereau.service';

const requireRestSample: RestBordereau = {
  ...sampleWithRequiredData,
  dateEmission: sampleWithRequiredData.dateEmission?.format(DATE_FORMAT),
  dateValidation: sampleWithRequiredData.dateValidation?.format(DATE_FORMAT),
};

describe('Bordereau Service', () => {
  let service: BordereauService;
  let httpMock: HttpTestingController;
  let expectedResult: IBordereau | IBordereau[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BordereauService);
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

    it('should create a Bordereau', () => {
      const bordereau = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bordereau).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bordereau', () => {
      const bordereau = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bordereau).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bordereau', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bordereau', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Bordereau', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addBordereauToCollectionIfMissing', () => {
      it('should add a Bordereau to an empty array', () => {
        const bordereau: IBordereau = sampleWithRequiredData;
        expectedResult = service.addBordereauToCollectionIfMissing([], bordereau);
        expect(expectedResult).toEqual([bordereau]);
      });

      it('should not add a Bordereau to an array that contains it', () => {
        const bordereau: IBordereau = sampleWithRequiredData;
        const bordereauCollection: IBordereau[] = [
          {
            ...bordereau,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBordereauToCollectionIfMissing(bordereauCollection, bordereau);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bordereau to an array that doesn't contain it", () => {
        const bordereau: IBordereau = sampleWithRequiredData;
        const bordereauCollection: IBordereau[] = [sampleWithPartialData];
        expectedResult = service.addBordereauToCollectionIfMissing(bordereauCollection, bordereau);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bordereau);
      });

      it('should add only unique Bordereau to an array', () => {
        const bordereauArray: IBordereau[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bordereauCollection: IBordereau[] = [sampleWithRequiredData];
        expectedResult = service.addBordereauToCollectionIfMissing(bordereauCollection, ...bordereauArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bordereau: IBordereau = sampleWithRequiredData;
        const bordereau2: IBordereau = sampleWithPartialData;
        expectedResult = service.addBordereauToCollectionIfMissing([], bordereau, bordereau2);
        expect(expectedResult).toEqual([bordereau, bordereau2]);
      });

      it('should accept null and undefined values', () => {
        const bordereau: IBordereau = sampleWithRequiredData;
        expectedResult = service.addBordereauToCollectionIfMissing([], null, bordereau, undefined);
        expect(expectedResult).toEqual([bordereau]);
      });

      it('should return initial array if no Bordereau is added', () => {
        const bordereauCollection: IBordereau[] = [sampleWithRequiredData];
        expectedResult = service.addBordereauToCollectionIfMissing(bordereauCollection, undefined, null);
        expect(expectedResult).toEqual(bordereauCollection);
      });
    });

    describe('compareBordereau', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBordereau(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19364 };
        const entity2 = null;

        const compareResult1 = service.compareBordereau(entity1, entity2);
        const compareResult2 = service.compareBordereau(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19364 };
        const entity2 = { id: 8037 };

        const compareResult1 = service.compareBordereau(entity1, entity2);
        const compareResult2 = service.compareBordereau(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 19364 };
        const entity2 = { id: 19364 };

        const compareResult1 = service.compareBordereau(entity1, entity2);
        const compareResult2 = service.compareBordereau(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
