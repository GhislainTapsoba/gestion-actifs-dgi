import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';
import { StatutMaintenance } from 'app/entities/enumerations/statut-maintenance.model';
import { TypeMaintenance } from 'app/entities/enumerations/type-maintenance.model';
import { IUser } from 'app/entities/user/user.model';

export interface IMaintenance {
  id: number;
  typeMaintenance?: keyof typeof TypeMaintenance | null;
  datePanne?: dayjs.Dayjs | null;
  statut?: keyof typeof StatutMaintenance | null;
  compteRendu?: string | null;
  dateCloture?: dayjs.Dayjs | null;
  technicien?: Pick<IUser, 'id'> | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewMaintenance = Omit<IMaintenance, 'id'> & { id: null };
