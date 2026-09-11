import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IAffectationActif } from '../affectation-actif.model';
import { AffectationActifService } from '../service/affectation-actif.service';

const affectationActifResolve = (route: ActivatedRouteSnapshot): Observable<null | IAffectationActif> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(AffectationActifService);
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

export default affectationActifResolve;
