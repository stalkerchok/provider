import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ProviderTestModule } from '../../../test.module';
import { ManagerDataUpdateComponent } from 'app/entities/manager-data/manager-data-update.component';
import { ManagerDataService } from 'app/entities/manager-data/manager-data.service';
import { ManagerData } from 'app/shared/model/manager-data.model';

describe('Component Tests', () => {
  describe('ManagerData Management Update Component', () => {
    let comp: ManagerDataUpdateComponent;
    let fixture: ComponentFixture<ManagerDataUpdateComponent>;
    let service: ManagerDataService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [ManagerDataUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ManagerDataUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ManagerDataUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ManagerDataService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ManagerData(123);
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
        const entity = new ManagerData();
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
