import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IInventaire, NewInventaire } from '../inventaire.model';

export type PartialUpdateInventaire = Partial<IInventaire> & Pick<IInventaire, 'id'>;

type RestOf<T extends IInventaire | NewInventaire> = Omit<T, 'dateImport'> & {
  dateImport?: string | null;
};

export type RestInventaire = RestOf<IInventaire>;

export type NewRestInventaire = RestOf<NewInventaire>;

export type PartialUpdateRestInventaire = RestOf<PartialUpdateInventaire>;

@Service()
export class InventairesService {
  readonly inventairesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly inventairesResource = httpResource<RestInventaire[]>(() => {
    const params = this.inventairesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of inventaire that have been fetched. It is updated when the inventairesResource emits a new value.
   * In case of error while fetching the inventaires, the signal is set to an empty array.
   */
  readonly inventaires = computed(() =>
    (this.inventairesResource.hasValue() ? this.inventairesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/inventaires`;

  protected convertValueFromServer(restInventaire: RestInventaire): IInventaire {
    return {
      ...restInventaire,
      dateImport: restInventaire.dateImport ? dayjs(restInventaire.dateImport) : undefined,
    };
  }
}

@Service()
export class InventaireService extends InventairesService {
  protected readonly http = inject(HttpClient);

  create(inventaire: NewInventaire): Observable<IInventaire> {
    const copy = this.convertValueFromClient(inventaire);
    return this.http.post<RestInventaire>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inventaire: IInventaire): Observable<IInventaire> {
    const copy = this.convertValueFromClient(inventaire);
    return this.http
      .put<RestInventaire>(`${this.resourceUrl}/${encodeURIComponent(this.getInventaireIdentifier(inventaire))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inventaire: PartialUpdateInventaire): Observable<IInventaire> {
    const copy = this.convertValueFromClient(inventaire);
    return this.http
      .patch<RestInventaire>(`${this.resourceUrl}/${encodeURIComponent(this.getInventaireIdentifier(inventaire))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IInventaire> {
    return this.http
      .get<RestInventaire>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IInventaire[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInventaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInventaireIdentifier(inventaire: Pick<IInventaire, 'id'>): number {
    return inventaire.id;
  }

  compareInventaire(o1: Pick<IInventaire, 'id'> | null, o2: Pick<IInventaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getInventaireIdentifier(o1) === this.getInventaireIdentifier(o2) : o1 === o2;
  }

  addInventaireToCollectionIfMissing<Type extends Pick<IInventaire, 'id'>>(
    inventaireCollection: Type[],
    ...inventairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inventaires: Type[] = inventairesToCheck.filter(inventaireItem => inventaireItem !== null && inventaireItem !== undefined);
    if (inventaires.length > 0) {
      const inventaireCollectionIdentifiers = inventaireCollection.map(inventaireItem => this.getInventaireIdentifier(inventaireItem));
      const inventairesToAdd = inventaires.filter(inventaireItem => {
        const inventaireIdentifier = this.getInventaireIdentifier(inventaireItem);
        if (inventaireCollectionIdentifiers.includes(inventaireIdentifier)) {
          return false;
        }
        inventaireCollectionIdentifiers.push(inventaireIdentifier);
        return true;
      });
      return [...inventairesToAdd, ...inventaireCollection];
    }
    return inventaireCollection;
  }

  protected convertValueFromClient<T extends IInventaire | NewInventaire | PartialUpdateInventaire>(inventaire: T): RestOf<T> {
    return {
      ...inventaire,
      dateImport: inventaire.dateImport?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestInventaire): IInventaire {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestInventaire[]): IInventaire[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
