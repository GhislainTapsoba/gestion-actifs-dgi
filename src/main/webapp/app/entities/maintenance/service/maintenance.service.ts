import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IMaintenance, NewMaintenance } from '../maintenance.model';

export type PartialUpdateMaintenance = Partial<IMaintenance> & Pick<IMaintenance, 'id'>;

type RestOf<T extends IMaintenance | NewMaintenance> = Omit<T, 'datePanne' | 'dateCloture'> & {
  datePanne?: string | null;
  dateCloture?: string | null;
};

export type RestMaintenance = RestOf<IMaintenance>;

export type NewRestMaintenance = RestOf<NewMaintenance>;

export type PartialUpdateRestMaintenance = RestOf<PartialUpdateMaintenance>;

@Service()
export class MaintenancesService {
  readonly maintenancesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly maintenancesResource = httpResource<RestMaintenance[]>(() => {
    const params = this.maintenancesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of maintenance that have been fetched. It is updated when the maintenancesResource emits a new value.
   * In case of error while fetching the maintenances, the signal is set to an empty array.
   */
  readonly maintenances = computed(() =>
    (this.maintenancesResource.hasValue() ? this.maintenancesResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/maintenances`;

  protected convertValueFromServer(restMaintenance: RestMaintenance): IMaintenance {
    return {
      ...restMaintenance,
      datePanne: restMaintenance.datePanne ? dayjs(restMaintenance.datePanne) : undefined,
      dateCloture: restMaintenance.dateCloture ? dayjs(restMaintenance.dateCloture) : undefined,
    };
  }
}

@Service()
export class MaintenanceService extends MaintenancesService {
  protected readonly http = inject(HttpClient);

  create(maintenance: NewMaintenance): Observable<IMaintenance> {
    const copy = this.convertValueFromClient(maintenance);
    return this.http.post<RestMaintenance>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(maintenance: IMaintenance): Observable<IMaintenance> {
    const copy = this.convertValueFromClient(maintenance);
    return this.http
      .put<RestMaintenance>(`${this.resourceUrl}/${encodeURIComponent(this.getMaintenanceIdentifier(maintenance))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(maintenance: PartialUpdateMaintenance): Observable<IMaintenance> {
    const copy = this.convertValueFromClient(maintenance);
    return this.http
      .patch<RestMaintenance>(`${this.resourceUrl}/${encodeURIComponent(this.getMaintenanceIdentifier(maintenance))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IMaintenance> {
    return this.http
      .get<RestMaintenance>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IMaintenance[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMaintenance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getMaintenanceIdentifier(maintenance: Pick<IMaintenance, 'id'>): number {
    return maintenance.id;
  }

  compareMaintenance(o1: Pick<IMaintenance, 'id'> | null, o2: Pick<IMaintenance, 'id'> | null): boolean {
    return o1 && o2 ? this.getMaintenanceIdentifier(o1) === this.getMaintenanceIdentifier(o2) : o1 === o2;
  }

  addMaintenanceToCollectionIfMissing<Type extends Pick<IMaintenance, 'id'>>(
    maintenanceCollection: Type[],
    ...maintenancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const maintenances: Type[] = maintenancesToCheck.filter(maintenanceItem => maintenanceItem !== null && maintenanceItem !== undefined);
    if (maintenances.length > 0) {
      const maintenanceCollectionIdentifiers = maintenanceCollection.map(maintenanceItem => this.getMaintenanceIdentifier(maintenanceItem));
      const maintenancesToAdd = maintenances.filter(maintenanceItem => {
        const maintenanceIdentifier = this.getMaintenanceIdentifier(maintenanceItem);
        if (maintenanceCollectionIdentifiers.includes(maintenanceIdentifier)) {
          return false;
        }
        maintenanceCollectionIdentifiers.push(maintenanceIdentifier);
        return true;
      });
      return [...maintenancesToAdd, ...maintenanceCollection];
    }
    return maintenanceCollection;
  }

  protected convertValueFromClient<T extends IMaintenance | NewMaintenance | PartialUpdateMaintenance>(maintenance: T): RestOf<T> {
    return {
      ...maintenance,
      datePanne: maintenance.datePanne?.format(DATE_FORMAT) ?? null,
      dateCloture: maintenance.dateCloture?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestMaintenance): IMaintenance {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestMaintenance[]): IMaintenance[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
