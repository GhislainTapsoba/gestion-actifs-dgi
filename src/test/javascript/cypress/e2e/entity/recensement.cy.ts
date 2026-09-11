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

describe('Recensement e2e test', () => {
  const recensementPageUrl = '/recensement';
  let username: string;
  let password: string;
  const recensementSample = { dateDebut: '2026-09-10', statut: 'PLANIFIER' };

  let recensement;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/recensements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/recensements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/recensements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (recensement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recensements/${recensement.id}`,
      }).then(() => {
        recensement = undefined;
      });
    }
  });

  it('Recensements menu should load Recensements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('recensement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Recensement').should('exist');
    cy.location('pathname').should('eq', recensementPageUrl);
  });

  describe('Recensement page', () => {
    it('should have translated page title', () => {
      cy.visit(recensementPageUrl);
      cy.getEntityHeading('Recensement').should('not.contain', 'gestionActifsDgiApp.recensement.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(recensementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Recensement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${recensementPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Recensement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recensementPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/recensements',
          body: recensementSample,
        }).then(({ body }) => {
          recensement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/recensements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/recensements?page=0&size=20>; rel="last",<http://localhost/api/recensements?page=0&size=20>; rel="first"',
              },
              body: [recensement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(recensementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Recensement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recensement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recensementPageUrl);
      });

      it('edit button click should load edit Recensement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Recensement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recensementPageUrl);
      });

      it('edit button click should load edit Recensement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Recensement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recensementPageUrl);
      });

      it('last delete button click should delete instance of Recensement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('recensement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', recensementPageUrl);

        recensement = undefined;
      });
    });
  });

  describe('new Recensement page', () => {
    beforeEach(() => {
      cy.visit(recensementPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Recensement');
    });

    it('should create an instance of Recensement', () => {
      cy.get(`[data-cy="dateDebut"]`).type('2026-09-09');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-09-09');

      cy.get(`[data-cy="dateFin"]`).type('2026-09-10');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-09-10');

      cy.get(`[data-cy="statut"]`).select('CLOTUREE');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        recensement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', recensementPageUrl);
    });
  });
});
