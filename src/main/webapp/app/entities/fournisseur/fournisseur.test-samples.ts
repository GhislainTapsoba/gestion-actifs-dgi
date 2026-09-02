import { IFournisseur, NewFournisseur } from './fournisseur.model';

export const sampleWithRequiredData: IFournisseur = {
  id: 10917,
  nom: 'glouglou personnel professionnel liquider',
};

export const sampleWithPartialData: IFournisseur = {
  id: 2922,
  nom: 'guide vroum',
};

export const sampleWithFullData: IFournisseur = {
  id: 21444,
  nom: 'grandir',
  contact: 'reconstituer toc-toc',
  email: 'Cedric.Lefevre@gmail.com',
  telephone: '+33 165028870',
};

export const sampleWithNewData: NewFournisseur = {
  nom: 'gigantesque de crainte que secouriste',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
