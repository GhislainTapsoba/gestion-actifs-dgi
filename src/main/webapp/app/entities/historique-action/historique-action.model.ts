import dayjs from 'dayjs/esm';

import { TypeMouvement } from 'app/entities/enumerations/type-mouvement.model';
import { IUser } from 'app/entities/user/user.model';

export interface IHistoriqueAction {
  id: number;
  dateAction?: dayjs.Dayjs | null;
  typeAction?: keyof typeof TypeMouvement | null;
  entiteCiblee?: string | null;
  ancienneValeur?: string | null;
  nouvelleValeur?: string | null;
  utilisateur?: Pick<IUser, 'id'> | null;
}

export type NewHistoriqueAction = Omit<IHistoriqueAction, 'id'> & { id: null };
