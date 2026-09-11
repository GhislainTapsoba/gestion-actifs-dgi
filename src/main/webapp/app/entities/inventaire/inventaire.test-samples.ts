import dayjs from 'dayjs/esm';

import { IInventaire, NewInventaire } from './inventaire.model';

export const sampleWithRequiredData: IInventaire = {
  id: 22814,
  nomFichier: 'vlan',
  dateImport: dayjs('2026-09-10'),
};

export const sampleWithPartialData: IInventaire = {
  id: 27082,
  nomFichier: 'au-devant population du Québec gémir',
  dateImport: dayjs('2026-09-10'),
};

export const sampleWithFullData: IInventaire = {
  id: 14362,
  nomFichier: 'extrêmement',
  dateImport: dayjs('2026-09-10'),
};

export const sampleWithNewData: NewInventaire = {
  nomFichier: 'mairie',
  dateImport: dayjs('2026-09-10'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
