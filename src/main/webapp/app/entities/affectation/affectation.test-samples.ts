import dayjs from 'dayjs/esm';

import { IAffectation, NewAffectation } from './affectation.model';

export const sampleWithRequiredData: IAffectation = {
  id: 14206,
  dateAffectation: dayjs('2026-09-02'),
};

export const sampleWithPartialData: IAffectation = {
  id: 31191,
  dateAffectation: dayjs('2026-09-02'),
  dateRestitution: dayjs('2026-09-02'),
  numeroBordereau: 'boum euh coin-coin',
};

export const sampleWithFullData: IAffectation = {
  id: 5705,
  dateAffectation: dayjs('2026-09-02'),
  dateRestitution: dayjs('2026-09-01'),
  numeroBordereau: 'de façon que équipe de recherche',
};

export const sampleWithNewData: NewAffectation = {
  dateAffectation: dayjs('2026-09-01'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
