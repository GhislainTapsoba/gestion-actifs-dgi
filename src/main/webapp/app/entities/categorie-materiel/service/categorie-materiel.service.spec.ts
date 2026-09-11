import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { ICategorieMateriel } from '../categorie-materiel.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../categorie-materiel.test-samples';

import { CategorieMaterielService } from './categorie-materiel.service';

const requireRestSample: ICategorieMateriel = {
  ...sampleWithRequiredData,
};

describe('CategorieMateriel Service', () => {
  let service: CategorieMaterielService;
  let httpMock: HttpTestingController;
  let expectedResult: ICategorieMateriel | ICategorieMateriel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CategorieMaterielService);
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

    it('should create a CategorieMateriel', () => {
      const categorieMateriel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(categorieMateriel).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategorieMateriel', () => {
      const categorieMateriel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(categorieMateriel).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategorieMateriel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategorieMateriel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CategorieMateriel', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addCategorieMaterielToCollectionIfMissing', () => {
      it('should add a CategorieMateriel to an empty array', () => {
        const categorieMateriel: ICategorieMateriel = sampleWithRequiredData;
        expectedResult = service.addCategorieMaterielToCollectionIfMissing([], categorieMateriel);
        expect(expectedResult).toEqual([categorieMateriel]);
      });

      it('should not add a CategorieMateriel to an array that contains it', () => {
        const categorieMateriel: ICategorieMateriel = sampleWithRequiredData;
        const categorieMaterielCollection: ICategorieMateriel[] = [
          {
            ...categorieMateriel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCategorieMaterielToCollectionIfMissing(categorieMaterielCollection, categorieMateriel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategorieMateriel to an array that doesn't contain it", () => {
        const categorieMateriel: ICategorieMateriel = sampleWithRequiredData;
        const categorieMaterielCollection: ICategorieMateriel[] = [sampleWithPartialData];
        expectedResult = service.addCategorieMaterielToCollectionIfMissing(categorieMaterielCollection, categorieMateriel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categorieMateriel);
      });

      it('should add only unique CategorieMateriel to an array', () => {
        const categorieMaterielArray: ICategorieMateriel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const categorieMaterielCollection: ICategorieMateriel[] = [sampleWithRequiredData];
        expectedResult = service.addCategorieMaterielToCollectionIfMissing(categorieMaterielCollection, ...categorieMaterielArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categorieMateriel: ICategorieMateriel = sampleWithRequiredData;
        const categorieMateriel2: ICategorieMateriel = sampleWithPartialData;
        expectedResult = service.addCategorieMaterielToCollectionIfMissing([], categorieMateriel, categorieMateriel2);
        expect(expectedResult).toEqual([categorieMateriel, categorieMateriel2]);
      });

      it('should accept null and undefined values', () => {
        const categorieMateriel: ICategorieMateriel = sampleWithRequiredData;
        expectedResult = service.addCategorieMaterielToCollectionIfMissing([], null, categorieMateriel, undefined);
        expect(expectedResult).toEqual([categorieMateriel]);
      });

      it('should return initial array if no CategorieMateriel is added', () => {
        const categorieMaterielCollection: ICategorieMateriel[] = [sampleWithRequiredData];
        expectedResult = service.addCategorieMaterielToCollectionIfMissing(categorieMaterielCollection, undefined, null);
        expect(expectedResult).toEqual(categorieMaterielCollection);
      });
    });

    describe('compareCategorieMateriel', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCategorieMateriel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3013 };
        const entity2 = null;

        const compareResult1 = service.compareCategorieMateriel(entity1, entity2);
        const compareResult2 = service.compareCategorieMateriel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3013 };
        const entity2 = { id: 1809 };

        const compareResult1 = service.compareCategorieMateriel(entity1, entity2);
        const compareResult2 = service.compareCategorieMateriel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 3013 };
        const entity2 = { id: 3013 };

        const compareResult1 = service.compareCategorieMateriel(entity1, entity2);
        const compareResult2 = service.compareCategorieMateriel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
