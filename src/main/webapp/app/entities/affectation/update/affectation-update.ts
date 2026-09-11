import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbInputDatepicker } from '@ng-bootstrap/ng-bootstrap/datepicker';
import { Observable, finalize, map } from 'rxjs';

import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAffectation } from '../affectation.model';
import { AffectationService } from '../service/affectation.service';

import { AffectationFormGroup, AffectationFormService } from './affectation-form.service';

@Component({
  selector: 'jhi-affectation-update',
  templateUrl: './affectation-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule, NgbInputDatepicker],
})
export class AffectationUpdate implements OnInit {
  readonly isSaving = signal(false);
  affectation: IAffectation | null = null;

  agentsSharedCollection = signal<IAgent[]>([]);

  protected affectationService = inject(AffectationService);
  protected affectationFormService = inject(AffectationFormService);
  protected agentService = inject(AgentService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AffectationFormGroup = this.affectationFormService.createAffectationFormGroup();

  compareAgent = (o1: IAgent | null, o2: IAgent | null): boolean => this.agentService.compareAgent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ affectation }) => {
      this.affectation = affectation;
      if (affectation) {
        this.updateForm(affectation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const affectation = this.affectationFormService.getAffectation(this.editForm);
    if (affectation.id === null) {
      this.subscribeToSaveResponse(this.affectationService.create(affectation));
    } else {
      this.subscribeToSaveResponse(this.affectationService.update(affectation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAffectation | null>): void {
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

  protected updateForm(affectation: IAffectation): void {
    this.affectation = affectation;
    this.affectationFormService.resetForm(this.editForm, affectation);

    this.agentsSharedCollection.update(agents => this.agentService.addAgentToCollectionIfMissing<IAgent>(agents, affectation.agent));
  }

  protected loadRelationshipsOptions(): void {
    this.agentService
      .query()
      .pipe(map((res: HttpResponse<IAgent[]>) => res.body ?? []))
      .pipe(map((agents: IAgent[]) => this.agentService.addAgentToCollectionIfMissing<IAgent>(agents, this.affectation?.agent)))
      .subscribe((agents: IAgent[]) => this.agentsSharedCollection.set(agents));
  }
}
