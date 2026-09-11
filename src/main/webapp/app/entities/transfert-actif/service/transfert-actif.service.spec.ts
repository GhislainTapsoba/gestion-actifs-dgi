import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ITransfertActif } from '../transfert-actif.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../transfert-actif.test-samples';

import { TransfertActifService } from './transfert-actif.service';

const requireRestSample: ITransfertActif = {
  ...sampleWithRequiredData,
};

describe('TransfertActif Service', () => {
  let service: TransfertActifService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransfertActif | ITransfertActif[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TransfertActifService);
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

    it('should create a TransfertActif', () => {
      const transfertActif = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transfertActif).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TransfertActif', () => {
      const transfertActif = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transfertActif).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TransfertActif', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TransfertActif', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TransfertActif', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addTransfertActifToCollectionIfMissing', () => {
      it('should add a TransfertActif to an empty array', () => {
        const transfertActif: ITransfertActif = sampleWithRequiredData;
        expectedResult = service.addTransfertActifToCollectionIfMissing([], transfertActif);
        expect(expectedResult).toEqual([transfertActif]);
      });

      it('should not add a TransfertActif to an array that contains it', () => {
        const transfertActif: ITransfertActif = sampleWithRequiredData;
        const transfertActifCollection: ITransfertActif[] = [
          {
            ...transfertActif,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransfertActifToCollectionIfMissing(transfertActifCollection, transfertActif);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TransfertActif to an array that doesn't contain it", () => {
        const transfertActif: ITransfertActif = sampleWithRequiredData;
        const transfertActifCollection: ITransfertActif[] = [sampleWithPartialData];
        expectedResult = service.addTransfertActifToCollectionIfMissing(transfertActifCollection, transfertActif);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transfertActif);
      });

      it('should add only unique TransfertActif to an array', () => {
        const transfertActifArray: ITransfertActif[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transfertActifCollection: ITransfertActif[] = [sampleWithRequiredData];
        expectedResult = service.addTransfertActifToCollectionIfMissing(transfertActifCollection, ...transfertActifArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transfertActif: ITransfertActif = sampleWithRequiredData;
        const transfertActif2: ITransfertActif = sampleWithPartialData;
        expectedResult = service.addTransfertActifToCollectionIfMissing([], transfertActif, transfertActif2);
        expect(expectedResult).toEqual([transfertActif, transfertActif2]);
      });

      it('should accept null and undefined values', () => {
        const transfertActif: ITransfertActif = sampleWithRequiredData;
        expectedResult = service.addTransfertActifToCollectionIfMissing([], null, transfertActif, undefined);
        expect(expectedResult).toEqual([transfertActif]);
      });

      it('should return initial array if no TransfertActif is added', () => {
        const transfertActifCollection: ITransfertActif[] = [sampleWithRequiredData];
        expectedResult = service.addTransfertActifToCollectionIfMissing(transfertActifCollection, undefined, null);
        expect(expectedResult).toEqual(transfertActifCollection);
      });
    });

    describe('compareTransfertActif', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransfertActif(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 4253 };
        const entity2 = null;

        const compareResult1 = service.compareTransfertActif(entity1, entity2);
        const compareResult2 = service.compareTransfertActif(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 4253 };
        const entity2 = { id: 3986 };

        const compareResult1 = service.compareTransfertActif(entity1, entity2);
        const compareResult2 = service.compareTransfertActif(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 4253 };
        const entity2 = { id: 4253 };

        const compareResult1 = service.compareTransfertActif(entity1, entity2);
        const compareResult2 = service.compareTransfertActif(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
