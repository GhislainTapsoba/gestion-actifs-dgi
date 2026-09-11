import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { IAgent, NewAgent } from '../agent.model';

export type PartialUpdateAgent = Partial<IAgent> & Pick<IAgent, 'id'>;

@Service()
export class AgentsService {
  readonly agentsParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(undefined);
  readonly agentsResource = httpResource<IAgent[]>(() => {
    const params = this.agentsParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of agent that have been fetched. It is updated when the agentsResource emits a new value.
   * In case of error while fetching the agents, the signal is set to an empty array.
   */
  readonly agents = computed(() => (this.agentsResource.hasValue() ? this.agentsResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/agents`;
}

@Service()
export class AgentService extends AgentsService {
  protected readonly http = inject(HttpClient);

  create(agent: NewAgent): Observable<IAgent> {
    return this.http.post<IAgent>(this.resourceUrl, agent);
  }

  update(agent: IAgent): Observable<IAgent> {
    return this.http.put<IAgent>(`${this.resourceUrl}/${encodeURIComponent(this.getAgentIdentifier(agent))}`, agent);
  }

  partialUpdate(agent: PartialUpdateAgent): Observable<IAgent> {
    return this.http.patch<IAgent>(`${this.resourceUrl}/${encodeURIComponent(this.getAgentIdentifier(agent))}`, agent);
  }

  find(id: number): Observable<IAgent> {
    return this.http.get<IAgent>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<IAgent[]>> {
    const options = createRequestOption(req);
    return this.http.get<IAgent[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getAgentIdentifier(agent: Pick<IAgent, 'id'>): number {
    return agent.id;
  }

  compareAgent(o1: Pick<IAgent, 'id'> | null, o2: Pick<IAgent, 'id'> | null): boolean {
    return o1 && o2 ? this.getAgentIdentifier(o1) === this.getAgentIdentifier(o2) : o1 === o2;
  }

  addAgentToCollectionIfMissing<Type extends Pick<IAgent, 'id'>>(
    agentCollection: Type[],
    ...agentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const agents: Type[] = agentsToCheck.filter(agentItem => agentItem !== null && agentItem !== undefined);
    if (agents.length > 0) {
      const agentCollectionIdentifiers = agentCollection.map(agentItem => this.getAgentIdentifier(agentItem));
      const agentsToAdd = agents.filter(agentItem => {
        const agentIdentifier = this.getAgentIdentifier(agentItem);
        if (agentCollectionIdentifiers.includes(agentIdentifier)) {
          return false;
        }
        agentCollectionIdentifiers.push(agentIdentifier);
        return true;
      });
      return [...agentsToAdd, ...agentCollection];
    }
    return agentCollection;
  }
}
