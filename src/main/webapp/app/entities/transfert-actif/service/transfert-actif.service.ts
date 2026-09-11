import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ITransfertActif, NewTransfertActif } from '../transfert-actif.model';

export type PartialUpdateTransfertActif = Partial<ITransfertActif> & Pick<ITransfertActif, 'id'>;

@Service()
export class TransfertActifsService {
  readonly transfertActifsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly transfertActifsResource = httpResource<ITransfertActif[]>(() => {
    const params = this.transfertActifsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of transfertActif that have been fetched. It is updated when the transfertActifsResource emits a new value.
   * In case of error while fetching the transfertActifs, the signal is set to an empty array.
   */
  readonly transfertActifs = computed(() => (this.transfertActifsResource.hasValue() ? this.transfertActifsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/transfert-actifs`;
}

@Service()
export class TransfertActifService extends TransfertActifsService {
  protected readonly http = inject(HttpClient);

  create(transfertActif: NewTransfertActif): Observable<ITransfertActif> {
    return this.http.post<ITransfertActif>(this.resourceUrl, transfertActif);
  }

  update(transfertActif: ITransfertActif): Observable<ITransfertActif> {
    return this.http.put<ITransfertActif>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTransfertActifIdentifier(transfertActif))}`,
      transfertActif,
    );
  }

  partialUpdate(transfertActif: PartialUpdateTransfertActif): Observable<ITransfertActif> {
    return this.http.patch<ITransfertActif>(
      `${this.resourceUrl}/${encodeURIComponent(this.getTransfertActifIdentifier(transfertActif))}`,
      transfertActif,
    );
  }

  find(id: number): Observable<ITransfertActif> {
    return this.http.get<ITransfertActif>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ITransfertActif[]>> {
    const options = createRequestOption(req);
    return this.http.get<ITransfertActif[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getTransfertActifIdentifier(transfertActif: Pick<ITransfertActif, 'id'>): number {
    return transfertActif.id;
  }

  compareTransfertActif(o1: Pick<ITransfertActif, 'id'> | null, o2: Pick<ITransfertActif, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransfertActifIdentifier(o1) === this.getTransfertActifIdentifier(o2) : o1 === o2;
  }

  addTransfertActifToCollectionIfMissing<Type extends Pick<ITransfertActif, 'id'>>(
    transfertActifCollection: Type[],
    ...transfertActifsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transfertActifs: Type[] = transfertActifsToCheck.filter(
      transfertActifItem => transfertActifItem !== null && transfertActifItem !== undefined,
    );
    if (transfertActifs.length > 0) {
      const transfertActifCollectionIdentifiers = transfertActifCollection.map(transfertActifItem =>
        this.getTransfertActifIdentifier(transfertActifItem),
      );
      const transfertActifsToAdd = transfertActifs.filter(transfertActifItem => {
        const transfertActifIdentifier = this.getTransfertActifIdentifier(transfertActifItem);
        if (transfertActifCollectionIdentifiers.includes(transfertActifIdentifier)) {
          return false;
        }
        transfertActifCollectionIdentifiers.push(transfertActifIdentifier);
        return true;
      });
      return [...transfertActifsToAdd, ...transfertActifCollection];
    }
    return transfertActifCollection;
  }
}
