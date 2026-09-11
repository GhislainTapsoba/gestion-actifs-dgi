import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPanne, NewPanne } from '../panne.model';

export type PartialUpdatePanne = Partial<IPanne> & Pick<IPanne, 'id'>;

type RestOf<T extends IPanne | NewPanne> = Omit<T, 'dateDeclaration'> & {
  dateDeclaration?: string | null;
};

export type RestPanne = RestOf<IPanne>;

export type NewRestPanne = RestOf<NewPanne>;

export type PartialUpdateRestPanne = RestOf<PartialUpdatePanne>;

@Service()
export class PannesService {
  readonly pannesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly pannesResource = httpResource<RestPanne[]>(() => {
    const params = this.pannesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of panne that have been fetched. It is updated when the pannesResource emits a new value.
   * In case of error while fetching the pannes, the signal is set to an empty array.
   */
  readonly pannes = computed(() =>
    (this.pannesResource.hasValue() ? this.pannesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/pannes`;

  protected convertValueFromServer(restPanne: RestPanne): IPanne {
    return {
      ...restPanne,
      dateDeclaration: restPanne.dateDeclaration ? dayjs(restPanne.dateDeclaration) : undefined,
    };
  }
}

@Service()
export class PanneService extends PannesService {
  protected readonly http = inject(HttpClient);

  create(panne: NewPanne): Observable<IPanne> {
    const copy = this.convertValueFromClient(panne);
    return this.http.post<RestPanne>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(panne: IPanne): Observable<IPanne> {
    const copy = this.convertValueFromClient(panne);
    return this.http
      .put<RestPanne>(`${this.resourceUrl}/${encodeURIComponent(this.getPanneIdentifier(panne))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(panne: PartialUpdatePanne): Observable<IPanne> {
    const copy = this.convertValueFromClient(panne);
    return this.http
      .patch<RestPanne>(`${this.resourceUrl}/${encodeURIComponent(this.getPanneIdentifier(panne))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPanne> {
    return this.http.get<RestPanne>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPanne[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPanne[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPanneIdentifier(panne: Pick<IPanne, 'id'>): number {
    return panne.id;
  }

  comparePanne(o1: Pick<IPanne, 'id'> | null, o2: Pick<IPanne, 'id'> | null): boolean {
    return o1 && o2 ? this.getPanneIdentifier(o1) === this.getPanneIdentifier(o2) : o1 === o2;
  }

  addPanneToCollectionIfMissing<Type extends Pick<IPanne, 'id'>>(
    panneCollection: Type[],
    ...pannesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pannes: Type[] = pannesToCheck.filter(panneItem => panneItem !== null && panneItem !== undefined);
    if (pannes.length > 0) {
      const panneCollectionIdentifiers = panneCollection.map(panneItem => this.getPanneIdentifier(panneItem));
      const pannesToAdd = pannes.filter(panneItem => {
        const panneIdentifier = this.getPanneIdentifier(panneItem);
        if (panneCollectionIdentifiers.includes(panneIdentifier)) {
          return false;
        }
        panneCollectionIdentifiers.push(panneIdentifier);
        return true;
      });
      return [...pannesToAdd, ...panneCollection];
    }
    return panneCollection;
  }

  protected convertValueFromClient<T extends IPanne | NewPanne | PartialUpdatePanne>(panne: T): RestOf<T> {
    return {
      ...panne,
      dateDeclaration: panne.dateDeclaration?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPanne): IPanne {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPanne[]): IPanne[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
