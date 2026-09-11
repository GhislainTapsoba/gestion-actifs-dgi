import { ITransfertActif, NewTransfertActif } from './transfert-actif.model';

export const sampleWithRequiredData: ITransfertActif = {
  id: 17766,
};

export const sampleWithPartialData: ITransfertActif = {
  id: 12114,
};

export const sampleWithFullData: ITransfertActif = {
  id: 9108,
  observation: 'à même rectorat sursauter',
};

export const sampleWithNewData: NewTransfertActif = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
