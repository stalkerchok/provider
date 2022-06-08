import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IManagerData, ManagerData } from 'app/shared/model/manager-data.model';
import { ManagerDataService } from './manager-data.service';
import { ManagerDataComponent } from './manager-data.component';
import { ManagerDataDetailComponent } from './manager-data-detail.component';
import { ManagerDataUpdateComponent } from './manager-data-update.component';

@Injectable({ providedIn: 'root' })
export class ManagerDataResolve implements Resolve<IManagerData> {
  constructor(private service: ManagerDataService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManagerData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((managerData: HttpResponse<ManagerData>) => {
          if (managerData.body) {
            return of(managerData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ManagerData());
  }
}

export const managerDataRoute: Routes = [
  {
    path: '',
    component: ManagerDataComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'providerApp.managerData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManagerDataDetailComponent,
    resolve: {
      managerData: ManagerDataResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.managerData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManagerDataUpdateComponent,
    resolve: {
      managerData: ManagerDataResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.managerData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManagerDataUpdateComponent,
    resolve: {
      managerData: ManagerDataResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.managerData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
