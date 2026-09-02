import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import dayjs from 'dayjs/esm';
import { Observable, map } from 'rxjs';

import { DATE_FORMAT, serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITransfert, NewTransfert } from '../transfert.model';

export type PartialUpdateTransfert = Partial<ITransfert> & Pick<ITransfert, 'id'>;

type RestOf<T extends ITransfert | NewTransfert> = Omit<T, 'dateDemande' | 'dateTraitement'> & {
  dateDemande?: string | null;
  dateTraitement?: string | null;
};

export type RestTransfert = RestOf<ITransfert>;

export type NewRestTransfert = RestOf<NewTransfert>;

export type PartialUpdateRestTransfert = RestOf<PartialUpdateTransfert>;

@Service()
export class TransfertsService {
  readonly transfertsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly transfertsResource = httpResource<RestTransfert[]>(() => {
    const params = this.transfertsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of transfert that have been fetched. It is updated when the transfertsResource emits a new value.
   * In case of error while fetching the transferts, the signal is set to an empty array.
   */
  readonly transferts = computed(() =>
    (this.transfertsResource.hasValue() ? this.transfertsResource.value() : []).map(item => this.convertValueFromServer(item)),
  );
  protected readonly resourceUrl = `${serverApiUrl}api/transferts`;

  protected convertValueFromServer(restTransfert: RestTransfert): ITransfert {
    return {
      ...restTransfert,
      dateDemande: restTransfert.dateDemande ? dayjs(restTransfert.dateDemande) : undefined,
      dateTraitement: restTransfert.dateTraitement ? dayjs(restTransfert.dateTraitement) : undefined,
    };
  }
}

@Service()
export class TransfertService extends TransfertsService {
  protected readonly http = inject(HttpClient);

  create(transfert: NewTransfert): Observable<ITransfert> {
    const copy = this.convertValueFromClient(transfert);
    return this.http.post<RestTransfert>(this.resourceUrl, copy).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(transfert: ITransfert): Observable<ITransfert> {
    const copy = this.convertValueFromClient(transfert);
    return this.http
      .put<RestTransfert>(`${this.resourceUrl}/${encodeURIComponent(this.getTransfertIdentifier(transfert))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(transfert: PartialUpdateTransfert): Observable<ITransfert> {
    const copy = this.convertValueFromClient(transfert);
    return this.http
      .patch<RestTransfert>(`${this.resourceUrl}/${encodeURIComponent(this.getTransfertIdentifier(transfert))}`, copy)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<ITransfert> {
    return this.http
      .get<RestTransfert>(`${this.resourceUrl}/${encodeURIComponent(id)}`)
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<HttpResponse<ITransfert[]>> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTransfert[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => res.clone({ body: this.convertResponseArrayFromServer(res.body!) })));
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTransfertIdentifier(transfert: Pick<ITransfert, 'id'>): number {
    return transfert.id;
  }

  compareTransfert(o1: Pick<ITransfert, 'id'> | null, o2: Pick<ITransfert, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransfertIdentifier(o1) === this.getTransfertIdentifier(o2) : o1 === o2;
  }

  addTransfertToCollectionIfMissing<Type extends Pick<ITransfert, 'id'>>(
    transfertCollection: Type[],
    ...transfertsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transferts: Type[] = transfertsToCheck.filter(transfertItem => transfertItem !== null && transfertItem !== undefined);
    if (transferts.length > 0) {
      const transfertCollectionIdentifiers = transfertCollection.map(transfertItem => this.getTransfertIdentifier(transfertItem));
      const transfertsToAdd = transferts.filter(transfertItem => {
        const transfertIdentifier = this.getTransfertIdentifier(transfertItem);
        if (transfertCollectionIdentifiers.includes(transfertIdentifier)) {
          return false;
        }
        transfertCollectionIdentifiers.push(transfertIdentifier);
        return true;
      });
      return [...transfertsToAdd, ...transfertCollection];
    }
    return transfertCollection;
  }

  protected convertValueFromClient<T extends ITransfert | NewTransfert | PartialUpdateTransfert>(transfert: T): RestOf<T> {
    return {
      ...transfert,
      dateDemande: transfert.dateDemande?.format(DATE_FORMAT) ?? null,
      dateTraitement: transfert.dateTraitement?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertResponseFromServer(res: RestTransfert): ITransfert {
    return this.convertValueFromServer(res);
  }

  protected convertResponseArrayFromServer(res: RestTransfert[]): ITransfert[] {
    return res.map(item => this.convertValueFromServer(item));
  }
}
