export interface ICategorieMateriel {
  id: number;
  libelle?: string | null;
  description?: string | null;
}

export type NewCategorieMateriel = Omit<ICategorieMateriel, 'id'> & { id: null };
