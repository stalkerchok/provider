import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITemporaryAccess } from 'app/shared/model/temporary-access.model';
import { TemporaryAccessService } from './temporary-access.service';

@Component({
  templateUrl: './temporary-access-delete-dialog.component.html',
})
export class TemporaryAccessDeleteDialogComponent {
  temporaryAccess?: ITemporaryAccess;

  constructor(
    protected temporaryAccessService: TemporaryAccessService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.temporaryAccessService.delete(id).subscribe(() => {
      this.eventManager.broadcast('temporaryAccessListModification');
      this.activeModal.close();
    });
  }
}
