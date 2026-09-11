import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IRecensement, NewRecensement } from '../recensement.model';

export type PartialUpdateRecensement = Partial<IRecensement> & Pick<IRecensement, 'id'>;

type RestOf<T extends IRecensement | NewRecensement> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestRecensement = RestOf<IRecensement>;

export type NewRestRecensement = RestOf<NewRecensement>;

export type PartialUpdateRestRecensement = RestOf<PartialUpdateRecensement>;

@Service()
export class RecensementsService {
  readonly recensementsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly recensementsResource = httpResource<RestRecensement[]>(() => {
    const params = this.recensementsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of recensement that have been fetched. It is updated when the recensementsResource emits a new value.
   * In case of error while fetching the recensements, the signal is set to an empty array.
   */
  readonly recensements = computed(() =>
    (this.recensementsResource.hasValue() ? this.recensementsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/recensements`;

  protected convertValueFromServer(restRecensement: RestRecensement): IRecensement {
    return {
      ...restRecensement,
      dateDebut: restRecensement.dateDebut ? dayjs(restRecensement.dateDebut) : undefined,
      dateFin: restRecensement.dateFin ? dayjs(restRecensement.dateFin) : undefined,
    };
  }
}

@Service()
export class RecensementService extends RecensementsService {
  protected readonly http = inject(HttpClient);

  create(recensement: NewRecensement): Observable<IRecensement> {
    const copy = this.convertValueFromClient(recensement);
    return this.http.post<RestRecensement>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(recensement: IRecensement): Observable<IRecensement> {
    const copy = this.convertValueFromClient(recensement);
    return this.http
      .put<RestRecensement>(`${this.resourceUrl}/${encodeURIComponent(this.getRecensementIdentifier(recensement))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(recensement: PartialUpdateRecensement): Observable<IRecensement> {
    const copy = this.convertValueFromClient(recensement);
    return this.http
      .patch<RestRecensement>(`${this.resourceUrl}/${encodeURIComponent(this.getRecensementIdentifier(recensement))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IRecensement> {
    return this.http
      .get<RestRecensement>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IRecensement[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRecensement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRecensementIdentifier(recensement: Pick<IRecensement, 'id'>): number {
    return recensement.id;
  }

  compareRecensement(o1: Pick<IRecensement, 'id'> | null, o2: Pick<IRecensement, 'id'> | null): boolean {
    return o1 && o2 ? this.getRecensementIdentifier(o1) === this.getRecensementIdentifier(o2) : o1 === o2;
  }

  addRecensementToCollectionIfMissing<Type extends Pick<IRecensement, 'id'>>(
    recensementCollection: Type[],
    ...recensementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const recensements: Type[] = recensementsToCheck.filter(recensementItem => recensementItem !== null && recensementItem !== undefined);
    if (recensements.length > 0) {
      const recensementCollectionIdentifiers = recensementCollection.map(recensementItem => this.getRecensementIdentifier(recensementItem));
      const recensementsToAdd = recensements.filter(recensementItem => {
        const recensementIdentifier = this.getRecensementIdentifier(recensementItem);
        if (recensementCollectionIdentifiers.includes(recensementIdentifier)) {
          return false;
        }
        recensementCollectionIdentifiers.push(recensementIdentifier);
        return true;
      });
      return [...recensementsToAdd, ...recensementCollection];
    }
    return recensementCollection;
  }

  protected convertValueFromClient<T extends IRecensement | NewRecensement | PartialUpdateRecensement>(recensement: T): RestOf<T> {
    return {
      ...recensement,
      dateDebut: recensement.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: recensement.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestRecensement): IRecensement {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestRecensement[]): IRecensement[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
