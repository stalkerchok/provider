import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IExecutorData, ExecutorData } from 'app/shared/model/executor-data.model';
import { ExecutorDataService } from './executor-data.service';

@Component({
  selector: 'jhi-executor-data-update',
  templateUrl: './executor-data-update.component.html',
})
export class ExecutorDataUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    data: [],
  });

  constructor(protected executorDataService: ExecutorDataService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ executorData }) => {
      this.updateForm(executorData);
    });
  }

  updateForm(executorData: IExecutorData): void {
    this.editForm.patchValue({
      id: executorData.id,
      name: executorData.name,
      data: executorData.data,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const executorData = this.createFromForm();
    if (executorData.id !== undefined) {
      this.subscribeToSaveResponse(this.executorDataService.update(executorData));
    } else {
      this.subscribeToSaveResponse(this.executorDataService.create(executorData));
    }
  }

  private createFromForm(): IExecutorData {
    return {
      ...new ExecutorData(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      data: this.editForm.get(['data'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExecutorData>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
