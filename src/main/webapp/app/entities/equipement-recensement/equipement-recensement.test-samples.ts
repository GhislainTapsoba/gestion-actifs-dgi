import dayjs from 'dayjs/esm';

import { IEquipementRecensement, NewEquipementRecensement } from './equipement-recensement.model';

export const sampleWithRequiredData: IEquipementRecensement = {
  id: 16202,
  etatConstate: 'EN_PANNE',
  dateConstat: dayjs('2026-09-09'),
};

export const sampleWithPartialData: IEquipementRecensement = {
  id: 10192,
  etatConstate: 'REFORME',
  dateConstat: dayjs('2026-09-10'),
  emplacementConstate: 'envahir',
};

export const sampleWithFullData: IEquipementRecensement = {
  id: 8692,
  etatConstate: 'EN_SERVICE',
  dateConstat: dayjs('2026-09-09'),
  emplacementConstate: 'reproduire aborder',
  anomalieConstatee: false,
};

export const sampleWithNewData: NewEquipementRecensement = {
  etatConstate: 'REFORME',
  dateConstat: dayjs('2026-09-10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
