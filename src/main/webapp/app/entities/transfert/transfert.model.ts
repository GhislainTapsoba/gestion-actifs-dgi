import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';
import { StatutTransfert } from 'app/entities/enumerations/statut-transfert.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITransfert {
  id: number;
  dateDemande?: dayjs.Dayjs | null;
  statut?: keyof typeof StatutTransfert | null;
  commentaireRejet?: string | null;
  dateTraitement?: dayjs.Dayjs | null;
  demandeur?: Pick<IUser, 'id'> | null;
  validateur?: Pick<IUser, 'id'> | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewTransfert = Omit<ITransfert, 'id'> & { id: null };
