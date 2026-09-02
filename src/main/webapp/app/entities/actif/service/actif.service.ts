import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IActif, NewActif } from '../actif.model';

export type PartialUpdateActif = Partial<IActif> & Pick<IActif, 'id'>;

type RestOf<T extends IActif | NewActif> = Omit<T, 'dateAcquisition'> & {
  dateAcquisition?: string | null;
};

export type RestActif = RestOf<IActif>;

export type NewRestActif = RestOf<NewActif>;

export type PartialUpdateRestActif = RestOf<PartialUpdateActif>;

@Service()
export class ActifsService {
  readonly actifsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly actifsResource = httpResource<RestActif[]>(() => {
    const params = this.actifsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of actif that have been fetched. It is updated when the actifsResource emits a new value.
   * In case of error while fetching the actifs, the signal is set to an empty array.
   */
  readonly actifs = computed(() =>
    (this.actifsResource.hasValue() ? this.actifsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/actifs`;

  protected convertValueFromServer(restActif: RestActif): IActif {
    return {
      ...restActif,
      dateAcquisition: restActif.dateAcquisition ? dayjs(restActif.dateAcquisition) : undefined,
    };
  }
}

@Service()
export class ActifService extends ActifsService {
  protected readonly http = inject(HttpClient);

  create(actif: NewActif): Observable<IActif> {
    const copy = this.convertValueFromClient(actif);
    return this.http.post<RestActif>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(actif: IActif): Observable<IActif> {
    const copy = this.convertValueFromClient(actif);
    return this.http
      .put<RestActif>(`${this.resourceUrl}/${encodeURIComponent(this.getActifIdentifier(actif))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(actif: PartialUpdateActif): Observable<IActif> {
    const copy = this.convertValueFromClient(actif);
    return this.http
      .patch<RestActif>(`${this.resourceUrl}/${encodeURIComponent(this.getActifIdentifier(actif))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IActif> {
    return this.http.get<RestActif>(`${this.resourceUrl}/${encodeURIComponent(id)}`).pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IActif[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestActif[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getActifIdentifier(actif: Pick<IActif, 'id'>): number {
    return actif.id;
  }

  compareActif(o1: Pick<IActif, 'id'> | null, o2: Pick<IActif, 'id'> | null): boolean {
    return o1 && o2 ? this.getActifIdentifier(o1) === this.getActifIdentifier(o2) : o1 === o2;
  }

  addActifToCollectionIfMissing<Type extends Pick<IActif, 'id'>>(
    actifCollection: Type[],
    ...actifsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const actifs: Type[] = actifsToCheck.filter(actifItem => actifItem !== null && actifItem !== undefined);
    if (actifs.length > 0) {
      const actifCollectionIdentifiers = actifCollection.map(actifItem => this.getActifIdentifier(actifItem));
      const actifsToAdd = actifs.filter(actifItem => {
        const actifIdentifier = this.getActifIdentifier(actifItem);
        if (actifCollectionIdentifiers.includes(actifIdentifier)) {
          return false;
        }
        actifCollectionIdentifiers.push(actifIdentifier);
        return true;
      });
      return [...actifsToAdd, ...actifCollection];
    }
    return actifCollection;
  }

  protected convertValueFromClient<T extends IActif | NewActif | PartialUpdateActif>(actif: T): RestOf<T> {
    return {
      ...actif,
      dateAcquisition: actif.dateAcquisition?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestActif): IActif {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestActif[]): IActif[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
