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

describe('Inventaire e2e test', () => {
  const inventairePageUrl = '/inventaire';
  let username: string;
  let password: string;
  const inventaireSample = { nomFichier: 'snif dans conseil municipal', dateImport: '2026-09-10' };

  let inventaire;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/inventaires+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/inventaires').as('postEntityRequest');
    cy.intercept('DELETE', '/api/inventaires/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inventaire) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/inventaires/${inventaire.id}`,
      }).then(() => {
        inventaire = undefined;
      });
    }
  });

  it('Inventaires menu should load Inventaires page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('inventaire');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Inventaire').should('exist');
    cy.location('pathname').should('eq', inventairePageUrl);
  });

  describe('Inventaire page', () => {
    it('should have translated page title', () => {
      cy.visit(inventairePageUrl);
      cy.getEntityHeading('Inventaire').should('not.contain', 'gestionActifsDgiApp.inventaire.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inventairePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Inventaire page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${inventairePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Inventaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventairePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/inventaires',
          body: inventaireSample,
        }).then(({ body }) => {
          inventaire = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/inventaires+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/inventaires?page=0&size=20>; rel="last",<http://localhost/api/inventaires?page=0&size=20>; rel="first"',
              },
              body: [inventaire],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inventairePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Inventaire page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inventaire');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventairePageUrl);
      });

      it('edit button click should load edit Inventaire page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inventaire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventairePageUrl);
      });

      it('edit button click should load edit Inventaire page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Inventaire');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventairePageUrl);
      });

      it('last delete button click should delete instance of Inventaire', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inventaire').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', inventairePageUrl);

        inventaire = undefined;
      });
    });
  });

  describe('new Inventaire page', () => {
    beforeEach(() => {
      cy.visit(inventairePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Inventaire');
    });

    it('should create an instance of Inventaire', () => {
      cy.get(`[data-cy="nomFichier"]`).type('à cause de');
      cy.get(`[data-cy="nomFichier"]`).should('have.value', 'à cause de');

      cy.get(`[data-cy="dateImport"]`).type('2026-09-10');
      cy.get(`[data-cy="dateImport"]`).blur();
      cy.get(`[data-cy="dateImport"]`).should('have.value', '2026-09-10');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        inventaire = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', inventairePageUrl);
    });
  });
});
