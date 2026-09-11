import dayjs from 'dayjs/esm';

import { IBordereau, NewBordereau } from './bordereau.model';

export const sampleWithRequiredData: IBordereau = {
  id: 17413,
  numero: 'cocorico fade lunatique',
  dateEmission: dayjs('2026-09-09'),
  typeBordereau: 'TRANSFERT',
  statutValidation: 'EN_ATTENTE',
};

export const sampleWithPartialData: IBordereau = {
  id: 8339,
  numero: 'avouer',
  dateEmission: dayjs('2026-09-10'),
  typeBordereau: 'AFFECTATION',
  statutValidation: 'EN_ATTENTE',
};

export const sampleWithFullData: IBordereau = {
  id: 27735,
  numero: 'davantage parmi',
  dateEmission: dayjs('2026-09-10'),
  typeBordereau: 'TRANSFERT',
  statutValidation: 'REJETE',
  dateValidation: dayjs('2026-09-09'),
};

export const sampleWithNewData: NewBordereau = {
  numero: 'trop agréable',
  dateEmission: dayjs('2026-09-10'),
  typeBordereau: 'TRANSFERT',
  statutValidation: 'VALIDE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
