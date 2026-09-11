import dayjs from 'dayjs/esm';

import { StatutIntervention } from 'app/entities/enumerations/statut-intervention.model';
import { TypeIntervention } from 'app/entities/enumerations/type-intervention.model';
import { IPanne } from 'app/entities/panne/panne.model';
import { IPlanningMaintenance } from 'app/entities/planning-maintenance/planning-maintenance.model';

export interface IIntervention {
  id: number;
  dateDeclaration?: dayjs.Dayjs | null;
  typeIntervention?: keyof typeof TypeIntervention | null;
  statut?: keyof typeof StatutIntervention | null;
  description?: string | null;
  panne?: Pick<IPanne, 'id'> | null;
  plannings?: Pick<IPlanningMaintenance, 'id'>[] | null;
}

export type NewIntervention = Omit<IIntervention, 'id'> & { id: null };
