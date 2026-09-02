import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IFournisseur, NewFournisseur } from '../fournisseur.model';

export type PartialUpdateFournisseur = Partial<IFournisseur> & Pick<IFournisseur, 'id'>;

@Service()
export class FournisseursService {
  readonly fournisseursParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly fournisseursResource = httpResource<IFournisseur[]>(() => {
    const params = this.fournisseursParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of fournisseur that have been fetched. It is updated when the fournisseursResource emits a new value.
   * In case of error while fetching the fournisseurs, the signal is set to an empty array.
   */
  readonly fournisseurs = computed(() => (this.fournisseursResource.hasValue() ? this.fournisseursResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/fournisseurs`;
}

@Service()
export class FournisseurService extends FournisseursService {
  protected readonly http = inject(HttpClient);

  create(fournisseur: NewFournisseur): Observable<IFournisseur> {
    return this.http.post<IFournisseur>(this.resourceUrl, fournisseur);
  }

  update(fournisseur: IFournisseur): Observable<IFournisseur> {
    return this.http.put<IFournisseur>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFournisseurIdentifier(fournisseur))}`,
      fournisseur,
    );
  }

  partialUpdate(fournisseur: PartialUpdateFournisseur): Observable<IFournisseur> {
    return this.http.patch<IFournisseur>(
      `${this.resourceUrl}/${encodeURIComponent(this.getFournisseurIdentifier(fournisseur))}`,
      fournisseur,
    );
  }

  find(id: number): Observable<IFournisseur> {
    return this.http.get<IFournisseur>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IFournisseur[]>> {
    const options = createRequestOption(req);
    return this.http.get<IFournisseur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getFournisseurIdentifier(fournisseur: Pick<IFournisseur, 'id'>): number {
    return fournisseur.id;
  }

  compareFournisseur(o1: Pick<IFournisseur, 'id'> | null, o2: Pick<IFournisseur, 'id'> | null): boolean {
    return o1 && o2 ? this.getFournisseurIdentifier(o1) === this.getFournisseurIdentifier(o2) : o1 === o2;
  }

  addFournisseurToCollectionIfMissing<Type extends Pick<IFournisseur, 'id'>>(
    fournisseurCollection: Type[],
    ...fournisseursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const fournisseurs: Type[] = fournisseursToCheck.filter(fournisseurItem => fournisseurItem !== null && fournisseurItem !== undefined);
    if (fournisseurs.length > 0) {
      const fournisseurCollectionIdentifiers = fournisseurCollection.map(fournisseurItem => this.getFournisseurIdentifier(fournisseurItem));
      const fournisseursToAdd = fournisseurs.filter(fournisseurItem => {
        const fournisseurIdentifier = this.getFournisseurIdentifier(fournisseurItem);
        if (fournisseurCollectionIdentifiers.includes(fournisseurIdentifier)) {
          return false;
        }
        fournisseurCollectionIdentifiers.push(fournisseurIdentifier);
        return true;
      });
      return [...fournisseursToAdd, ...fournisseurCollection];
    }
    return fournisseurCollection;
  }
}
