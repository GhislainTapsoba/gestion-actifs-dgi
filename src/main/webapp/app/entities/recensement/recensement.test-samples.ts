import dayjs from 'dayjs/esm';

import { IRecensement, NewRecensement } from './recensement.model';

export const sampleWithRequiredData: IRecensement = {
  id: 23648,
  dateDebut: dayjs('2026-09-10'),
  statut: 'EN_COURS',
};

export const sampleWithPartialData: IRecensement = {
  id: 32026,
  dateDebut: dayjs('2026-09-09'),
  statut: 'CLOTUREE',
};

export const sampleWithFullData: IRecensement = {
  id: 9675,
  dateDebut: dayjs('2026-09-10'),
  dateFin: dayjs('2026-09-10'),
  statut: 'EN_COURS',
};

export const sampleWithNewData: NewRecensement = {
  dateDebut: dayjs('2026-09-09'),
  statut: 'PLANIFIER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
