import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ProviderSharedModule } from 'app/shared/shared.module';
import { TemporaryAccessComponent } from './temporary-access.component';
import { TemporaryAccessDetailComponent } from './temporary-access-detail.component';
import { TemporaryAccessUpdateComponent } from './temporary-access-update.component';
import { TemporaryAccessDeleteDialogComponent } from './temporary-access-delete-dialog.component';
import { temporaryAccessRoute } from './temporary-access.route';

@NgModule({
  imports: [ProviderSharedModule, RouterModule.forChild(temporaryAccessRoute)],
  declarations: [
    TemporaryAccessComponent,
    TemporaryAccessDetailComponent,
    TemporaryAccessUpdateComponent,
    TemporaryAccessDeleteDialogComponent,
  ],
  entryComponents: [TemporaryAccessDeleteDialogComponent],
})
export class ProviderTemporaryAccessModule {}
