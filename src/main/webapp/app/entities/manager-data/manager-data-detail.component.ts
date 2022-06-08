import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManagerData } from 'app/shared/model/manager-data.model';

@Component({
  selector: 'jhi-manager-data-detail',
  templateUrl: './manager-data-detail.component.html',
})
export class ManagerDataDetailComponent implements OnInit {
  managerData: IManagerData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ managerData }) => (this.managerData = managerData));
  }

  previousState(): void {
    window.history.back();
  }
}
