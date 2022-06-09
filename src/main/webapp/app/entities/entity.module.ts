import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'manager-data',
        loadChildren: () => import('./manager-data/manager-data.module').then(m => m.ProviderManagerDataModule),
      },
      {
        path: 'executor-data',
        loadChildren: () => import('./executor-data/executor-data.module').then(m => m.ProviderExecutorDataModule),
      },
      {
        path: 'temporary-access',
        loadChildren: () => import('./temporary-access/temporary-access.module').then(m => m.ProviderTemporaryAccessModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class ProviderEntityModule {}
