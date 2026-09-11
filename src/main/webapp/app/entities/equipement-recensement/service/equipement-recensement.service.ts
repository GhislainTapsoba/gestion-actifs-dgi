import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IEquipementRecensement, NewEquipementRecensement } from '../equipement-recensement.model';

export type PartialUpdateEquipementRecensement = Partial<IEquipementRecensement> & Pick<IEquipementRecensement, 'id'>;

type RestOf<T extends IEquipementRecensement | NewEquipementRecensement> = Omit<T, 'dateConstat'> & {
  dateConstat?: string | null;
};

export type RestEquipementRecensement = RestOf<IEquipementRecensement>;

export type NewRestEquipementRecensement = RestOf<NewEquipementRecensement>;

export type PartialUpdateRestEquipementRecensement = RestOf<PartialUpdateEquipementRecensement>;

@Service()
export class EquipementRecensementsService {
  readonly equipementRecensementsParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly equipementRecensementsResource = httpResource<RestEquipementRecensement[]>(() => {
    const params = this.equipementRecensementsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of equipementRecensement that have been fetched. It is updated when the equipementRecensementsResource emits a new value.
   * In case of error while fetching the equipementRecensements, the signal is set to an empty array.
   */
  readonly equipementRecensements = computed(() =>
    (this.equipementRecensementsResource.hasValue() ? this.equipementRecensementsResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/equipement-recensements`;

  protected convertValueFromServer(restEquipementRecensement: RestEquipementRecensement): IEquipementRecensement {
    return {
      ...restEquipementRecensement,
      dateConstat: restEquipementRecensement.dateConstat ? dayjs(restEquipementRecensement.dateConstat) : undefined,
    };
  }
}

@Service()
export class EquipementRecensementService extends EquipementRecensementsService {
  protected readonly http = inject(HttpClient);

  create(equipementRecensement: NewEquipementRecensement): Observable<IEquipementRecensement> {
    const copy = this.convertValueFromClient(equipementRecensement);
    return this.http.post<RestEquipementRecensement>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(equipementRecensement: IEquipementRecensement): Observable<IEquipementRecensement> {
    const copy = this.convertValueFromClient(equipementRecensement);
    return this.http
      .put<RestEquipementRecensement>(
        `${this.resourceUrl}/${encodeURIComponent(this.getEquipementRecensementIdentifier(equipementRecensement))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(equipementRecensement: PartialUpdateEquipementRecensement): Observable<IEquipementRecensement> {
    const copy = this.convertValueFromClient(equipementRecensement);
    return this.http
      .patch<RestEquipementRecensement>(
        `${this.resourceUrl}/${encodeURIComponent(this.getEquipementRecensementIdentifier(equipementRecensement))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IEquipementRecensement> {
    return this.http
      .get<RestEquipementRecensement>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IEquipementRecensement[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEquipementRecensement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getEquipementRecensementIdentifier(equipementRecensement: Pick<IEquipementRecensement, 'id'>): number {
    return equipementRecensement.id;
  }

  compareEquipementRecensement(o1: Pick<IEquipementRecensement, 'id'> | null, o2: Pick<IEquipementRecensement, 'id'> | null): boolean {
    return o1 && o2 ? this.getEquipementRecensementIdentifier(o1) === this.getEquipementRecensementIdentifier(o2) : o1 === o2;
  }

  addEquipementRecensementToCollectionIfMissing<Type extends Pick<IEquipementRecensement, 'id'>>(
    equipementRecensementCollection: Type[],
    ...equipementRecensementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const equipementRecensements: Type[] = equipementRecensementsToCheck.filter(
      equipementRecensementItem => equipementRecensementItem !== null && equipementRecensementItem !== undefined,
    );
    if (equipementRecensements.length > 0) {
      const equipementRecensementCollectionIdentifiers = equipementRecensementCollection.map(equipementRecensementItem =>
        this.getEquipementRecensementIdentifier(equipementRecensementItem),
      );
      const equipementRecensementsToAdd = equipementRecensements.filter(equipementRecensementItem => {
        const equipementRecensementIdentifier = this.getEquipementRecensementIdentifier(equipementRecensementItem);
        if (equipementRecensementCollectionIdentifiers.includes(equipementRecensementIdentifier)) {
          return false;
        }
        equipementRecensementCollectionIdentifiers.push(equipementRecensementIdentifier);
        return true;
      });
      return [...equipementRecensementsToAdd, ...equipementRecensementCollection];
    }
    return equipementRecensementCollection;
  }

  protected convertValueFromClient<T extends IEquipementRecensement | NewEquipementRecensement | PartialUpdateEquipementRecensement>(
    equipementRecensement: T,
  ): RestOf<T> {
    return {
      ...equipementRecensement,
      dateConstat: equipementRecensement.dateConstat?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestEquipementRecensement): IEquipementRecensement {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestEquipementRecensement[]): IEquipementRecensement[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
