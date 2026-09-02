import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { TransfertService } from '../service/transfert.service';
import { ITransfert } from '../transfert.model';

const transfertResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransfert> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(TransfertService);
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

export default transfertResolve;
