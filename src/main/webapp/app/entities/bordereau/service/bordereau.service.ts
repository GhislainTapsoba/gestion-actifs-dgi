import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IBordereau, NewBordereau } from '../bordereau.model';

export type PartialUpdateBordereau = Partial<IBordereau> & Pick<IBordereau, 'id'>;

type RestOf<T extends IBordereau | NewBordereau> = Omit<T, 'dateEmission' | 'dateValidation'> & {
  dateEmission?: string | null;
  dateValidation?: string | null;
};

export type RestBordereau = RestOf<IBordereau>;

export type NewRestBordereau = RestOf<NewBordereau>;

export type PartialUpdateRestBordereau = RestOf<PartialUpdateBordereau>;

@Service()
export class BordereausService {
  readonly bordereausParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly bordereausResource = httpResource<RestBordereau[]>(() => {
    const params = this.bordereausParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of bordereau that have been fetched. It is updated when the bordereausResource emits a new value.
   * In case of error while fetching the bordereaus, the signal is set to an empty array.
   */
  readonly bordereaus = computed(() =>
    (this.bordereausResource.hasValue() ? this.bordereausResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/bordereaus`;

  protected convertValueFromServer(restBordereau: RestBordereau): IBordereau {
    return {
      ...restBordereau,
      dateEmission: restBordereau.dateEmission ? dayjs(restBordereau.dateEmission) : undefined,
      dateValidation: restBordereau.dateValidation ? dayjs(restBordereau.dateValidation) : undefined,
    };
  }
}

@Service()
export class BordereauService extends BordereausService {
  protected readonly http = inject(HttpClient);

  create(bordereau: NewBordereau): Observable<IBordereau> {
    const copy = this.convertValueFromClient(bordereau);
    return this.http.post<RestBordereau>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bordereau: IBordereau): Observable<IBordereau> {
    const copy = this.convertValueFromClient(bordereau);
    return this.http
      .put<RestBordereau>(`${this.resourceUrl}/${encodeURIComponent(this.getBordereauIdentifier(bordereau))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bordereau: PartialUpdateBordereau): Observable<IBordereau> {
    const copy = this.convertValueFromClient(bordereau);
    return this.http
      .patch<RestBordereau>(`${this.resourceUrl}/${encodeURIComponent(this.getBordereauIdentifier(bordereau))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IBordereau> {
    return this.http
      .get<RestBordereau>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IBordereau[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBordereau[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getBordereauIdentifier(bordereau: Pick<IBordereau, 'id'>): number {
    return bordereau.id;
  }

  compareBordereau(o1: Pick<IBordereau, 'id'> | null, o2: Pick<IBordereau, 'id'> | null): boolean {
    return o1 && o2 ? this.getBordereauIdentifier(o1) === this.getBordereauIdentifier(o2) : o1 === o2;
  }

  addBordereauToCollectionIfMissing<Type extends Pick<IBordereau, 'id'>>(
    bordereauCollection: Type[],
    ...bordereausToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bordereaus: Type[] = bordereausToCheck.filter(bordereauItem => bordereauItem !== null && bordereauItem !== undefined);
    if (bordereaus.length > 0) {
      const bordereauCollectionIdentifiers = bordereauCollection.map(bordereauItem => this.getBordereauIdentifier(bordereauItem));
      const bordereausToAdd = bordereaus.filter(bordereauItem => {
        const bordereauIdentifier = this.getBordereauIdentifier(bordereauItem);
        if (bordereauCollectionIdentifiers.includes(bordereauIdentifier)) {
          return false;
        }
        bordereauCollectionIdentifiers.push(bordereauIdentifier);
        return true;
      });
      return [...bordereausToAdd, ...bordereauCollection];
    }
    return bordereauCollection;
  }

  protected convertValueFromClient<T extends IBordereau | NewBordereau | PartialUpdateBordereau>(bordereau: T): RestOf<T> {
    return {
      ...bordereau,
      dateEmission: bordereau.dateEmission?.format(DATE_FORMAT) ?? null,
      dateValidation: bordereau.dateValidation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestBordereau): IBordereau {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestBordereau[]): IBordereau[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
