import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { ProviderTestModule } from '../../../test.module';
import { TemporaryAccessComponent } from 'app/entities/temporary-access/temporary-access.component';
import { TemporaryAccessService } from 'app/entities/temporary-access/temporary-access.service';
import { TemporaryAccess } from 'app/shared/model/temporary-access.model';

describe('Component Tests', () => {
  describe('TemporaryAccess Management Component', () => {
    let comp: TemporaryAccessComponent;
    let fixture: ComponentFixture<TemporaryAccessComponent>;
    let service: TemporaryAccessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [TemporaryAccessComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(TemporaryAccessComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TemporaryAccessComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TemporaryAccessService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TemporaryAccess(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.temporaryAccesses && comp.temporaryAccesses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new TemporaryAccess(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.temporaryAccesses && comp.temporaryAccesses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
