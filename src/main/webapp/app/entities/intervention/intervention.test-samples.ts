import dayjs from 'dayjs/esm';

import { IIntervention, NewIntervention } from './intervention.model';

export const sampleWithRequiredData: IIntervention = {
  id: 29795,
  dateDeclaration: dayjs('2026-09-10'),
  typeIntervention: 'CORRECTIVE',
  statut: 'CLOTUREE',
};

export const sampleWithPartialData: IIntervention = {
  id: 22307,
  dateDeclaration: dayjs('2026-09-10'),
  typeIntervention: 'PREVENTIVE',
  statut: 'CLOTUREE',
};

export const sampleWithFullData: IIntervention = {
  id: 30017,
  dateDeclaration: dayjs('2026-09-10'),
  typeIntervention: 'CORRECTIVE',
  statut: 'CLOTUREE',
  description: 'tracer gens bien que',
};

export const sampleWithNewData: NewIntervention = {
  dateDeclaration: dayjs('2026-09-10'),
  typeIntervention: 'PREVENTIVE',
  statut: 'CLOTUREE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
