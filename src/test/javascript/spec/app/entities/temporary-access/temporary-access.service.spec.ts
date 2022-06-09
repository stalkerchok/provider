import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { TemporaryAccessService } from 'app/admin/temporary-access/temporary-access.service';
import { ITemporaryAccess, TemporaryAccess } from 'app/shared/model/temporary-access.model';
import { PermissionType } from 'app/shared/model/enumerations/permission-type.model';
import { EntityClass } from 'app/shared/model/enumerations/entity-class.model';

describe('Service Tests', () => {
  describe('TemporaryAccess Service', () => {
    let injector: TestBed;
    let service: TemporaryAccessService;
    let httpMock: HttpTestingController;
    let elemDefault: ITemporaryAccess;
    let expectedResult: ITemporaryAccess | ITemporaryAccess[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(TemporaryAccessService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new TemporaryAccess(0, 'AAAAAAA', currentDate, PermissionType.RO, EntityClass.MANAGER_DATA, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TemporaryAccess', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new TemporaryAccess()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TemporaryAccess', () => {
        const returnedFromService = Object.assign(
          {
            login: 'BBBBBB',
            endDate: currentDate.format(DATE_FORMAT),
            permissionType: 'BBBBBB',
            entityClass: 'BBBBBB',
            entityId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TemporaryAccess', () => {
        const returnedFromService = Object.assign(
          {
            login: 'BBBBBB',
            endDate: currentDate.format(DATE_FORMAT),
            permissionType: 'BBBBBB',
            entityClass: 'BBBBBB',
            entityId: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TemporaryAccess', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
