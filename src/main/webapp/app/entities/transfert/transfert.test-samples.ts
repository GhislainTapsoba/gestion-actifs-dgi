import dayjs from 'dayjs/esm';

import { ITransfert, NewTransfert } from './transfert.model';

export const sampleWithRequiredData: ITransfert = {
  id: 7543,
  dateDemande: dayjs('2026-09-02'),
  statut: 'VALIDE',
};

export const sampleWithPartialData: ITransfert = {
  id: 19897,
  dateDemande: dayjs('2026-09-02'),
  statut: 'VALIDE',
};

export const sampleWithFullData: ITransfert = {
  id: 13521,
  dateDemande: dayjs('2026-09-02'),
  statut: 'EN_ATTENTE',
  commentaireRejet: 'pourrir',
  dateTraitement: dayjs('2026-09-02'),
};

export const sampleWithNewData: NewTransfert = {
  dateDemande: dayjs('2026-09-02'),
  statut: 'VALIDE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
