import dayjs from 'dayjs/esm';

import { IActif, NewActif } from './actif.model';

export const sampleWithRequiredData: IActif = {
  id: 22306,
  identifiantUnique: "impromptu terne à l'instar de",
  type: 'RESEAU',
  etat: 'EN_SERVICE',
};

export const sampleWithPartialData: IActif = {
  id: 4495,
  identifiantUnique: 'étant donné que main-d’œuvre jaillir',
  type: 'RESEAU',
  etat: 'REFORME',
};

export const sampleWithFullData: IActif = {
  id: 8983,
  identifiantUnique: 'fleurir boum',
  codeBarreQR: 'verger passablement',
  type: 'IMPRIMANTE',
  etat: 'EN_SERVICE',
  localisation: 'après que',
  dateAcquisition: dayjs('2026-09-01'),
};

export const sampleWithNewData: NewActif = {
  identifiantUnique: 'touriste adapter vorace',
  type: 'IMPRIMANTE',
  etat: 'EN_MAINTENANCE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
