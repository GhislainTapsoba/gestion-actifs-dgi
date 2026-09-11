import dayjs from 'dayjs/esm';

import { StatutPlanning } from 'app/entities/enumerations/statut-planning.model';
import { IIntervention } from 'app/entities/intervention/intervention.model';

export interface IPlanningMaintenance {
  id: number;
  datePrevue?: dayjs.Dayjs | null;
  periodicite?: string | null;
  statut?: keyof typeof StatutPlanning | null;
  description?: string | null;
  interventions?: Pick<IIntervention, 'id'>[] | null;
}

export type NewPlanningMaintenance = Omit<IPlanningMaintenance, 'id'> & { id: null };
