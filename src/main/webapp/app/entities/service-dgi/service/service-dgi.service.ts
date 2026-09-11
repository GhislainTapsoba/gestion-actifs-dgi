import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IServiceDgi, NewServiceDgi } from '../service-dgi.model';

export type PartialUpdateServiceDgi = Partial<IServiceDgi> & Pick<IServiceDgi, 'id'>;

@Service()
export class ServiceDgisService {
  readonly serviceDgisParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly serviceDgisResource = httpResource<IServiceDgi[]>(() => {
    const params = this.serviceDgisParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of serviceDgi that have been fetched. It is updated when the serviceDgisResource emits a new value.
   * In case of error while fetching the serviceDgis, the signal is set to an empty array.
   */
  readonly serviceDgis = computed(() => (this.serviceDgisResource.hasValue() ? this.serviceDgisResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/service-dgis`;
}

@Service()
export class ServiceDgiService extends ServiceDgisService {
  protected readonly http = inject(HttpClient);

  create(serviceDgi: NewServiceDgi): Observable<IServiceDgi> {
    return this.http.post<IServiceDgi>(this.resourceUrl, serviceDgi);
  }

  update(serviceDgi: IServiceDgi): Observable<IServiceDgi> {
    return this.http.put<IServiceDgi>(`${this.resourceUrl}/${encodeURIComponent(this.getServiceDgiIdentifier(serviceDgi))}`, serviceDgi);
  }

  partialUpdate(serviceDgi: PartialUpdateServiceDgi): Observable<IServiceDgi> {
    return this.http.patch<IServiceDgi>(`${this.resourceUrl}/${encodeURIComponent(this.getServiceDgiIdentifier(serviceDgi))}`, serviceDgi);
  }

  find(id: number): Observable<IServiceDgi> {
    return this.http.get<IServiceDgi>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IServiceDgi[]>> {
    const options = createRequestOption(req);
    return this.http.get<IServiceDgi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getServiceDgiIdentifier(serviceDgi: Pick<IServiceDgi, 'id'>): number {
    return serviceDgi.id;
  }

  compareServiceDgi(o1: Pick<IServiceDgi, 'id'> | null, o2: Pick<IServiceDgi, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceDgiIdentifier(o1) === this.getServiceDgiIdentifier(o2) : o1 === o2;
  }

  addServiceDgiToCollectionIfMissing<Type extends Pick<IServiceDgi, 'id'>>(
    serviceDgiCollection: Type[],
    ...serviceDgisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceDgis: Type[] = serviceDgisToCheck.filter(serviceDgiItem => serviceDgiItem !== null && serviceDgiItem !== undefined);
    if (serviceDgis.length > 0) {
      const serviceDgiCollectionIdentifiers = serviceDgiCollection.map(serviceDgiItem => this.getServiceDgiIdentifier(serviceDgiItem));
      const serviceDgisToAdd = serviceDgis.filter(serviceDgiItem => {
        const serviceDgiIdentifier = this.getServiceDgiIdentifier(serviceDgiItem);
        if (serviceDgiCollectionIdentifiers.includes(serviceDgiIdentifier)) {
          return false;
        }
        serviceDgiCollectionIdentifiers.push(serviceDgiIdentifier);
        return true;
      });
      return [...serviceDgisToAdd, ...serviceDgiCollection];
    }
    return serviceDgiCollection;
  }
}
