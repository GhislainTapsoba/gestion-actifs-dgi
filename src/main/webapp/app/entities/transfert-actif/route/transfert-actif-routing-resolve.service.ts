import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TransfertActifService } from '../service/transfert-actif.service';
import { ITransfertActif } from '../transfert-actif.model';

const transfertActifResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransfertActif> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TransfertActifService);
    return service.find(id).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 404) {
          router.navigate(['404']);
        } else {
          router.navigate(['error']);
        }
        return EMPTY;
      }),
    );
  }

  return of(null);
};

export default transfertActifResolve;
