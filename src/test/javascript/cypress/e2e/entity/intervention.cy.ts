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

describe('Intervention e2e test', () => {
  const interventionPageUrl = '/intervention';
  let username: string;
  let password: string;
  // const interventionSample = {"dateDeclaration":"2026-09-10","typeIntervention":"CORRECTIVE","statut":"CLOTUREE"};

  let intervention;
  // let panne;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/pannes',
      body: {"description":"après au dépens de depuis","dateDeclaration":"2026-09-10","statutPanne":"RESOLUE"},
    }).then(({ body }) => {
      panne = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/interventions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/interventions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/interventions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/pannes', {
      statusCode: 200,
      body: [panne],
    });

    cy.intercept('GET', '/api/planning-maintenances', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (intervention) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/interventions/${intervention.id}`,
      }).then(() => {
        intervention = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (panne) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/pannes/${panne.id}`,
      }).then(() => {
        panne = undefined;
      });
    }
  });
   */

  it('Interventions menu should load Interventions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('intervention');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Intervention').should('exist');
    cy.location('pathname').should('eq', interventionPageUrl);
  });

  describe('Intervention page', () => {
    it('should have translated page title', () => {
      cy.visit(interventionPageUrl);
      cy.getEntityHeading('Intervention').should('not.contain', 'gestionActifsDgiApp.intervention.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(interventionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Intervention page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${interventionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Intervention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', interventionPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/interventions',
          body: {
            ...interventionSample,
            panne: panne,
          },
        }).then(({ body }) => {
          intervention = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/interventions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/interventions?page=0&size=20>; rel="last",<http://localhost/api/interventions?page=0&size=20>; rel="first"',
              },
              body: [intervention],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(interventionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(interventionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Intervention page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('intervention');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', interventionPageUrl);
      });

      it('edit button click should load edit Intervention page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Intervention');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', interventionPageUrl);
      });

      it('edit button click should load edit Intervention page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Intervention');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', interventionPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Intervention', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('intervention').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', interventionPageUrl);

        intervention = undefined;
      });
    });
  });

  describe('new Intervention page', () => {
    beforeEach(() => {
      cy.visit(interventionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Intervention');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Intervention', () => {
      cy.get(`[data-cy="dateDeclaration"]`).type('2026-09-10');
      cy.get(`[data-cy="dateDeclaration"]`).blur();
      cy.get(`[data-cy="dateDeclaration"]`).should('have.value', '2026-09-10');

      cy.get(`[data-cy="typeIntervention"]`).select('CORRECTIVE');

      cy.get(`[data-cy="statut"]`).select('CLOTUREE');

      cy.get(`[data-cy="description"]`).type('partenaire');
      cy.get(`[data-cy="description"]`).should('have.value', 'partenaire');

      cy.get(`[data-cy="panne"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        intervention = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', interventionPageUrl);
    });
  });
});
