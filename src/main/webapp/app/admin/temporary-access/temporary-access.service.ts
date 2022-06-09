import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITemporaryAccess } from 'app/shared/model/temporary-access.model';

type EntityResponseType = HttpResponse<ITemporaryAccess>;
type EntityArrayResponseType = HttpResponse<ITemporaryAccess[]>;

@Injectable({ providedIn: 'root' })
export class TemporaryAccessService {
  public resourceUrl = SERVER_API_URL + 'api/temporary-accesses';

  constructor(protected http: HttpClient) {}

  create(temporaryAccess: ITemporaryAccess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temporaryAccess);
    return this.http
      .post<ITemporaryAccess>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(temporaryAccess: ITemporaryAccess): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(temporaryAccess);
    return this.http
      .put<ITemporaryAccess>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITemporaryAccess>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITemporaryAccess[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(temporaryAccess: ITemporaryAccess): ITemporaryAccess {
    const copy: ITemporaryAccess = Object.assign({}, temporaryAccess, {
      endDate: temporaryAccess.endDate && temporaryAccess.endDate.isValid() ? temporaryAccess.endDate.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.endDate = res.body.endDate ? moment(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((temporaryAccess: ITemporaryAccess) => {
        temporaryAccess.endDate = temporaryAccess.endDate ? moment(temporaryAccess.endDate) : undefined;
      });
    }
    return res;
  }
}
