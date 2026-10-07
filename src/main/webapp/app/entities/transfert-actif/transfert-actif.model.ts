import { IActif } from 'app/entities/actif/actif.model';
import { ITransfert } from 'app/entities/transfert/transfert.model';

export interface ITransfertActif {
  id: number;
  observation?: string | null;
  transfert?: Pick<ITransfert, 'id' | 'dateTransfert'> | null;
  actif?: Pick<IActif, 'id' | 'codeInventaire'> | null;
}

export type NewTransfertActif = Omit<ITransfertActif, 'id'> & { id: null };
