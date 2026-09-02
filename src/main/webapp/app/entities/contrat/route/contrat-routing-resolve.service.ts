import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IContrat } from '../contrat.model';
import { ContratService } from '../service/contrat.service';

const contratResolve = (route: ActivatedRouteSnapshot): Observable<null | IContrat> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ContratService);
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

export default contratResolve;
