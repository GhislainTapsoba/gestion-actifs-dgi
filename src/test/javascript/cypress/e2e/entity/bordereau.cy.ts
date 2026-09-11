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

describe('Bordereau e2e test', () => {
  const bordereauPageUrl = '/bordereau';
  let username: string;
  let password: string;
  // const bordereauSample = {"numero":"avoir pendant que","dateEmission":"2026-09-10","typeBordereau":"TRANSFERT","statutValidation":"REJETE"};

  let bordereau;
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
      body: {"login":"Leopoldine.Gaillard28","firstName":"Aurélienne","lastName":"Arnaud","email":"Iseult18@yahoo.fr","langKey":"corps ense","imageUrl":"étant donné que chef terriblement"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/bordereaus+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bordereaus').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bordereaus/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transferts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/affectations', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (bordereau) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bordereaus/${bordereau.id}`,
      }).then(() => {
        bordereau = undefined;
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

  it('Bordereaus menu should load Bordereaus page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bordereau');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Bordereau').should('exist');
    cy.location('pathname').should('eq', bordereauPageUrl);
  });

  describe('Bordereau page', () => {
    it('should have translated page title', () => {
      cy.visit(bordereauPageUrl);
      cy.getEntityHeading('Bordereau').should('not.contain', 'gestionActifsDgiApp.bordereau.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(bordereauPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Bordereau page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${bordereauPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Bordereau');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bordereauPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bordereaus',
          body: {
            ...bordereauSample,
            emetteur: user,
          },
        }).then(({ body }) => {
          bordereau = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bordereaus+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/bordereaus?page=0&size=20>; rel="last",<http://localhost/api/bordereaus?page=0&size=20>; rel="first"',
              },
              body: [bordereau],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(bordereauPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(bordereauPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Bordereau page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('bordereau');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bordereauPageUrl);
      });

      it('edit button click should load edit Bordereau page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Bordereau');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bordereauPageUrl);
      });

      it('edit button click should load edit Bordereau page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Bordereau');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bordereauPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Bordereau', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('bordereau').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', bordereauPageUrl);

        bordereau = undefined;
      });
    });
  });

  describe('new Bordereau page', () => {
    beforeEach(() => {
      cy.visit(bordereauPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Bordereau');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Bordereau', () => {
      cy.get(`[data-cy="numero"]`).type('relier');
      cy.get(`[data-cy="numero"]`).should('have.value', 'relier');

      cy.get(`[data-cy="dateEmission"]`).type('2026-09-09');
      cy.get(`[data-cy="dateEmission"]`).blur();
      cy.get(`[data-cy="dateEmission"]`).should('have.value', '2026-09-09');

      cy.get(`[data-cy="typeBordereau"]`).select('TRANSFERT');

      cy.get(`[data-cy="statutValidation"]`).select('REJETE');

      cy.get(`[data-cy="dateValidation"]`).type('2026-09-10');
      cy.get(`[data-cy="dateValidation"]`).blur();
      cy.get(`[data-cy="dateValidation"]`).should('have.value', '2026-09-10');

      cy.get(`[data-cy="emetteur"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        bordereau = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', bordereauPageUrl);
    });
  });
});
