import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IInventaire } from '../inventaire.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inventaire.test-samples';

import { InventaireService, RestInventaire } from './inventaire.service';

const requireRestSample: RestInventaire = {
  ...sampleWithRequiredData,
  dateImport: sampleWithRequiredData.dateImport?.format(DATE_FORMAT),
};

describe('Inventaire Service', () => {
  let service: InventaireService;
  let httpMock: HttpTestingController;
  let expectedResult: IInventaire | IInventaire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InventaireService);
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

    it('should create a Inventaire', () => {
      const inventaire = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inventaire).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Inventaire', () => {
      const inventaire = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inventaire).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Inventaire', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Inventaire', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Inventaire', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addInventaireToCollectionIfMissing', () => {
      it('should add a Inventaire to an empty array', () => {
        const inventaire: IInventaire = sampleWithRequiredData;
        expectedResult = service.addInventaireToCollectionIfMissing([], inventaire);
        expect(expectedResult).toEqual([inventaire]);
      });

      it('should not add a Inventaire to an array that contains it', () => {
        const inventaire: IInventaire = sampleWithRequiredData;
        const inventaireCollection: IInventaire[] = [
          {
            ...inventaire,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, inventaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Inventaire to an array that doesn't contain it", () => {
        const inventaire: IInventaire = sampleWithRequiredData;
        const inventaireCollection: IInventaire[] = [sampleWithPartialData];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, inventaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inventaire);
      });

      it('should add only unique Inventaire to an array', () => {
        const inventaireArray: IInventaire[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inventaireCollection: IInventaire[] = [sampleWithRequiredData];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, ...inventaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inventaire: IInventaire = sampleWithRequiredData;
        const inventaire2: IInventaire = sampleWithPartialData;
        expectedResult = service.addInventaireToCollectionIfMissing([], inventaire, inventaire2);
        expect(expectedResult).toEqual([inventaire, inventaire2]);
      });

      it('should accept null and undefined values', () => {
        const inventaire: IInventaire = sampleWithRequiredData;
        expectedResult = service.addInventaireToCollectionIfMissing([], null, inventaire, undefined);
        expect(expectedResult).toEqual([inventaire]);
      });

      it('should return initial array if no Inventaire is added', () => {
        const inventaireCollection: IInventaire[] = [sampleWithRequiredData];
        expectedResult = service.addInventaireToCollectionIfMissing(inventaireCollection, undefined, null);
        expect(expectedResult).toEqual(inventaireCollection);
      });
    });

    describe('compareInventaire', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInventaire(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3857 };
        const entity2 = null;

        const compareResult1 = service.compareInventaire(entity1, entity2);
        const compareResult2 = service.compareInventaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3857 };
        const entity2 = { id: 8000 };

        const compareResult1 = service.compareInventaire(entity1, entity2);
        const compareResult2 = service.compareInventaire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 3857 };
        const entity2 = { id: 3857 };

        const compareResult1 = service.compareInventaire(entity1, entity2);
        const compareResult2 = service.compareInventaire(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
