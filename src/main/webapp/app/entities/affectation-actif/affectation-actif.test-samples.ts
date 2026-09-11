import { IAffectationActif, NewAffectationActif } from './affectation-actif.model';

export const sampleWithRequiredData: IAffectationActif = {
  id: 26307,
  statut: 'CLOTUREE',
};

export const sampleWithPartialData: IAffectationActif = {
  id: 1083,
  observation: 'dire',
  statut: 'ACTIVE',
};

export const sampleWithFullData: IAffectationActif = {
  id: 1183,
  observation: 'méconnaître près jadis',
  statut: 'CLOTUREE',
};

export const sampleWithNewData: NewAffectationActif = {
  statut: 'CLOTUREE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
