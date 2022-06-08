import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProviderSharedModule } from 'app/shared/shared.module';
import { ExecutorDataComponent } from './executor-data.component';
import { ExecutorDataDetailComponent } from './executor-data-detail.component';
import { ExecutorDataUpdateComponent } from './executor-data-update.component';
import { ExecutorDataDeleteDialogComponent } from './executor-data-delete-dialog.component';
import { executorDataRoute } from './executor-data.route';

@NgModule({
  imports: [ProviderSharedModule, RouterModule.forChild(executorDataRoute)],
  declarations: [ExecutorDataComponent, ExecutorDataDetailComponent, ExecutorDataUpdateComponent, ExecutorDataDeleteDialogComponent],
  entryComponents: [ExecutorDataDeleteDialogComponent],
})
export class ProviderExecutorDataModule {}
