import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProviderTestModule } from '../../../test.module';
import { ManagerDataDetailComponent } from 'app/entities/manager-data/manager-data-detail.component';
import { ManagerData } from 'app/shared/model/manager-data.model';

describe('Component Tests', () => {
  describe('ManagerData Management Detail Component', () => {
    let comp: ManagerDataDetailComponent;
    let fixture: ComponentFixture<ManagerDataDetailComponent>;
    const route = ({ data: of({ managerData: new ManagerData(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [ManagerDataDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ManagerDataDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ManagerDataDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load managerData on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.managerData).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
