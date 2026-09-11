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

describe('Contrat e2e test', () => {
  const contratPageUrl = '/contrat';
  let username: string;
  let password: string;
  const contratSample = { typeContrat: 'GARANTIE', dateFin: '2026-09-02' };

  let contrat;
  let fournisseur;

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
      url: '/api/fournisseurs',
      body: { nom: 'apte fourbe', contact: 'coin-coin depuis nonobstant', email: 'Philothee95@hotmail.fr', telephone: '+33 399286245' },
    }).then(({ body }) => {
      fournisseur = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contrats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contrats').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contrats/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/fournisseurs', {
      statusCode: 200,
      body: [fournisseur],
    });

    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (contrat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contrats/${contrat.id}`,
      }).then(() => {
        contrat = undefined;
      });
    }
  });

  afterEach(() => {
    if (fournisseur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fournisseurs/${fournisseur.id}`,
      }).then(() => {
        fournisseur = undefined;
      });
    }
  });

  it('Contrats menu should load Contrats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contrat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Contrat').should('exist');
    cy.location('pathname').should('eq', contratPageUrl);
  });

  describe('Contrat page', () => {
    it('should have translated page title', () => {
      cy.visit(contratPageUrl);
      cy.getEntityHeading('Contrat').should('not.contain', 'gestionActifsDgiApp.contrat.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contratPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Contrat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${contratPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Contrat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contratPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contrats',
          body: {
            ...contratSample,
            fournisseur,
          },
        }).then(({ body }) => {
          contrat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contrats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contrats?page=0&size=20>; rel="last",<http://localhost/api/contrats?page=0&size=20>; rel="first"',
              },
              body: [contrat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contratPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Contrat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contrat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contratPageUrl);
      });

      it('edit button click should load edit Contrat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Contrat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contratPageUrl);
      });

      it('edit button click should load edit Contrat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Contrat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contratPageUrl);
      });

      it('last delete button click should delete instance of Contrat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contrat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', contratPageUrl);

        contrat = undefined;
      });
    });
  });

  describe('new Contrat page', () => {
    beforeEach(() => {
      cy.visit(contratPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Contrat');
    });

    it('should create an instance of Contrat', () => {
      cy.get(`[data-cy="typeContrat"]`).select('MAINTENANCE');

      cy.get(`[data-cy="reference"]`).type('du moment que trop peu');
      cy.get(`[data-cy="reference"]`).should('have.value', 'du moment que trop peu');

      cy.get(`[data-cy="dateDebut"]`).type('2026-09-01');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2026-09-01');

      cy.get(`[data-cy="dateFin"]`).type('2026-09-02');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="fournisseur"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contrat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', contratPageUrl);
    });
  });
});
