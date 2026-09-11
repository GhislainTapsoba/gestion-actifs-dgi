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

describe('PlanningMaintenance e2e test', () => {
  const planningMaintenancePageUrl = '/planning-maintenance';
  let username: string;
  let password: string;
  const planningMaintenanceSample = { datePrevue: '2026-09-10', statut: 'TERMINER' };

  let planningMaintenance;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/planning-maintenances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/planning-maintenances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/planning-maintenances/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (planningMaintenance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/planning-maintenances/${planningMaintenance.id}`,
      }).then(() => {
        planningMaintenance = undefined;
      });
    }
  });

  it('PlanningMaintenances menu should load PlanningMaintenances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('planning-maintenance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PlanningMaintenance').should('exist');
    cy.location('pathname').should('eq', planningMaintenancePageUrl);
  });

  describe('PlanningMaintenance page', () => {
    it('should have translated page title', () => {
      cy.visit(planningMaintenancePageUrl);
      cy.getEntityHeading('PlanningMaintenance').should('not.contain', 'gestionActifsDgiApp.planningMaintenance.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(planningMaintenancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PlanningMaintenance page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${planningMaintenancePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('PlanningMaintenance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', planningMaintenancePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/planning-maintenances',
          body: planningMaintenanceSample,
        }).then(({ body }) => {
          planningMaintenance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/planning-maintenances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/planning-maintenances?page=0&size=20>; rel="last",<http://localhost/api/planning-maintenances?page=0&size=20>; rel="first"',
              },
              body: [planningMaintenance],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(planningMaintenancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PlanningMaintenance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('planningMaintenance');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', planningMaintenancePageUrl);
      });

      it('edit button click should load edit PlanningMaintenance page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlanningMaintenance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', planningMaintenancePageUrl);
      });

      it('edit button click should load edit PlanningMaintenance page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PlanningMaintenance');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', planningMaintenancePageUrl);
      });

      it('last delete button click should delete instance of PlanningMaintenance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('planningMaintenance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', planningMaintenancePageUrl);

        planningMaintenance = undefined;
      });
    });
  });

  describe('new PlanningMaintenance page', () => {
    beforeEach(() => {
      cy.visit(planningMaintenancePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PlanningMaintenance');
    });

    it('should create an instance of PlanningMaintenance', () => {
      cy.get(`[data-cy="datePrevue"]`).type('2026-09-10');
      cy.get(`[data-cy="datePrevue"]`).blur();
      cy.get(`[data-cy="datePrevue"]`).should('have.value', '2026-09-10');

      cy.get(`[data-cy="periodicite"]`).type('cocher');
      cy.get(`[data-cy="periodicite"]`).should('have.value', 'cocher');

      cy.get(`[data-cy="statut"]`).select('EN_COURS');

      cy.get(`[data-cy="description"]`).type('crac');
      cy.get(`[data-cy="description"]`).should('have.value', 'crac');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        planningMaintenance = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', planningMaintenancePageUrl);
    });
  });
});
