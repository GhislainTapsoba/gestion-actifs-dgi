import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IEquipementRecensement } from '../equipement-recensement.model';
import { EquipementRecensementService } from '../service/equipement-recensement.service';

const equipementRecensementResolve = (route: ActivatedRouteSnapshot): Observable<null | IEquipementRecensement> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(EquipementRecensementService);
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

export default equipementRecensementResolve;
