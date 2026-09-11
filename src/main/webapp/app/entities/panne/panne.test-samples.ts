import dayjs from 'dayjs/esm';

import { IPanne, NewPanne } from './panne.model';

export const sampleWithRequiredData: IPanne = {
  id: 25317,
  description: 'subito frayer',
  dateDeclaration: dayjs('2026-09-10'),
  statutPanne: 'SIGNALEE',
};

export const sampleWithPartialData: IPanne = {
  id: 7593,
  description: "à l'entour de chut",
  dateDeclaration: dayjs('2026-09-10'),
  statutPanne: 'EN_COURS',
};

export const sampleWithFullData: IPanne = {
  id: 25874,
  description: 'fonctionnaire à peu près',
  dateDeclaration: dayjs('2026-09-10'),
  statutPanne: 'EN_COURS',
};

export const sampleWithNewData: NewPanne = {
  description: 'biathlète de peur que aussitôt que',
  dateDeclaration: dayjs('2026-09-10'),
  statutPanne: 'RESOLUE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
