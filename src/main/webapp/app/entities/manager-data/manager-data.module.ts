import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProviderSharedModule } from 'app/shared/shared.module';
import { ManagerDataComponent } from './manager-data.component';
import { ManagerDataDetailComponent } from './manager-data-detail.component';
import { ManagerDataUpdateComponent } from './manager-data-update.component';
import { ManagerDataDeleteDialogComponent } from './manager-data-delete-dialog.component';
import { managerDataRoute } from './manager-data.route';

@NgModule({
  imports: [ProviderSharedModule, RouterModule.forChild(managerDataRoute)],
  declarations: [ManagerDataComponent, ManagerDataDetailComponent, ManagerDataUpdateComponent, ManagerDataDeleteDialogComponent],
  entryComponents: [ManagerDataDeleteDialogComponent],
})
export class ProviderManagerDataModule {}
