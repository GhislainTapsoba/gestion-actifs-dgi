import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';

export interface IInventaire {
  id: number;
  nomFichier?: string | null;
  dateImport?: dayjs.Dayjs | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewInventaire = Omit<IInventaire, 'id'> & { id: null };
