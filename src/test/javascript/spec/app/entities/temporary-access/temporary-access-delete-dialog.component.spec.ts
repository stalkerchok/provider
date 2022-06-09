import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ProviderTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { TemporaryAccessDeleteDialogComponent } from 'app/entities/temporary-access/temporary-access-delete-dialog.component';
import { TemporaryAccessService } from 'app/entities/temporary-access/temporary-access.service';

describe('Component Tests', () => {
  describe('TemporaryAccess Management Delete Component', () => {
    let comp: TemporaryAccessDeleteDialogComponent;
    let fixture: ComponentFixture<TemporaryAccessDeleteDialogComponent>;
    let service: TemporaryAccessService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ProviderTestModule],
        declarations: [TemporaryAccessDeleteDialogComponent],
      })
        .overrideTemplate(TemporaryAccessDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TemporaryAccessDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TemporaryAccessService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
