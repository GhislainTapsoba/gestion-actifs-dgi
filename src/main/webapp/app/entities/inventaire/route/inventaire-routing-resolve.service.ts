import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IInventaire } from '../inventaire.model';
import { InventaireService } from '../service/inventaire.service';

const inventaireResolve = (route: ActivatedRouteSnapshot): Observable<null | IInventaire> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(InventaireService);
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

export default inventaireResolve;
