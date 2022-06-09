import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ProviderTestModule } from '../../../test.module';
import { TemporaryAccessUpdateComponent } from 'app/entities/temporary-access/temporary-access-update.component';
import { TemporaryAccessService } from 'app/entities/temporary-access/temporary-access.service';
import { TemporaryAccess } from 'app/shared/model/temporary-access.model';

describe('Component Tests', () => {
  describe('TemporaryAccess Management Update Component', () => {
    let comp: TemporaryAccessUpdateComponent;
    let fixture: ComponentFixture<TemporaryAccessUpdateComponent>;
    let service: TemporaryAccessService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [TemporaryAccessUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TemporaryAccessUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TemporaryAccessUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TemporaryAccessService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TemporaryAccess(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new TemporaryAccess();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
