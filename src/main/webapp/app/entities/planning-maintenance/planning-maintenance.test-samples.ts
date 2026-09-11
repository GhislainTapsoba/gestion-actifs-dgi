import dayjs from 'dayjs/esm';

import { IPlanningMaintenance, NewPlanningMaintenance } from './planning-maintenance.model';

export const sampleWithRequiredData: IPlanningMaintenance = {
  id: 725,
  datePrevue: dayjs('2026-09-09'),
  statut: 'PLANIFIER',
};

export const sampleWithPartialData: IPlanningMaintenance = {
  id: 27987,
  datePrevue: dayjs('2026-09-10'),
  periodicite: 'tic-tac paf',
  statut: 'TERMINER',
  description: 'équipe de recherche',
};

export const sampleWithFullData: IPlanningMaintenance = {
  id: 22261,
  datePrevue: dayjs('2026-09-09'),
  periodicite: 'quelque',
  statut: 'TERMINER',
  description: 'abuser mêler',
};

export const sampleWithNewData: NewPlanningMaintenance = {
  datePrevue: dayjs('2026-09-10'),
  statut: 'PLANIFIER',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
