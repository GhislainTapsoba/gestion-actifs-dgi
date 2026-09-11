import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICategorieMateriel, NewCategorieMateriel } from '../categorie-materiel.model';

export type PartialUpdateCategorieMateriel = Partial<ICategorieMateriel> & Pick<ICategorieMateriel, 'id'>;

@Service()
export class CategorieMaterielsService {
  readonly categorieMaterielsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly categorieMaterielsResource = httpResource<ICategorieMateriel[]>(() => {
    const params = this.categorieMaterielsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of categorieMateriel that have been fetched. It is updated when the categorieMaterielsResource emits a new value.
   * In case of error while fetching the categorieMateriels, the signal is set to an empty array.
   */
  readonly categorieMateriels = computed(() => (this.categorieMaterielsResource.hasValue() ? this.categorieMaterielsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/categorie-materiels`;
}

@Service()
export class CategorieMaterielService extends CategorieMaterielsService {
  protected readonly http = inject(HttpClient);

  create(categorieMateriel: NewCategorieMateriel): Observable<ICategorieMateriel> {
    return this.http.post<ICategorieMateriel>(this.resourceUrl, categorieMateriel);
  }

  update(categorieMateriel: ICategorieMateriel): Observable<ICategorieMateriel> {
    return this.http.put<ICategorieMateriel>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCategorieMaterielIdentifier(categorieMateriel))}`,
      categorieMateriel,
    );
  }

  partialUpdate(categorieMateriel: PartialUpdateCategorieMateriel): Observable<ICategorieMateriel> {
    return this.http.patch<ICategorieMateriel>(
      `${this.resourceUrl}/${encodeURIComponent(this.getCategorieMaterielIdentifier(categorieMateriel))}`,
      categorieMateriel,
    );
  }

  find(id: number): Observable<ICategorieMateriel> {
    return this.http.get<ICategorieMateriel>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICategorieMateriel[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICategorieMateriel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCategorieMaterielIdentifier(categorieMateriel: Pick<ICategorieMateriel, 'id'>): number {
    return categorieMateriel.id;
  }

  compareCategorieMateriel(o1: Pick<ICategorieMateriel, 'id'> | null, o2: Pick<ICategorieMateriel, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategorieMaterielIdentifier(o1) === this.getCategorieMaterielIdentifier(o2) : o1 === o2;
  }

  addCategorieMaterielToCollectionIfMissing<Type extends Pick<ICategorieMateriel, 'id'>>(
    categorieMaterielCollection: Type[],
    ...categorieMaterielsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categorieMateriels: Type[] = categorieMaterielsToCheck.filter(
      categorieMaterielItem => categorieMaterielItem !== null && categorieMaterielItem !== undefined,
    );
    if (categorieMateriels.length > 0) {
      const categorieMaterielCollectionIdentifiers = categorieMaterielCollection.map(categorieMaterielItem =>
        this.getCategorieMaterielIdentifier(categorieMaterielItem),
      );
      const categorieMaterielsToAdd = categorieMateriels.filter(categorieMaterielItem => {
        const categorieMaterielIdentifier = this.getCategorieMaterielIdentifier(categorieMaterielItem);
        if (categorieMaterielCollectionIdentifiers.includes(categorieMaterielIdentifier)) {
          return false;
        }
        categorieMaterielCollectionIdentifiers.push(categorieMaterielIdentifier);
        return true;
      });
      return [...categorieMaterielsToAdd, ...categorieMaterielCollection];
    }
    return categorieMaterielCollection;
  }
}
