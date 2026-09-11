import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IEquipementRecensement } from '../equipement-recensement.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../equipement-recensement.test-samples';

import { EquipementRecensementService, RestEquipementRecensement } from './equipement-recensement.service';

const requireRestSample: RestEquipementRecensement = {
  ...sampleWithRequiredData,
  dateConstat: sampleWithRequiredData.dateConstat?.format(DATE_FORMAT),
};

describe('EquipementRecensement Service', () => {
  let service: EquipementRecensementService;
  let httpMock: HttpTestingController;
  let expectedResult: IEquipementRecensement | IEquipementRecensement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EquipementRecensementService);
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

    it('should create a EquipementRecensement', () => {
      const equipementRecensement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(equipementRecensement).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EquipementRecensement', () => {
      const equipementRecensement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(equipementRecensement).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EquipementRecensement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EquipementRecensement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EquipementRecensement', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addEquipementRecensementToCollectionIfMissing', () => {
      it('should add a EquipementRecensement to an empty array', () => {
        const equipementRecensement: IEquipementRecensement = sampleWithRequiredData;
        expectedResult = service.addEquipementRecensementToCollectionIfMissing([], equipementRecensement);
        expect(expectedResult).toEqual([equipementRecensement]);
      });

      it('should not add a EquipementRecensement to an array that contains it', () => {
        const equipementRecensement: IEquipementRecensement = sampleWithRequiredData;
        const equipementRecensementCollection: IEquipementRecensement[] = [
          {
            ...equipementRecensement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEquipementRecensementToCollectionIfMissing(equipementRecensementCollection, equipementRecensement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EquipementRecensement to an array that doesn't contain it", () => {
        const equipementRecensement: IEquipementRecensement = sampleWithRequiredData;
        const equipementRecensementCollection: IEquipementRecensement[] = [sampleWithPartialData];
        expectedResult = service.addEquipementRecensementToCollectionIfMissing(equipementRecensementCollection, equipementRecensement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(equipementRecensement);
      });

      it('should add only unique EquipementRecensement to an array', () => {
        const equipementRecensementArray: IEquipementRecensement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const equipementRecensementCollection: IEquipementRecensement[] = [sampleWithRequiredData];
        expectedResult = service.addEquipementRecensementToCollectionIfMissing(
          equipementRecensementCollection,
          ...equipementRecensementArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const equipementRecensement: IEquipementRecensement = sampleWithRequiredData;
        const equipementRecensement2: IEquipementRecensement = sampleWithPartialData;
        expectedResult = service.addEquipementRecensementToCollectionIfMissing([], equipementRecensement, equipementRecensement2);
        expect(expectedResult).toEqual([equipementRecensement, equipementRecensement2]);
      });

      it('should accept null and undefined values', () => {
        const equipementRecensement: IEquipementRecensement = sampleWithRequiredData;
        expectedResult = service.addEquipementRecensementToCollectionIfMissing([], null, equipementRecensement, undefined);
        expect(expectedResult).toEqual([equipementRecensement]);
      });

      it('should return initial array if no EquipementRecensement is added', () => {
        const equipementRecensementCollection: IEquipementRecensement[] = [sampleWithRequiredData];
        expectedResult = service.addEquipementRecensementToCollectionIfMissing(equipementRecensementCollection, undefined, null);
        expect(expectedResult).toEqual(equipementRecensementCollection);
      });
    });

    describe('compareEquipementRecensement', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEquipementRecensement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7919 };
        const entity2 = null;

        const compareResult1 = service.compareEquipementRecensement(entity1, entity2);
        const compareResult2 = service.compareEquipementRecensement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7919 };
        const entity2 = { id: 3026 };

        const compareResult1 = service.compareEquipementRecensement(entity1, entity2);
        const compareResult2 = service.compareEquipementRecensement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7919 };
        const entity2 = { id: 7919 };

        const compareResult1 = service.compareEquipementRecensement(entity1, entity2);
        const compareResult2 = service.compareEquipementRecensement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
