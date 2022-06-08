import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProviderTestModule } from '../../../test.module';
import { ExecutorDataDetailComponent } from 'app/entities/executor-data/executor-data-detail.component';
import { ExecutorData } from 'app/shared/model/executor-data.model';

describe('Component Tests', () => {
  describe('ExecutorData Management Detail Component', () => {
    let comp: ExecutorDataDetailComponent;
    let fixture: ComponentFixture<ExecutorDataDetailComponent>;
    const route = ({ data: of({ executorData: new ExecutorData(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [ExecutorDataDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ExecutorDataDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExecutorDataDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load executorData on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.executorData).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
