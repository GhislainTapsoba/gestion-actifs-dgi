export interface IFournisseur {
  id: number;
  nom?: string | null;
  contact?: string | null;
  email?: string | null;
  telephone?: string | null;
}

export type NewFournisseur = Omit<IFournisseur, 'id'> & { id: null };
