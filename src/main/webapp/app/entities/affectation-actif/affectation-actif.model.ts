import { IActif } from 'app/entities/actif/actif.model';
import { IAffectation } from 'app/entities/affectation/affectation.model';
import { StatutAffectation } from 'app/entities/enumerations/statut-affectation.model';

export interface IAffectationActif {
  id: number;
  observation?: string | null;
  statut?: keyof typeof StatutAffectation | null;
  affectation?: Pick<IAffectation, 'id'> | null;
  actif?: Pick<IActif, 'id'> | null;
}

export type NewAffectationActif = Omit<IAffectationActif, 'id'> & { id: null };
