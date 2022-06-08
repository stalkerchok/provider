import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IManagerData } from 'app/shared/model/manager-data.model';
import { ManagerDataService } from './manager-data.service';

@Component({
  templateUrl: './manager-data-delete-dialog.component.html',
})
export class ManagerDataDeleteDialogComponent {
  managerData?: IManagerData;

  constructor(
    protected managerDataService: ManagerDataService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.managerDataService.delete(id).subscribe(() => {
      this.eventManager.broadcast('managerDataListModification');
      this.activeModal.close();
    });
  }
}
