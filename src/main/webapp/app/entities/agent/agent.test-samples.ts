import { IAgent, NewAgent } from './agent.model';

export const sampleWithRequiredData: IAgent = {
  id: 10819,
  nom: 'avant que cuicui vlan',
  prenom: 'guide aux alentours de',
};

export const sampleWithPartialData: IAgent = {
  id: 20509,
  nom: 'tendre administration au-dessous de',
  prenom: 'jeune enfant',
};

export const sampleWithFullData: IAgent = {
  id: 15180,
  nom: 'deçà',
  prenom: 'avant que toutefois',
};

export const sampleWithNewData: NewAgent = {
  nom: 'appartenir pendant que',
  prenom: 'tandis que quoique',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
