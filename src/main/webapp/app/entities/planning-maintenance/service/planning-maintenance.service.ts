import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IPlanningMaintenance, NewPlanningMaintenance } from '../planning-maintenance.model';

export type PartialUpdatePlanningMaintenance = Partial<IPlanningMaintenance> & Pick<IPlanningMaintenance, 'id'>;

type RestOf<T extends IPlanningMaintenance | NewPlanningMaintenance> = Omit<T, 'datePrevue'> & {
  datePrevue?: string | null;
};

export type RestPlanningMaintenance = RestOf<IPlanningMaintenance>;

export type NewRestPlanningMaintenance = RestOf<NewPlanningMaintenance>;

export type PartialUpdateRestPlanningMaintenance = RestOf<PartialUpdatePlanningMaintenance>;

@Service()
export class PlanningMaintenancesService {
  readonly planningMaintenancesParams = signal<
    Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined
  >(undefined);
  readonly planningMaintenancesResource = httpResource<RestPlanningMaintenance[]>(() => {
    const params = this.planningMaintenancesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of planningMaintenance that have been fetched. It is updated when the planningMaintenancesResource emits a new value.
   * In case of error while fetching the planningMaintenances, the signal is set to an empty array.
   */
  readonly planningMaintenances = computed(() =>
    (this.planningMaintenancesResource.hasValue() ? this.planningMaintenancesResource.value() : []).map(item =>
      this.convertValueFromServer(item),
    ),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/planning-maintenances`;

  protected convertValueFromServer(restPlanningMaintenance: RestPlanningMaintenance): IPlanningMaintenance {
    return {
      ...restPlanningMaintenance,
      datePrevue: restPlanningMaintenance.datePrevue ? dayjs(restPlanningMaintenance.datePrevue) : undefined,
    };
  }
}

@Service()
export class PlanningMaintenanceService extends PlanningMaintenancesService {
  protected readonly http = inject(HttpClient);

  create(planningMaintenance: NewPlanningMaintenance): Observable<IPlanningMaintenance> {
    const copy = this.convertValueFromClient(planningMaintenance);
    return this.http.post<RestPlanningMaintenance>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(planningMaintenance: IPlanningMaintenance): Observable<IPlanningMaintenance> {
    const copy = this.convertValueFromClient(planningMaintenance);
    return this.http
      .put<RestPlanningMaintenance>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPlanningMaintenanceIdentifier(planningMaintenance))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(planningMaintenance: PartialUpdatePlanningMaintenance): Observable<IPlanningMaintenance> {
    const copy = this.convertValueFromClient(planningMaintenance);
    return this.http
      .patch<RestPlanningMaintenance>(
        `${this.resourceUrl}/${encodeURIComponent(this.getPlanningMaintenanceIdentifier(planningMaintenance))}`,
        copy,
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<IPlanningMaintenance> {
    return this.http
      .get<RestPlanningMaintenance>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<IPlanningMaintenance[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlanningMaintenance[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getPlanningMaintenanceIdentifier(planningMaintenance: Pick<IPlanningMaintenance, 'id'>): number {
    return planningMaintenance.id;
  }

  comparePlanningMaintenance(o1: Pick<IPlanningMaintenance, 'id'> | null, o2: Pick<IPlanningMaintenance, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlanningMaintenanceIdentifier(o1) === this.getPlanningMaintenanceIdentifier(o2) : o1 === o2;
  }

  addPlanningMaintenanceToCollectionIfMissing<Type extends Pick<IPlanningMaintenance, 'id'>>(
    planningMaintenanceCollection: Type[],
    ...planningMaintenancesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const planningMaintenances: Type[] = planningMaintenancesToCheck.filter(
      planningMaintenanceItem => planningMaintenanceItem !== null && planningMaintenanceItem !== undefined,
    );
    if (planningMaintenances.length > 0) {
      const planningMaintenanceCollectionIdentifiers = planningMaintenanceCollection.map(planningMaintenanceItem =>
        this.getPlanningMaintenanceIdentifier(planningMaintenanceItem),
      );
      const planningMaintenancesToAdd = planningMaintenances.filter(planningMaintenanceItem => {
        const planningMaintenanceIdentifier = this.getPlanningMaintenanceIdentifier(planningMaintenanceItem);
        if (planningMaintenanceCollectionIdentifiers.includes(planningMaintenanceIdentifier)) {
          return false;
        }
        planningMaintenanceCollectionIdentifiers.push(planningMaintenanceIdentifier);
        return true;
      });
      return [...planningMaintenancesToAdd, ...planningMaintenanceCollection];
    }
    return planningMaintenanceCollection;
  }

  protected convertValueFromClient<T extends IPlanningMaintenance | NewPlanningMaintenance | PartialUpdatePlanningMaintenance>(
    planningMaintenance: T,
  ): RestOf<T> {
    return {
      ...planningMaintenance,
      datePrevue: planningMaintenance.datePrevue?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestPlanningMaintenance): IPlanningMaintenance {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestPlanningMaintenance[]): IPlanningMaintenance[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
