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

describe('CategorieMateriel e2e test', () => {
  const categorieMaterielPageUrl = '/categorie-materiel';
  let username: string;
  let password: string;
  const categorieMaterielSample = { libelle: 'badaboum prestataire de services plonger' };

  let categorieMateriel;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/categorie-materiels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/categorie-materiels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/categorie-materiels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (categorieMateriel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/categorie-materiels/${categorieMateriel.id}`,
      }).then(() => {
        categorieMateriel = undefined;
      });
    }
  });

  it('CategorieMateriels menu should load CategorieMateriels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('categorie-materiel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CategorieMateriel').should('exist');
    cy.location('pathname').should('eq', categorieMaterielPageUrl);
  });

  describe('CategorieMateriel page', () => {
    it('should have translated page title', () => {
      cy.visit(categorieMaterielPageUrl);
      cy.getEntityHeading('CategorieMateriel').should('not.contain', 'gestionActifsDgiApp.categorieMateriel.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(categorieMaterielPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CategorieMateriel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${categorieMaterielPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CategorieMateriel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', categorieMaterielPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/categorie-materiels',
          body: categorieMaterielSample,
        }).then(({ body }) => {
          categorieMateriel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/categorie-materiels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/categorie-materiels?page=0&size=20>; rel="last",<http://localhost/api/categorie-materiels?page=0&size=20>; rel="first"',
              },
              body: [categorieMateriel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(categorieMaterielPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CategorieMateriel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('categorieMateriel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', categorieMaterielPageUrl);
      });

      it('edit button click should load edit CategorieMateriel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CategorieMateriel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', categorieMaterielPageUrl);
      });

      it('edit button click should load edit CategorieMateriel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CategorieMateriel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', categorieMaterielPageUrl);
      });

      it('last delete button click should delete instance of CategorieMateriel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('categorieMateriel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', categorieMaterielPageUrl);

        categorieMateriel = undefined;
      });
    });
  });

  describe('new CategorieMateriel page', () => {
    beforeEach(() => {
      cy.visit(categorieMaterielPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CategorieMateriel');
    });

    it('should create an instance of CategorieMateriel', () => {
      cy.get(`[data-cy="libelle"]`).type('athlète');
      cy.get(`[data-cy="libelle"]`).should('have.value', 'athlète');

      cy.get(`[data-cy="description"]`).type('grrr');
      cy.get(`[data-cy="description"]`).should('have.value', 'grrr');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        categorieMateriel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', categorieMaterielPageUrl);
    });
  });
});
