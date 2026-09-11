import dayjs from 'dayjs/esm';

import { IAgent } from 'app/entities/agent/agent.model';

export interface IAffectation {
  id: number;
  dateAffectation?: dayjs.Dayjs | null;
  motif?: string | null;
  dateRestitution?: dayjs.Dayjs | null;
  agent?: Pick<IAgent, 'id'> | null;
}

export type NewAffectation = Omit<IAffectation, 'id'> & { id: null };
