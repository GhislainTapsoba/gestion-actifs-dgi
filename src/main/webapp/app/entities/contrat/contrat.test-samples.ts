import dayjs from 'dayjs/esm';

import { IContrat, NewContrat } from './contrat.model';

export const sampleWithRequiredData: IContrat = {
  id: 16857,
  typeContrat: 'GARANTIE',
  dateFin: dayjs('2026-09-01'),
};

export const sampleWithPartialData: IContrat = {
  id: 21890,
  typeContrat: 'MAINTENANCE',
  reference: 'casser',
  dateFin: dayjs('2026-09-02'),
};

export const sampleWithFullData: IContrat = {
  id: 4932,
  typeContrat: 'GARANTIE',
  reference: 'quoique',
  dateDebut: dayjs('2026-09-02'),
  dateFin: dayjs('2026-09-01'),
};

export const sampleWithNewData: NewContrat = {
  typeContrat: 'GARANTIE',
  dateFin: dayjs('2026-09-02'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
