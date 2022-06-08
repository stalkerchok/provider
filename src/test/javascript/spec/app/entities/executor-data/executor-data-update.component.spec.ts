import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ProviderTestModule } from '../../../test.module';
import { ExecutorDataUpdateComponent } from 'app/entities/executor-data/executor-data-update.component';
import { ExecutorDataService } from 'app/entities/executor-data/executor-data.service';
import { ExecutorData } from 'app/shared/model/executor-data.model';

describe('Component Tests', () => {
  describe('ExecutorData Management Update Component', () => {
    let comp: ExecutorDataUpdateComponent;
    let fixture: ComponentFixture<ExecutorDataUpdateComponent>;
    let service: ExecutorDataService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [ExecutorDataUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ExecutorDataUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExecutorDataUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ExecutorDataService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ExecutorData(123);
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
        const entity = new ExecutorData();
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
