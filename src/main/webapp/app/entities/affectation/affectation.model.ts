import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';
import { IUser } from 'app/entities/user/user.model';

export interface IAffectation {
  id: number;
  dateAffectation?: dayjs.Dayjs | null;
  dateRestitution?: dayjs.Dayjs | null;
  numeroBordereau?: string | null;
  utilisateur?: Pick<IUser, 'id'> | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewAffectation = Omit<IAffectation, 'id'> & { id: null };
