import dayjs from 'dayjs/esm';

export interface IRapport {
  id: number;
  titre?: string | null;
  typeRapport?: string | null;
  description?: string | null;
  cheminFichier?: string | null;
  dateGeneration?: dayjs.Dayjs | null;
  generePar?: string | null;
  formatExport?: string | null;
  parametres?: string | null;
}

export type NewRapport = Omit<IRapport, 'id'> & { id: null };
