import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITemporaryAccess } from 'app/shared/model/temporary-access.model';

@Component({
  selector: 'jhi-temporary-access-detail',
  templateUrl: './temporary-access-detail.component.html',
})
export class TemporaryAccessDetailComponent implements OnInit {
  temporaryAccess: ITemporaryAccess | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ temporaryAccess }) => (this.temporaryAccess = temporaryAccess));
  }

  previousState(): void {
    window.history.back();
  }
}
