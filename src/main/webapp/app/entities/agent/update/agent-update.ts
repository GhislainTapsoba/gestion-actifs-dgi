import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Observable, finalize, map } from 'rxjs';

import { ServiceDgiService } from 'app/entities/service-dgi/service/service-dgi.service';
import { IServiceDgi } from 'app/entities/service-dgi/service-dgi.model';
import { UserService } from 'app/entities/user/service/user.service';
import { IUser } from 'app/entities/user/user.model';
import { AlertError } from 'app/shared/alert';
import { TranslateDirective } from 'app/shared/language';
import { IAgent } from '../agent.model';
import { AgentService } from '../service/agent.service';

import { AgentFormGroup, AgentFormService } from './agent-form.service';

@Component({
  selector: 'jhi-agent-update',
  templateUrl: './agent-update.html',
  imports: [TranslateDirective, FontAwesomeModule, AlertError, ReactiveFormsModule],
})
export class AgentUpdate implements OnInit {
  readonly isSaving = signal(false);
  agent: IAgent | null = null;

  serviceDgisSharedCollection = signal<IServiceDgi[]>([]);
  usersSharedCollection = signal<IUser[]>([]);

  protected agentService = inject(AgentService);
  protected agentFormService = inject(AgentFormService);
  protected serviceDgiService = inject(ServiceDgiService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AgentFormGroup = this.agentFormService.createAgentFormGroup();

  compareServiceDgi = (o1: IServiceDgi | null, o2: IServiceDgi | null): boolean => this.serviceDgiService.compareServiceDgi(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agent }) => {
      this.agent = agent;
      if (agent) {
        this.updateForm(agent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    globalThis.history.back();
  }

  save(): void {
    this.isSaving.set(true);
    const agent = this.agentFormService.getAgent(this.editForm);
    if (agent.id === null) {
      this.subscribeToSaveResponse(this.agentService.create(agent));
    } else {
      this.subscribeToSaveResponse(this.agentService.update(agent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<IAgent | null>): void {
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

  protected updateForm(agent: IAgent): void {
    this.agent = agent;
    this.agentFormService.resetForm(this.editForm, agent);

    this.serviceDgisSharedCollection.update(serviceDgis =>
      this.serviceDgiService.addServiceDgiToCollectionIfMissing<IServiceDgi>(serviceDgis, agent.service),
    );
    this.usersSharedCollection.update(users => this.userService.addUserToCollectionIfMissing<IUser>(users, agent.utilisateur));
  }

  protected loadRelationshipsOptions(): void {
    this.serviceDgiService
      .query()
      .pipe(map((res: HttpResponse<IServiceDgi[]>) => res.body ?? []))
      .pipe(
        map((serviceDgis: IServiceDgi[]) =>
          this.serviceDgiService.addServiceDgiToCollectionIfMissing<IServiceDgi>(serviceDgis, this.agent?.service),
        ),
      )
      .subscribe((serviceDgis: IServiceDgi[]) => this.serviceDgisSharedCollection.set(serviceDgis));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.agent?.utilisateur)))
      .subscribe((users: IUser[]) => this.usersSharedCollection.set(users));
  }
}
