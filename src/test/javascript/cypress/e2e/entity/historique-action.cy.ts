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

describe('HistoriqueAction e2e test', () => {
  const historiqueActionPageUrl = '/historique-action';
  let username: string;
  let password: string;
  // const historiqueActionSample = {"dateAction":"2026-09-10T13:01:23.424Z","typeAction":"SUPPRESSION"};

  let historiqueAction;
  // let user;

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
      url: '/api/users',
      body: {"login":"Audrey82","firstName":"Nathan","lastName":"Arnaud","email":"Valere_Marty7@yahoo.fr","langKey":"puisque de","imageUrl":"plouf sincère"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/historique-actions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/historique-actions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/historique-actions/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (historiqueAction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/historique-actions/${historiqueAction.id}`,
      }).then(() => {
        historiqueAction = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('HistoriqueActions menu should load HistoriqueActions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historique-action');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HistoriqueAction').should('exist');
    cy.location('pathname').should('eq', historiqueActionPageUrl);
  });

  describe('HistoriqueAction page', () => {
    it('should have translated page title', () => {
      cy.visit(historiqueActionPageUrl);
      cy.getEntityHeading('HistoriqueAction').should('not.contain', 'gestionActifsDgiApp.historiqueAction.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(historiqueActionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HistoriqueAction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${historiqueActionPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('HistoriqueAction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', historiqueActionPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/historique-actions',
          body: {
            ...historiqueActionSample,
            utilisateur: user,
          },
        }).then(({ body }) => {
          historiqueAction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/historique-actions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/historique-actions?page=0&size=20>; rel="last",<http://localhost/api/historique-actions?page=0&size=20>; rel="first"',
              },
              body: [historiqueAction],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(historiqueActionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(historiqueActionPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details HistoriqueAction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historiqueAction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', historiqueActionPageUrl);
      });

      it('edit button click should load edit HistoriqueAction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoriqueAction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', historiqueActionPageUrl);
      });

      it('edit button click should load edit HistoriqueAction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoriqueAction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', historiqueActionPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of HistoriqueAction', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('historiqueAction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', historiqueActionPageUrl);

        historiqueAction = undefined;
      });
    });
  });

  describe('new HistoriqueAction page', () => {
    beforeEach(() => {
      cy.visit(historiqueActionPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HistoriqueAction');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of HistoriqueAction', () => {
      cy.get(`[data-cy="dateAction"]`).type('2026-09-10T22:06');
      cy.get(`[data-cy="dateAction"]`).blur();
      cy.get(`[data-cy="dateAction"]`).should('have.value', '2026-09-10T22:06');

      cy.get(`[data-cy="typeAction"]`).select('AFFECTATION');

      cy.get(`[data-cy="entiteCiblee"]`).type('membre à vie recta membre à vie');
      cy.get(`[data-cy="entiteCiblee"]`).should('have.value', 'membre à vie recta membre à vie');

      cy.get(`[data-cy="ancienneValeur"]`).type('aussitôt que smack');
      cy.get(`[data-cy="ancienneValeur"]`).should('have.value', 'aussitôt que smack');

      cy.get(`[data-cy="nouvelleValeur"]`).type('ronron pendant que');
      cy.get(`[data-cy="nouvelleValeur"]`).should('have.value', 'ronron pendant que');

      cy.get(`[data-cy="utilisateur"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        historiqueAction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', historiqueActionPageUrl);
    });
  });
});
