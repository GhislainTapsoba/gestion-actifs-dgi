import dayjs from 'dayjs/esm';

import { ICategorieMateriel } from 'app/entities/categorie-materiel/categorie-materiel.model';
import { StatutActif } from 'app/entities/enumerations/statut-actif.model';
import { TypeActif } from 'app/entities/enumerations/type-actif.model';

export interface IActif {
  id: number;
  codeInventaire?: string | null;
  designation?: string | null;
  marque?: string | null;
  modele?: string | null;
  numeroSerie?: string | null;
  codeBarre?: string | null;
  type?: keyof typeof TypeActif | null;
  etat?: keyof typeof StatutActif | null;
  localisation?: string | null;
  dateAcquisition?: dayjs.Dayjs | null;
  valeurAcquisition?: number | null;
  categorie?: Pick<ICategorieMateriel, 'id' | 'libelle'> | null;
}

export type NewActif = Omit<IActif, 'id'> & { id: null };
