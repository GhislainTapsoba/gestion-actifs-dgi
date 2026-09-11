import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';
import { StatutPanne } from 'app/entities/enumerations/statut-panne.model';

export interface IPanne {
  id: number;
  description?: string | null;
  dateDeclaration?: dayjs.Dayjs | null;
  statutPanne?: keyof typeof StatutPanne | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewPanne = Omit<IPanne, 'id'> & { id: null };
