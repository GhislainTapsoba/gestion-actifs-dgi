import dayjs from 'dayjs/esm';

import { IMaintenance, NewMaintenance } from './maintenance.model';

export const sampleWithRequiredData: IMaintenance = {
  id: 3097,
  typeMaintenance: 'CORRECTIVE',
  statut: 'CLOTUREE',
};

export const sampleWithPartialData: IMaintenance = {
  id: 23337,
  typeMaintenance: 'CORRECTIVE',
  statut: 'OUVERTE',
  compteRendu: '../fake-data/blob/hipster.txt',
  dateCloture: dayjs('2026-09-02'),
};

export const sampleWithFullData: IMaintenance = {
  id: 10368,
  typeMaintenance: 'CORRECTIVE',
  datePanne: dayjs('2026-09-02'),
  statut: 'EN_COURS',
  compteRendu: '../fake-data/blob/hipster.txt',
  dateCloture: dayjs('2026-09-01'),
};

export const sampleWithNewData: NewMaintenance = {
  typeMaintenance: 'PREVENTIVE',
  statut: 'EN_COURS',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
