import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IExecutorData } from 'app/shared/model/executor-data.model';

type EntityResponseType = HttpResponse<IExecutorData>;
type EntityArrayResponseType = HttpResponse<IExecutorData[]>;

@Injectable({ providedIn: 'root' })
export class ExecutorDataService {
  public resourceUrl = SERVER_API_URL + 'api/executor-data';

  constructor(protected http: HttpClient) {}

  create(executorData: IExecutorData): Observable<EntityResponseType> {
    return this.http.post<IExecutorData>(this.resourceUrl, executorData, { observe: 'response' });
  }

  update(executorData: IExecutorData): Observable<EntityResponseType> {
    return this.http.put<IExecutorData>(this.resourceUrl, executorData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IExecutorData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IExecutorData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
