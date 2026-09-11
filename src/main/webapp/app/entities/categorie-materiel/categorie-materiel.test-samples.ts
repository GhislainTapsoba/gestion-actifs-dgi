import { ICategorieMateriel, NewCategorieMateriel } from './categorie-materiel.model';

export const sampleWithRequiredData: ICategorieMateriel = {
  id: 28084,
  libelle: 'de peur de altruiste',
};

export const sampleWithPartialData: ICategorieMateriel = {
  id: 9739,
  libelle: 'équipe de recherche',
};

export const sampleWithFullData: ICategorieMateriel = {
  id: 10987,
  libelle: 'plic',
  description: 'mieux peu parce que',
};

export const sampleWithNewData: NewCategorieMateriel = {
  libelle: 'grrr pourvu que',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
