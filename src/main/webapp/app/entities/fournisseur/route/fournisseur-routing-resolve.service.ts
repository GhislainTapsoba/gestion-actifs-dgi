import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IFournisseur } from '../fournisseur.model';
import { FournisseurService } from '../service/fournisseur.service';

const fournisseurResolve = (route: ActivatedRouteSnapshot): Observable<null | IFournisseur> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(FournisseurService);
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

export default fournisseurResolve;
