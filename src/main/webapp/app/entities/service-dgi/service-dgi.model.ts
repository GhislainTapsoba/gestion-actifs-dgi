export interface IServiceDgi {
  id: number;
  nomService?: string | null;
  chefService?: string | null;
}

export type NewServiceDgi = Omit<IServiceDgi, 'id'> & { id: null };
