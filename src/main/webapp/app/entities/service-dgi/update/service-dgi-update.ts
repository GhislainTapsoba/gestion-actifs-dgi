import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize } from 'rxjs';

import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { ServiceDgiService } from '../service/service-dgi.service';
import { IServiceDgi } from '../service-dgi.model';

import { ServiceDgiFormGroup, ServiceDgiFormService } from './service-dgi-form.service';

@Component({
  selector: 'jhi-service-dgi-update',
  templateUrl: './service-dgi-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class ServiceDgiUpdate implements OnInit {
  readonly isSaving = signal(false);
  serviceDgi: IServiceDgi | null = null;

  protected serviceDgiService = inject(ServiceDgiService);
  protected serviceDgiFormService = inject(ServiceDgiFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ServiceDgiFormGroup = this.serviceDgiFormService.createServiceDgiFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceDgi }) => {
      this.serviceDgi = serviceDgi;
      if (serviceDgi) {
        this.updateForm(serviceDgi);
      }
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const serviceDgi = this.serviceDgiFormService.getServiceDgi(this.editForm);
    if (serviceDgi.id === null) {
      this.subscribeToSaveResponse(this.serviceDgiService.create(serviceDgi));
    } else {
      this.subscribeToSaveResponse(this.serviceDgiService.update(serviceDgi));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IServiceDgi | null>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving.set(false);
  }

  protected updateForm(serviceDgi: IServiceDgi): void {
    this.serviceDgi = serviceDgi;
    this.serviceDgiFormService.resetForm(this.editForm, serviceDgi);
  }
}
