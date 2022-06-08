import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IExecutorData, ExecutorData } from 'app/shared/model/executor-data.model';
import { ExecutorDataService } from './executor-data.service';
import { ExecutorDataComponent } from './executor-data.component';
import { ExecutorDataDetailComponent } from './executor-data-detail.component';
import { ExecutorDataUpdateComponent } from './executor-data-update.component';

@Injectable({ providedIn: 'root' })
export class ExecutorDataResolve implements Resolve<IExecutorData> {
  constructor(private service: ExecutorDataService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExecutorData> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((executorData: HttpResponse<ExecutorData>) => {
          if (executorData.body) {
            return of(executorData.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExecutorData());
  }
}

export const executorDataRoute: Routes = [
  {
    path: '',
    component: ExecutorDataComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'providerApp.executorData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExecutorDataDetailComponent,
    resolve: {
      executorData: ExecutorDataResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.executorData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExecutorDataUpdateComponent,
    resolve: {
      executorData: ExecutorDataResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.executorData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExecutorDataUpdateComponent,
    resolve: {
      executorData: ExecutorDataResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'providerApp.executorData.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
