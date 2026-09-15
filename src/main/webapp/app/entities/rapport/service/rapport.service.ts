import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IRapport, NewRapport } from '../rapport.model';

export type PartialUpdateRapport = Partial<IRapport> & Pick<IRapport, 'id'>;

type RestOf<T extends IRapport | NewRapport> = Omit<T, 'dateGeneration'> & {
  dateGeneration?: string | null;
};

export type RestRapport = RestOf<IRapport>;
export type NewRestRapport = RestOf<NewRapport>;
export type PartialUpdateRestRapport = RestOf<PartialUpdateRapport>;

@Service()
export class RapportsService {
  readonly rapportsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly rapportsResource = httpResource<RestRapport[]>(() => {
    const params = this.rapportsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });

  readonly rapports = computed(() =>
    (this.rapportsResource.hasValue() ? this.rapportsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/rapports`;

  protected convertValueFromServer(restRapport: RestRapport): IRapport {
    return {
      ...restRapport,
      dateGeneration: restRapport.dateGeneration ? dayjs(restRapport.dateGeneration) : undefined,
    };
  }
}

@Service()
export class RapportService extends RapportsService {
  protected readonly http = inject(HttpClient);

  create(rapport: NewRapport): Observable<IRapport> {
    const copy = this.convertValueFromClient(rapport);
    return this.http.post<RestRapport>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(rapport: IRapport): Observable<IRapport> {
    const copy = this.convertValueFromClient(rapport);
    return this.http
      .put<RestRapport>(`${this.resourceUrl}/${encodeURIComponent(this.getRapportIdentifier(rapport))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(rapport: PartialUpdateRapport): Observable<IRapport> {
    const copy = this.convertValueFromClient(rapport);
    return this.http
      .patch<RestRapport>(`${this.resourceUrl}/${encodeURIComponent(this.getRapportIdentifier(rapport))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IRapport> {
    return this.http
      .get<RestRapport>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IRapport[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRapport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  findByTypeRapport(typeRapport: string, req?: any): Observable<HttpResponse<IRapport[]>> {
    const options = createRequestOption({ ...req, typeRapport });
    return this.http
      .get<RestRapport[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getRapportIdentifier(rapport: Pick<IRapport, 'id'>): number {
    return rapport.id;
  }

  compareRapport(o1: Pick<IRapport, 'id'> | null, o2: Pick<IRapport, 'id'> | null): boolean {
    return o1 && o2 ? this.getRapportIdentifier(o1) === this.getRapportIdentifier(o2) : o1 === o2;
  }

  addRapportToCollectionIfMissing<Type extends Pick<IRapport, 'id'>>(
    rapportCollection: Type[],
    ...rapportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const rapports: Type[] = rapportsToCheck.filter(rapportItem => rapportItem !== null && rapportItem !== undefined);
    if (rapports.length > 0) {
      const rapportCollectionIdentifiers = rapportCollection.map(rapportItem => this.getRapportIdentifier(rapportItem));
      const rapportsToAdd = rapports.filter(rapportItem => {
        const rapportIdentifier = this.getRapportIdentifier(rapportItem);
        if (rapportCollectionIdentifiers.includes(rapportIdentifier)) {
          return false;
        }
        rapportCollectionIdentifiers.push(rapportIdentifier);
        return true;
      });
      return [...rapportsToAdd, ...rapportCollection];
    }
    return rapportCollection;
  }

  protected convertValueFromClient<T extends IRapport | NewRapport | PartialUpdateRapport>(rapport: T): RestOf<T> {
    return {
      ...rapport,
      dateGeneration: rapport.dateGeneration?.toISOString() ?? null,
    };
  }

  protected convertResponseFromServer(res: RestRapport): IRapport {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestRapport[]): IRapport[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
