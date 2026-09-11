import { IServiceDgi } from 'app/entities/service-dgi/service-dgi.model';
import { IUser } from 'app/entities/user/user.model';

export interface IAgent {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  service?: Pick<IServiceDgi, 'id'> | null;
  utilisateur?: Pick<IUser, 'id'> | null;
}

export type NewAgent = Omit<IAgent, 'id'> & { id: null };
