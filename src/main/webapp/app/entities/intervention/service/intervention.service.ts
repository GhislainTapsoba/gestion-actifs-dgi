import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IIntervention, NewIntervention } from '../intervention.model';

export type PartialUpdateIntervention = Partial<IIntervention> & Pick<IIntervention, 'id'>;

type RestOf<T extends IIntervention | NewIntervention> = Omit<T, 'dateDeclaration'> & {
  dateDeclaration?: string | null;
};

export type RestIntervention = RestOf<IIntervention>;

export type NewRestIntervention = RestOf<NewIntervention>;

export type PartialUpdateRestIntervention = RestOf<PartialUpdateIntervention>;

@Service()
export class InterventionsService {
  readonly interventionsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly interventionsResource = httpResource<RestIntervention[]>(() => {
    const params = this.interventionsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of intervention that have been fetched. It is updated when the interventionsResource emits a new value.
   * In case of error while fetching the interventions, the signal is set to an empty array.
   */
  readonly interventions = computed(() =>
    (this.interventionsResource.hasValue() ? this.interventionsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/interventions`;

  protected convertValueFromServer(restIntervention: RestIntervention): IIntervention {
    return {
      ...restIntervention,
      dateDeclaration: restIntervention.dateDeclaration ? dayjs(restIntervention.dateDeclaration) : undefined,
    };
  }
}

@Service()
export class InterventionService extends InterventionsService {
  protected readonly http = inject(HttpClient);

  create(intervention: NewIntervention): Observable<IIntervention> {
    const copy = this.convertValueFromClient(intervention);
    return this.http.post<RestIntervention>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(intervention: IIntervention): Observable<IIntervention> {
    const copy = this.convertValueFromClient(intervention);
    return this.http
      .put<RestIntervention>(`${this.resourceUrl}/${encodeURIComponent(this.getInterventionIdentifier(intervention))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(intervention: PartialUpdateIntervention): Observable<IIntervention> {
    const copy = this.convertValueFromClient(intervention);
    return this.http
      .patch<RestIntervention>(`${this.resourceUrl}/${encodeURIComponent(this.getInterventionIdentifier(intervention))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IIntervention> {
    return this.http
      .get<RestIntervention>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IIntervention[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIntervention[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getInterventionIdentifier(intervention: Pick<IIntervention, 'id'>): number {
    return intervention.id;
  }

  compareIntervention(o1: Pick<IIntervention, 'id'> | null, o2: Pick<IIntervention, 'id'> | null): boolean {
    return o1 && o2 ? this.getInterventionIdentifier(o1) === this.getInterventionIdentifier(o2) : o1 === o2;
  }

  addInterventionToCollectionIfMissing<Type extends Pick<IIntervention, 'id'>>(
    interventionCollection: Type[],
    ...interventionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const interventions: Type[] = interventionsToCheck.filter(
      interventionItem => interventionItem !== null && interventionItem !== undefined,
    );
    if (interventions.length > 0) {
      const interventionCollectionIdentifiers = interventionCollection.map(interventionItem =>
        this.getInterventionIdentifier(interventionItem),
      );
      const interventionsToAdd = interventions.filter(interventionItem => {
        const interventionIdentifier = this.getInterventionIdentifier(interventionItem);
        if (interventionCollectionIdentifiers.includes(interventionIdentifier)) {
          return false;
        }
        interventionCollectionIdentifiers.push(interventionIdentifier);
        return true;
      });
      return [...interventionsToAdd, ...interventionCollection];
    }
    return interventionCollection;
  }

  protected convertValueFromClient<T extends IIntervention | NewIntervention | PartialUpdateIntervention>(intervention: T): RestOf<T> {
    return {
      ...intervention,
      dateDeclaration: intervention.dateDeclaration?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestIntervention): IIntervention {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestIntervention[]): IIntervention[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
