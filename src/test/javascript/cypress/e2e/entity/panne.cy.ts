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

describe('Panne e2e test', () => {
  const pannePageUrl = '/panne';
  let username: string;
  let password: string;
  // const panneSample = {"description":"bien penser","dateDeclaration":"2026-09-10","statutPanne":"SIGNALEE"};

  let panne;
  // let actif;

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
      url: '/api/actifs',
      body: {"codeInventaire":"en bas de éliminer","designation":"deviner taper","marque":"dès à seule fin de consentir","modele":"commenter","numeroSerie":"ouf hi","codeBarre":"désagréable toc","type":"IMPRIMANTE","etat":"PERDU_VOLE","localisation":"désagréable","dateAcquisition":"2026-09-02","valeurAcquisition":308.99},
    }).then(({ body }) => {
      actif = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/pannes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/pannes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/pannes/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });

  });
   */

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

  /* Disabled due to incompatibility
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
   */

  it('Pannes menu should load Pannes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('panne');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Panne').should('exist');
    cy.location('pathname').should('eq', pannePageUrl);
  });

  describe('Panne page', () => {
    it('should have translated page title', () => {
      cy.visit(pannePageUrl);
      cy.getEntityHeading('Panne').should('not.contain', 'gestionActifsDgiApp.panne.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(pannePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Panne page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${pannePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Panne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pannePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/pannes',
          body: {
            ...panneSample,
            actif: actif,
          },
        }).then(({ body }) => {
          panne = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/pannes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/pannes?page=0&size=20>; rel="last",<http://localhost/api/pannes?page=0&size=20>; rel="first"',
              },
              body: [panne],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(pannePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(pannePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Panne page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('panne');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pannePageUrl);
      });

      it('edit button click should load edit Panne page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Panne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pannePageUrl);
      });

      it('edit button click should load edit Panne page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Panne');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pannePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Panne', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('panne').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', pannePageUrl);

        panne = undefined;
      });
    });
  });

  describe('new Panne page', () => {
    beforeEach(() => {
      cy.visit(pannePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Panne');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Panne', () => {
      cy.get(`[data-cy="description"]`).type('tout à fait carrément à seule fin de');
      cy.get(`[data-cy="description"]`).should('have.value', 'tout à fait carrément à seule fin de');

      cy.get(`[data-cy="dateDeclaration"]`).type('2026-09-10');
      cy.get(`[data-cy="dateDeclaration"]`).blur();
      cy.get(`[data-cy="dateDeclaration"]`).should('have.value', '2026-09-10');

      cy.get(`[data-cy="statutPanne"]`).select('SIGNALEE');

      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        panne = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', pannePageUrl);
    });
  });
});
