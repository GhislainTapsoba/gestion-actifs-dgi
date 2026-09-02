import { afterEach, beforeEach, describe, expect, it } from 'vitest';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { DATE_FORMAT } from 'app/config';
import { IContrat } from '../contrat.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../contrat.test-samples';

import { ContratService, RestContrat } from './contrat.service';

const requireRestSample: RestContrat = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('Contrat Service', () => {
  let service: ContratService;
  let httpMock: HttpTestingController;
  let expectedResult: IContrat | IContrat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ContratService);
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

    it('should create a Contrat', () => {
      const contrat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(contrat).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Contrat', () => {
      const contrat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(contrat).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Contrat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Contrat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Contrat', () => {
      service.delete(123).subscribe();

      const requests = httpMock.match({ method: 'DELETE' });
      expect(requests).toHaveLength(1);
    });

    describe('addContratToCollectionIfMissing', () => {
      it('should add a Contrat to an empty array', () => {
        const contrat: IContrat = sampleWithRequiredData;
        expectedResult = service.addContratToCollectionIfMissing([], contrat);
        expect(expectedResult).toEqual([contrat]);
      });

      it('should not add a Contrat to an array that contains it', () => {
        const contrat: IContrat = sampleWithRequiredData;
        const contratCollection: IContrat[] = [
          {
            ...contrat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addContratToCollectionIfMissing(contratCollection, contrat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Contrat to an array that doesn't contain it", () => {
        const contrat: IContrat = sampleWithRequiredData;
        const contratCollection: IContrat[] = [sampleWithPartialData];
        expectedResult = service.addContratToCollectionIfMissing(contratCollection, contrat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(contrat);
      });

      it('should add only unique Contrat to an array', () => {
        const contratArray: IContrat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const contratCollection: IContrat[] = [sampleWithRequiredData];
        expectedResult = service.addContratToCollectionIfMissing(contratCollection, ...contratArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const contrat: IContrat = sampleWithRequiredData;
        const contrat2: IContrat = sampleWithPartialData;
        expectedResult = service.addContratToCollectionIfMissing([], contrat, contrat2);
        expect(expectedResult).toEqual([contrat, contrat2]);
      });

      it('should accept null and undefined values', () => {
        const contrat: IContrat = sampleWithRequiredData;
        expectedResult = service.addContratToCollectionIfMissing([], null, contrat, undefined);
        expect(expectedResult).toEqual([contrat]);
      });

      it('should return initial array if no Contrat is added', () => {
        const contratCollection: IContrat[] = [sampleWithRequiredData];
        expectedResult = service.addContratToCollectionIfMissing(contratCollection, undefined, null);
        expect(expectedResult).toEqual(contratCollection);
      });
    });

    describe('compareContrat', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareContrat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 7637 };
        const entity2 = null;

        const compareResult1 = service.compareContrat(entity1, entity2);
        const compareResult2 = service.compareContrat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 7637 };
        const entity2 = { id: 25812 };

        const compareResult1 = service.compareContrat(entity1, entity2);
        const compareResult2 = service.compareContrat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return true if primaryKey matches', () => {
        const entity1 = { id: 7637 };
        const entity2 = { id: 7637 };

        const compareResult1 = service.compareContrat(entity1, entity2);
        const compareResult2 = service.compareContrat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
