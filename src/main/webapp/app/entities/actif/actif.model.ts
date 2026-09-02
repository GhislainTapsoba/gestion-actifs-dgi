import dayjs from 'dayjs/esm';

import { StatutActif } from 'app/entities/enumerations/statut-actif.model';
import { TypeActif } from 'app/entities/enumerations/type-actif.model';

export interface IActif {
  id: number;
  identifiantUnique?: string | null;
  codeBarreQR?: string | null;
  type?: keyof typeof TypeActif | null;
  etat?: keyof typeof StatutActif | null;
  localisation?: string | null;
  dateAcquisition?: dayjs.Dayjs | null;
}

export type NewActif = Omit<IActif, 'id'> & { id: null };
