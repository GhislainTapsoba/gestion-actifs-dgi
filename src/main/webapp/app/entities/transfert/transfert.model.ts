import dayjs from 'dayjs/esm';

import { StatutTransfert } from 'app/entities/enumerations/statut-transfert.model';
import { IServiceDgi } from 'app/entities/service-dgi/service-dgi.model';
import { IUser } from 'app/entities/user/user.model';

export interface ITransfert {
  id: number;
  dateTransfert?: dayjs.Dayjs | null;
  statut?: keyof typeof StatutTransfert | null;
  commentaireRejet?: string | null;
  dateTraitement?: dayjs.Dayjs | null;
  serviceOrigine?: Pick<IServiceDgi, 'id' | 'nomService'> | null;
  serviceDestinataire?: Pick<IServiceDgi, 'id' | 'nomService'> | null;
  demandeur?: Pick<IUser, 'id' | 'login'> | null;
  validateur?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewTransfert = Omit<ITransfert, 'id'> & { id: null };
