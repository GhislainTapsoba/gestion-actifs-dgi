import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IAffectationActif, NewAffectationActif } from '../affectation-actif.model';

export type PartialUpdateAffectationActif = Partial<IAffectationActif> & Pick<IAffectationActif, 'id'>;

@Service()
export class AffectationActifsService {
  readonly affectationActifsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly affectationActifsResource = httpResource<IAffectationActif[]>(() => {
    const params = this.affectationActifsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of affectationActif that have been fetched. It is updated when the affectationActifsResource emits a new value.
   * In case of error while fetching the affectationActifs, the signal is set to an empty array.
   */
  readonly affectationActifs = computed(() => (this.affectationActifsResource.hasValue() ? this.affectationActifsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/affectation-actifs`;
}

@Service()
export class AffectationActifService extends AffectationActifsService {
  protected readonly http = inject(HttpClient);

  create(affectationActif: NewAffectationActif): Observable<IAffectationActif> {
    return this.http.post<IAffectationActif>(this.resourceUrl, affectationActif);
  }

  update(affectationActif: IAffectationActif): Observable<IAffectationActif> {
    return this.http.put<IAffectationActif>(
      `${this.resourceUrl}/${encodeURIComponent(this.getAffectationActifIdentifier(affectationActif))}`,
      affectationActif,
    );
  }

  partialUpdate(affectationActif: PartialUpdateAffectationActif): Observable<IAffectationActif> {
    return this.http.patch<IAffectationActif>(
      `${this.resourceUrl}/${encodeURIComponent(this.getAffectationActifIdentifier(affectationActif))}`,
      affectationActif,
    );
  }

  find(id: number): Observable<IAffectationActif> {
    return this.http.get<IAffectationActif>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IAffectationActif[]>> {
    const options = createRequestOption(req);
    return this.http.get<IAffectationActif[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAffectationActifIdentifier(affectationActif: Pick<IAffectationActif, 'id'>): number {
    return affectationActif.id;
  }

  compareAffectationActif(o1: Pick<IAffectationActif, 'id'> | null, o2: Pick<IAffectationActif, 'id'> | null): boolean {
    return o1 && o2 ? this.getAffectationActifIdentifier(o1) === this.getAffectationActifIdentifier(o2) : o1 === o2;
  }

  addAffectationActifToCollectionIfMissing<Type extends Pick<IAffectationActif, 'id'>>(
    affectationActifCollection: Type[],
    ...affectationActifsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const affectationActifs: Type[] = affectationActifsToCheck.filter(
      affectationActifItem => affectationActifItem !== null && affectationActifItem !== undefined,
    );
    if (affectationActifs.length > 0) {
      const affectationActifCollectionIdentifiers = affectationActifCollection.map(affectationActifItem =>
        this.getAffectationActifIdentifier(affectationActifItem),
      );
      const affectationActifsToAdd = affectationActifs.filter(affectationActifItem => {
        const affectationActifIdentifier = this.getAffectationActifIdentifier(affectationActifItem);
        if (affectationActifCollectionIdentifiers.includes(affectationActifIdentifier)) {
          return false;
        }
        affectationActifCollectionIdentifiers.push(affectationActifIdentifier);
        return true;
      });
      return [...affectationActifsToAdd, ...affectationActifCollection];
    }
    return affectationActifCollection;
  }
}
