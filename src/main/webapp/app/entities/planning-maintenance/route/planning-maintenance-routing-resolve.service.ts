import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, catchError, of } from 'rxjs';

import { IPlanningMaintenance } from '../planning-maintenance.model';
import { PlanningMaintenanceService } from '../service/planning-maintenance.service';

const planningMaintenanceResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlanningMaintenance> => {
  const { id } = route.params;
  if (id) {
    const router = inject(Router);
    const service = inject(PlanningMaintenanceService);
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

export default planningMaintenanceResolve;
