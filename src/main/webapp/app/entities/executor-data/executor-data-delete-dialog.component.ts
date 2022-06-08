import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IExecutorData } from 'app/shared/model/executor-data.model';
import { ExecutorDataService } from './executor-data.service';

@Component({
  templateUrl: './executor-data-delete-dialog.component.html',
})
export class ExecutorDataDeleteDialogComponent {
  executorData?: IExecutorData;

  constructor(
    protected executorDataService: ExecutorDataService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.executorDataService.delete(id).subscribe(() => {
      this.eventManager.broadcast('executorDataListModification');
      this.activeModal.close();
    });
  }
}
