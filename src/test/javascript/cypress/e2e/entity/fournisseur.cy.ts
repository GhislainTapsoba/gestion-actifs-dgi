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

describe('Fournisseur e2e test', () => {
  const fournisseurPageUrl = '/fournisseur';
  let username: string;
  let password: string;
  const fournisseurSample = { nom: 'ouin vlan' };

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
    cy.intercept('GET', '/api/fournisseurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fournisseurs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fournisseurs/*').as('deleteEntityRequest');
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

  it('Fournisseurs menu should load Fournisseurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fournisseur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Fournisseur').should('exist');
    cy.location('pathname').should('eq', fournisseurPageUrl);
  });

  describe('Fournisseur page', () => {
    it('should have translated page title', () => {
      cy.visit(fournisseurPageUrl);
      cy.getEntityHeading('Fournisseur').should('not.contain', 'gestionActifsDgiApp.fournisseur.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fournisseurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Fournisseur page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${fournisseurPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Fournisseur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fournisseurPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fournisseurs',
          body: fournisseurSample,
        }).then(({ body }) => {
          fournisseur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fournisseurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [fournisseur],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fournisseurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Fournisseur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('fournisseur');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fournisseurPageUrl);
      });

      it('edit button click should load edit Fournisseur page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fournisseur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fournisseurPageUrl);
      });

      it('edit button click should load edit Fournisseur page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Fournisseur');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fournisseurPageUrl);
      });

      it('last delete button click should delete instance of Fournisseur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('fournisseur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', fournisseurPageUrl);

        fournisseur = undefined;
      });
    });
  });

  describe('new Fournisseur page', () => {
    beforeEach(() => {
      cy.visit(fournisseurPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Fournisseur');
    });

    it('should create an instance of Fournisseur', () => {
      cy.get(`[data-cy="nom"]`).type('devant aïe');
      cy.get(`[data-cy="nom"]`).should('have.value', 'devant aïe');

      cy.get(`[data-cy="contact"]`).type('circulaire');
      cy.get(`[data-cy="contact"]`).should('have.value', 'circulaire');

      cy.get(`[data-cy="email"]`).type('Abigail.Mercier@hotmail.fr');
      cy.get(`[data-cy="email"]`).should('have.value', 'Abigail.Mercier@hotmail.fr');

      cy.get(`[data-cy="telephone"]`).type('0238692954');
      cy.get(`[data-cy="telephone"]`).should('have.value', '0238692954');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        fournisseur = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', fournisseurPageUrl);
    });
  });
});
