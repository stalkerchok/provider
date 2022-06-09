import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITemporaryAccess, TemporaryAccess } from 'app/shared/model/temporary-access.model';
import { TemporaryAccessService } from './temporary-access.service';

@Component({
  selector: 'jhi-temporary-access-update',
  templateUrl: './temporary-access-update.component.html',
})
export class TemporaryAccessUpdateComponent implements OnInit {
  isSaving = false;
  endDateDp: any;

  editForm = this.fb.group({
    id: [],
    login: [],
    endDate: [],
    permissionType: [],
    role: [],
    entityClass: [],
    entityId: [],
  });

  constructor(
    protected temporaryAccessService: TemporaryAccessService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temporaryAccess }) => {
      this.updateForm(temporaryAccess);
    });
  }

  updateForm(temporaryAccess: ITemporaryAccess): void {
    this.editForm.patchValue({
      id: temporaryAccess.id,
      login: temporaryAccess.login,
      endDate: temporaryAccess.endDate,
      permissionType: temporaryAccess.permissionType,
      role: temporaryAccess.role,
      entityClass: temporaryAccess.entityClass,
      entityId: temporaryAccess.entityId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const temporaryAccess = this.createFromForm();
    if (temporaryAccess.id !== undefined) {
      this.subscribeToSaveResponse(this.temporaryAccessService.update(temporaryAccess));
    } else {
      this.subscribeToSaveResponse(this.temporaryAccessService.create(temporaryAccess));
    }
  }

  private createFromForm(): ITemporaryAccess {
    return {
      ...new TemporaryAccess(),
      id: this.editForm.get(['id'])!.value,
      login: this.editForm.get(['login'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      permissionType: this.editForm.get(['permissionType'])!.value,
      role: this.editForm.get(['role'])!.value,
      entityClass: this.editForm.get(['entityClass'])!.value,
      entityId: this.editForm.get(['entityId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITemporaryAccess>>): void {
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
