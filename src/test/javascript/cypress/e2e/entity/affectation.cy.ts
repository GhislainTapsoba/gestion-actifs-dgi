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

describe('Affectation e2e test', () => {
  const affectationPageUrl = '/affectation';
  let username: string;
  let password: string;
  const affectationSample = { dateAffectation: '2026-09-01' };

  let affectation;
  let actif;

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
      url: '/api/actifs',
      body: {
        identifiantUnique: 'venir',
        codeBarreQR: 'au point que glouglou grandement',
        type: 'SERVEUR',
        etat: 'REFORME',
        localisation: 'horrible plouf au-dehors',
        dateAcquisition: '2026-09-02',
      },
    }).then(({ body }) => {
      actif = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/affectations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/affectations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/affectations/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });
  });

  afterEach(() => {
    if (affectation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/affectations/${affectation.id}`,
      }).then(() => {
        affectation = undefined;
      });
    }
  });

  afterEach(() => {
    if (actif) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/actifs/${actif.id}`,
      }).then(() => {
        actif = undefined;
      });
    }
  });

  it('Affectations menu should load Affectations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('affectation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Affectation').should('exist');
    cy.location('pathname').should('eq', affectationPageUrl);
  });

  describe('Affectation page', () => {
    it('should have translated page title', () => {
      cy.visit(affectationPageUrl);
      cy.getEntityHeading('Affectation').should('not.contain', 'gestionActifsDgiApp.affectation.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(affectationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Affectation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${affectationPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Affectation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/affectations',
          body: {
            ...affectationSample,
            actif,
          },
        }).then(({ body }) => {
          affectation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/affectations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/affectations?page=0&size=20>; rel="last",<http://localhost/api/affectations?page=0&size=20>; rel="first"',
              },
              body: [affectation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(affectationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Affectation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('affectation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationPageUrl);
      });

      it('edit button click should load edit Affectation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Affectation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationPageUrl);
      });

      it('edit button click should load edit Affectation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Affectation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationPageUrl);
      });

      it('last delete button click should delete instance of Affectation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('affectation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationPageUrl);

        affectation = undefined;
      });
    });
  });

  describe('new Affectation page', () => {
    beforeEach(() => {
      cy.visit(affectationPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Affectation');
    });

    it('should create an instance of Affectation', () => {
      cy.get(`[data-cy="dateAffectation"]`).type('2026-09-02');
      cy.get(`[data-cy="dateAffectation"]`).blur();
      cy.get(`[data-cy="dateAffectation"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="dateRestitution"]`).type('2026-09-02');
      cy.get(`[data-cy="dateRestitution"]`).blur();
      cy.get(`[data-cy="dateRestitution"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="numeroBordereau"]`).type('oh');
      cy.get(`[data-cy="numeroBordereau"]`).should('have.value', 'oh');

      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        affectation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', affectationPageUrl);
    });
  });
});
