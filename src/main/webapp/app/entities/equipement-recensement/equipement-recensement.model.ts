import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';
import { EtatMateriel } from 'app/entities/enumerations/etat-materiel.model';
import { IRecensement } from 'app/entities/recensement/recensement.model';

export interface IEquipementRecensement {
  id: number;
  etatConstate?: keyof typeof EtatMateriel | null;
  dateConstat?: dayjs.Dayjs | null;
  emplacementConstate?: string | null;
  anomalieConstatee?: boolean | null;
  recensement?: Pick<IRecensement, 'id'> | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewEquipementRecensement = Omit<IEquipementRecensement, 'id'> & { id: null };
