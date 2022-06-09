import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProviderTestModule } from '../../../test.module';
import { TemporaryAccessDetailComponent } from 'app/entities/temporary-access/temporary-access-detail.component';
import { TemporaryAccess } from 'app/shared/model/temporary-access.model';

describe('Component Tests', () => {
  describe('TemporaryAccess Management Detail Component', () => {
    let comp: TemporaryAccessDetailComponent;
    let fixture: ComponentFixture<TemporaryAccessDetailComponent>;
    const route = ({ data: of({ temporaryAccess: new TemporaryAccess(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [TemporaryAccessDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TemporaryAccessDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TemporaryAccessDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load temporaryAccess on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.temporaryAccess).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
