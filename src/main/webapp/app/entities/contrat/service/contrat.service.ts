import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IContrat, NewContrat } from '../contrat.model';

export type PartialUpdateContrat = Partial<IContrat> & Pick<IContrat, 'id'>;

type RestOf<T extends IContrat | NewContrat> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestContrat = RestOf<IContrat>;

export type NewRestContrat = RestOf<NewContrat>;

export type PartialUpdateRestContrat = RestOf<PartialUpdateContrat>;

@Service()
export class ContratsService {
  readonly contratsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly contratsResource = httpResource<RestContrat[]>(() => {
    const params = this.contratsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of contrat that have been fetched. It is updated when the contratsResource emits a new value.
   * In case of error while fetching the contrats, the signal is set to an empty array.
   */
  readonly contrats = computed(() =>
    (this.contratsResource.hasValue() ? this.contratsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/contrats`;

  protected convertValueFromServer(restContrat: RestContrat): IContrat {
    return {
      ...restContrat,
      dateDebut: restContrat.dateDebut ? dayjs(restContrat.dateDebut) : undefined,
      dateFin: restContrat.dateFin ? dayjs(restContrat.dateFin) : undefined,
    };
  }
}

@Service()
export class ContratService extends ContratsService {
  protected readonly http = inject(HttpClient);

  create(contrat: NewContrat): Observable<IContrat> {
    const copy = this.convertValueFromClient(contrat);
    return this.http.post<RestContrat>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(contrat: IContrat): Observable<IContrat> {
    const copy = this.convertValueFromClient(contrat);
    return this.http
      .put<RestContrat>(`${this.resourceUrl}/${encodeURIComponent(this.getContratIdentifier(contrat))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(contrat: PartialUpdateContrat): Observable<IContrat> {
    const copy = this.convertValueFromClient(contrat);
    return this.http
      .patch<RestContrat>(`${this.resourceUrl}/${encodeURIComponent(this.getContratIdentifier(contrat))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IContrat> {
    return this.http
      .get<RestContrat>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IContrat[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestContrat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getContratIdentifier(contrat: Pick<IContrat, 'id'>): number {
    return contrat.id;
  }

  compareContrat(o1: Pick<IContrat, 'id'> | null, o2: Pick<IContrat, 'id'> | null): boolean {
    return o1 && o2 ? this.getContratIdentifier(o1) === this.getContratIdentifier(o2) : o1 === o2;
  }

  addContratToCollectionIfMissing<Type extends Pick<IContrat, 'id'>>(
    contratCollection: Type[],
    ...contratsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const contrats: Type[] = contratsToCheck.filter(contratItem => contratItem !== null && contratItem !== undefined);
    if (contrats.length > 0) {
      const contratCollectionIdentifiers = contratCollection.map(contratItem => this.getContratIdentifier(contratItem));
      const contratsToAdd = contrats.filter(contratItem => {
        const contratIdentifier = this.getContratIdentifier(contratItem);
        if (contratCollectionIdentifiers.includes(contratIdentifier)) {
          return false;
        }
        contratCollectionIdentifiers.push(contratIdentifier);
        return true;
      });
      return [...contratsToAdd, ...contratCollection];
    }
    return contratCollection;
  }

  protected convertValueFromClient<T extends IContrat | NewContrat | PartialUpdateContrat>(contrat: T): RestOf<T> {
    return {
      ...contrat,
      dateDebut: contrat.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: contrat.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestContrat): IContrat {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestContrat[]): IContrat[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
