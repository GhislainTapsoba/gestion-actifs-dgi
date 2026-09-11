import dayjs from 'dayjs/esm';

import { IActif, NewActif } from './actif.model';

export const sampleWithRequiredData: IActif = {
  id: 22306,
  codeInventaire: "impromptu terne à l'instar de",
  designation: 'rouler avertir actionnaire',
  type: 'SERVEUR',
  etat: 'REFORME',
};

export const sampleWithPartialData: IActif = {
  id: 5491,
  codeInventaire: 'chef sauf à',
  designation: 'conseil municipal solitaire',
  codeBarre: 'autrefois',
  type: 'SERVEUR',
  etat: 'EN_SERVICE',
  dateAcquisition: dayjs('2026-09-02'),
};

export const sampleWithFullData: IActif = {
  id: 8983,
  codeInventaire: 'fleurir boum',
  designation: 'verger passablement',
  marque: 'vu que',
  modele: 'secouriste abîmer hebdomadaire',
  numeroSerie: 'debout étant donné que',
  codeBarre: 'secouriste pousser concurrence',
  type: 'POSTE_TRAVAIL',
  etat: 'EN_MAINTENANCE',
  localisation: 'durant approximativement',
  dateAcquisition: dayjs('2026-09-02'),
  valeurAcquisition: 14442.63,
};

export const sampleWithNewData: NewActif = {
  codeInventaire: 'touriste adapter vorace',
  designation: 'à force de',
  type: 'RESEAU',
  etat: 'EN_MAINTENANCE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
