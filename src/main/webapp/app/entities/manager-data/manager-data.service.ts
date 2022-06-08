import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IManagerData } from 'app/shared/model/manager-data.model';

type EntityResponseType = HttpResponse<IManagerData>;
type EntityArrayResponseType = HttpResponse<IManagerData[]>;

@Injectable({ providedIn: 'root' })
export class ManagerDataService {
  public resourceUrl = SERVER_API_URL + 'api/manager-data';

  constructor(protected http: HttpClient) {}

  create(managerData: IManagerData): Observable<EntityResponseType> {
    return this.http.post<IManagerData>(this.resourceUrl, managerData, { observe: 'response' });
  }

  update(managerData: IManagerData): Observable<EntityResponseType> {
    return this.http.put<IManagerData>(this.resourceUrl, managerData, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IManagerData>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IManagerData[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
