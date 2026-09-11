import { IServiceDgi, NewServiceDgi } from './service-dgi.model';

export const sampleWithRequiredData: IServiceDgi = {
  id: 27489,
  nomService: 'grrr aussitôt',
};

export const sampleWithPartialData: IServiceDgi = {
  id: 13338,
  nomService: 'concurrence géométrique toujours',
  chefService: 'personnel sage bien',
};

export const sampleWithFullData: IServiceDgi = {
  id: 24655,
  nomService: 'dresser',
  chefService: 'autour de',
};

export const sampleWithNewData: NewServiceDgi = {
  nomService: 'électorat',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
