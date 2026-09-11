import dayjs from 'dayjs/esm';

import { IHistoriqueAction, NewHistoriqueAction } from './historique-action.model';

export const sampleWithRequiredData: IHistoriqueAction = {
  id: 829,
  dateAction: dayjs('2026-09-10T18:10'),
  typeAction: 'MAINTENANCE',
};

export const sampleWithPartialData: IHistoriqueAction = {
  id: 6782,
  dateAction: dayjs('2026-09-10T08:00'),
  typeAction: 'MAINTENANCE',
  entiteCiblee: 'simple jusqu’à ce que efficace',
  ancienneValeur: 'au point que',
  nouvelleValeur: 'tchou tchouu concernant',
};

export const sampleWithFullData: IHistoriqueAction = {
  id: 1268,
  dateAction: dayjs('2026-09-10T00:26'),
  typeAction: 'MODIFICATION',
  entiteCiblee: 'suffire rectangulaire',
  ancienneValeur: 'direction à partir de',
  nouvelleValeur: 'trop',
};

export const sampleWithNewData: NewHistoriqueAction = {
  dateAction: dayjs('2026-09-10T12:10'),
  typeAction: 'AFFECTATION',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
