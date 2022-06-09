import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITemporaryAccess, TemporaryAccess } from 'app/shared/model/temporary-access.model';
import { TemporaryAccessService } from './temporary-access.service';
import { TemporaryAccessComponent } from './temporary-access.component';
import { TemporaryAccessDetailComponent } from './temporary-access-detail.component';
import { TemporaryAccessUpdateComponent } from './temporary-access-update.component';

@Injectable({ providedIn: 'root' })
export class TemporaryAccessResolve implements Resolve<ITemporaryAccess> {
  constructor(private service: TemporaryAccessService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITemporaryAccess> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((temporaryAccess: HttpResponse<TemporaryAccess>) => {
          if (temporaryAccess.body) {
            return of(temporaryAccess.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TemporaryAccess());
  }
}

export const temporaryAccessRoute: Routes = [
  {
    path: '',
    component: TemporaryAccessComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'providerApp.temporaryAccess.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TemporaryAccessDetailComponent,
    resolve: {
      temporaryAccess: TemporaryAccessResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.temporaryAccess.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TemporaryAccessUpdateComponent,
    resolve: {
      temporaryAccess: TemporaryAccessResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.temporaryAccess.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TemporaryAccessUpdateComponent,
    resolve: {
      temporaryAccess: TemporaryAccessResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.temporaryAccess.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
