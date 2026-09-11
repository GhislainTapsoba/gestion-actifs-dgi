import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { ICategorieMateriel } from '../categorie-materiel.model';
import { CategorieMaterielService } from '../service/categorie-materiel.service';

const categorieMaterielResolve = (route: ActivatedRouteSnapshot): Observable<null | ICategorieMateriel> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(CategorieMaterielService);
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

export default categorieMaterielResolve;
