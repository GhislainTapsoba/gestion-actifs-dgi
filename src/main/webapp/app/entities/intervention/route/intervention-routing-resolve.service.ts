import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IIntervention } from '../intervention.model';
import { InterventionService } from '../service/intervention.service';

const interventionResolve = (route: ActivatedRouteSnapshot): Observable<null | IIntervention> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InterventionService);
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

export default interventionResolve;
