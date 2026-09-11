import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('Agent e2e test', () => {
  const agentPageUrl = '/agent';
  let username: string;
  let password: string;
  const agentSample = { nom: 'gestionnaire magnifique débile', prenom: 'parlementaire ouin partout' };

  let agent;
  let serviceDgi;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/service-dgis',
      body: { nomService: 'assez âcre', chefService: 'tantôt quelque coin-coin' },
    }).then(({ body }) => {
      serviceDgi = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/agents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/agents').as('postEntityRequest');
    cy.intercept('DELETE', '/api/agents/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/service-dgis', {
      statusCode: 200,
      body: [serviceDgi],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (agent) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/agents/${agent.id}`,
      }).then(() => {
        agent = undefined;
      });
    }
  });

  afterEach(() => {
    if (serviceDgi) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-dgis/${serviceDgi.id}`,
      }).then(() => {
        serviceDgi = undefined;
      });
    }
  });

  it('Agents menu should load Agents page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('agent');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Agent').should('exist');
    cy.location('pathname').should('eq', agentPageUrl);
  });

  describe('Agent page', () => {
    it('should have translated page title', () => {
      cy.visit(agentPageUrl);
      cy.getEntityHeading('Agent').should('not.contain', 'gestionActifsDgiApp.agent.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(agentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Agent page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${agentPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Agent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agentPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/agents',
          body: {
            ...agentSample,
            service: serviceDgi,
          },
        }).then(({ body }) => {
          agent = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/agents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/agents?page=0&size=20>; rel="last",<http://localhost/api/agents?page=0&size=20>; rel="first"',
              },
              body: [agent],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(agentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Agent page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('agent');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agentPageUrl);
      });

      it('edit button click should load edit Agent page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agent');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agentPageUrl);
      });

      it('edit button click should load edit Agent page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Agent');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agentPageUrl);
      });

      it('last delete button click should delete instance of Agent', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('agent').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', agentPageUrl);

        agent = undefined;
      });
    });
  });

  describe('new Agent page', () => {
    beforeEach(() => {
      cy.visit(agentPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Agent');
    });

    it('should create an instance of Agent', () => {
      cy.get(`[data-cy="nom"]`).type('bien que d’autant que population du Québec');
      cy.get(`[data-cy="nom"]`).should('have.value', 'bien que d’autant que population du Québec');

      cy.get(`[data-cy="prenom"]`).type('triangulaire lier');
      cy.get(`[data-cy="prenom"]`).should('have.value', 'triangulaire lier');

      cy.get(`[data-cy="service"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        agent = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', agentPageUrl);
    });
  });
});
