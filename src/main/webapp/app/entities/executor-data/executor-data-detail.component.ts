import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExecutorData } from 'app/shared/model/executor-data.model';

@Component({
  selector: 'jhi-executor-data-detail',
  templateUrl: './executor-data-detail.component.html',
})
export class ExecutorDataDetailComponent implements OnInit {
  executorData: IExecutorData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ executorData }) => (this.executorData = executorData));
  }

  previousState(): void {
    window.history.back();
  }
}
