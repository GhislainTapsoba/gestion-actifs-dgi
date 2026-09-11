import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ServiceDgiService } from '../service/service-dgi.service';
import { IServiceDgi } from '../service-dgi.model';

const serviceDgiResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceDgi> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(ServiceDgiService);
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

export default serviceDgiResolve;
