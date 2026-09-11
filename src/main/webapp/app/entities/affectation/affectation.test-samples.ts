import dayjs from 'dayjs/esm';

import { IAffectation, NewAffectation } from './affectation.model';

export const sampleWithRequiredData: IAffectation = {
  id: 14206,
  dateAffectation: dayjs('2026-09-02'),
};

export const sampleWithPartialData: IAffectation = {
  id: 31191,
  dateAffectation: dayjs('2026-09-02'),
  motif: 'pff au cas où',
  dateRestitution: dayjs('2026-09-02'),
};

export const sampleWithFullData: IAffectation = {
  id: 5705,
  dateAffectation: dayjs('2026-09-02'),
  motif: 'au dépens de',
  dateRestitution: dayjs('2026-09-02'),
};

export const sampleWithNewData: NewAffectation = {
  dateAffectation: dayjs('2026-09-01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
