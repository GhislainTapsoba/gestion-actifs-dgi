import dayjs from 'dayjs/esm';

import { StatutRecensement } from 'app/entities/enumerations/statut-recensement.model';

export interface IRecensement {
  id: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  statut?: keyof typeof StatutRecensement | null;
}

export type NewRecensement = Omit<IRecensement, 'id'> & { id: null };
