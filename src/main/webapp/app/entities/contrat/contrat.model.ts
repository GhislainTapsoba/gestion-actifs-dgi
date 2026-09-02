import dayjs from 'dayjs/esm';

import { IActif } from 'app/entities/actif/actif.model';
import { TypeContrat } from 'app/entities/enumerations/type-contrat.model';
import { IFournisseur } from 'app/entities/fournisseur/fournisseur.model';

export interface IContrat {
  id: number;
  typeContrat?: keyof typeof TypeContrat | null;
  reference?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  actif?: Pick<IActif, 'id'> | null;
  fournisseur?: Pick<IFournisseur, 'id'> | null;
}

export type NewContrat = Omit<IContrat, 'id'> & { id: null };
