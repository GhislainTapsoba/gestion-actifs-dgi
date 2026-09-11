import dayjs from 'dayjs/esm';

import { IAffectation } from 'app/entities/affectation/affectation.model';
import { StatutBordereau } from 'app/entities/enumerations/statut-bordereau.model';
import { TypeBordereau } from 'app/entities/enumerations/type-bordereau.model';
import { ITransfert } from 'app/entities/transfert/transfert.model';
import { IUser } from 'app/entities/user/user.model';

export interface IBordereau {
  id: number;
  numero?: string | null;
  dateEmission?: dayjs.Dayjs | null;
  typeBordereau?: keyof typeof TypeBordereau | null;
  statutValidation?: keyof typeof StatutBordereau | null;
  dateValidation?: dayjs.Dayjs | null;
  transfert?: Pick<ITransfert, 'id'> | null;
  affectation?: Pick<IAffectation, 'id'> | null;
  emetteur?: Pick<IUser, 'id'> | null;
}

export type NewBordereau = Omit<IBordereau, 'id'> & { id: null };
