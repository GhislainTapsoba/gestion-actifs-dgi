import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IAffectation, NewAffectation } from '../affectation.model';

export type PartialUpdateAffectation = Partial<IAffectation> & Pick<IAffectation, 'id'>;

type RestOf<T extends IAffectation | NewAffectation> = Omit<T, 'dateAffectation' | 'dateRestitution'> & {
  dateAffectation?: string | null;
  dateRestitution?: string | null;
};

export type RestAffectation = RestOf<IAffectation>;

export type NewRestAffectation = RestOf<NewAffectation>;

export type PartialUpdateRestAffectation = RestOf<PartialUpdateAffectation>;

@Service()
export class AffectationsService {
  readonly affectationsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly affectationsResource = httpResource<RestAffectation[]>(() => {
    const params = this.affectationsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of affectation that have been fetched. It is updated when the affectationsResource emits a new value.
   * In case of error while fetching the affectations, the signal is set to an empty array.
   */
  readonly affectations = computed(() =>
    (this.affectationsResource.hasValue() ? this.affectationsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/affectations`;

  protected convertValueFromServer(restAffectation: RestAffectation): IAffectation {
    return {
      ...restAffectation,
      dateAffectation: restAffectation.dateAffectation ? dayjs(restAffectation.dateAffectation) : undefined,
      dateRestitution: restAffectation.dateRestitution ? dayjs(restAffectation.dateRestitution) : undefined,
    };
  }
}

@Service()
export class AffectationService extends AffectationsService {
  protected readonly http = inject(HttpClient);

  create(affectation: NewAffectation): Observable<IAffectation> {
    const copy = this.convertValueFromClient(affectation);
    return this.http.post<RestAffectation>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(affectation: IAffectation): Observable<IAffectation> {
    const copy = this.convertValueFromClient(affectation);
    return this.http
      .put<RestAffectation>(`${this.resourceUrl}/${encodeURIComponent(this.getAffectationIdentifier(affectation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(affectation: PartialUpdateAffectation): Observable<IAffectation> {
    const copy = this.convertValueFromClient(affectation);
    return this.http
      .patch<RestAffectation>(`${this.resourceUrl}/${encodeURIComponent(this.getAffectationIdentifier(affectation))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IAffectation> {
    return this.http
      .get<RestAffectation>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IAffectation[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAffectation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAffectationIdentifier(affectation: Pick<IAffectation, 'id'>): number {
    return affectation.id;
  }

  compareAffectation(o1: Pick<IAffectation, 'id'> | null, o2: Pick<IAffectation, 'id'> | null): boolean {
    return o1 && o2 ? this.getAffectationIdentifier(o1) === this.getAffectationIdentifier(o2) : o1 === o2;
  }

  addAffectationToCollectionIfMissing<Type extends Pick<IAffectation, 'id'>>(
    affectationCollection: Type[],
    ...affectationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const affectations: Type[] = affectationsToCheck.filter(affectationItem => affectationItem !== null && affectationItem !== undefined);
    if (affectations.length > 0) {
      const affectationCollectionIdentifiers = affectationCollection.map(affectationItem => this.getAffectationIdentifier(affectationItem));
      const affectationsToAdd = affectations.filter(affectationItem => {
        const affectationIdentifier = this.getAffectationIdentifier(affectationItem);
        if (affectationCollectionIdentifiers.includes(affectationIdentifier)) {
          return false;
        }
        affectationCollectionIdentifiers.push(affectationIdentifier);
        return true;
      });
      return [...affectationsToAdd, ...affectationCollection];
    }
    return affectationCollection;
  }

  protected convertValueFromClient<T extends IAffectation | NewAffectation | PartialUpdateAffectation>(affectation: T): RestOf<T> {
    return {
      ...affectation,
      dateAffectation: affectation.dateAffectation?.format(DATE_FORMAT) ?? null,
      dateRestitution: affectation.dateRestitution?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestAffectation): IAffectation {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestAffectation[]): IAffectation[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
