import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IManagerData, ManagerData } from 'app/shared/model/manager-data.model';
import { ManagerDataService } from './manager-data.service';

@Component({
  selector: 'jhi-manager-data-update',
  templateUrl: './manager-data-update.component.html',
})
export class ManagerDataUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    data: [],
  });

  constructor(protected managerDataService: ManagerDataService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ managerData }) => {
      this.updateForm(managerData);
    });
  }

  updateForm(managerData: IManagerData): void {
    this.editForm.patchValue({
      id: managerData.id,
      name: managerData.name,
      data: managerData.data,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const managerData = this.createFromForm();
    if (managerData.id !== undefined) {
      this.subscribeToSaveResponse(this.managerDataService.update(managerData));
    } else {
      this.subscribeToSaveResponse(this.managerDataService.create(managerData));
    }
  }

  private createFromForm(): IManagerData {
    return {
      ...new ManagerData(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      data: this.editForm.get(['data'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManagerData>>): void {
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
