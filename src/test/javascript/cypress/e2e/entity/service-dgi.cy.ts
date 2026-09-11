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

describe('ServiceDgi e2e test', () => {
  const serviceDgiPageUrl = '/service-dgi';
  let username: string;
  let password: string;
  const serviceDgiSample = { nomService: 'lentement super sale' };

  let serviceDgi;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/service-dgis+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/service-dgis').as('postEntityRequest');
    cy.intercept('DELETE', '/api/service-dgis/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (serviceDgi) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-dgis/${serviceDgi.id}`,
      }).then(() => {
        serviceDgi = undefined;
      });
    }
  });

  it('ServiceDgis menu should load ServiceDgis page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('service-dgi');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServiceDgi').should('exist');
    cy.location('pathname').should('eq', serviceDgiPageUrl);
  });

  describe('ServiceDgi page', () => {
    it('should have translated page title', () => {
      cy.visit(serviceDgiPageUrl);
      cy.getEntityHeading('ServiceDgi').should('not.contain', 'gestionActifsDgiApp.serviceDgi.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(serviceDgiPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServiceDgi page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${serviceDgiPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ServiceDgi');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', serviceDgiPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/service-dgis',
          body: serviceDgiSample,
        }).then(({ body }) => {
          serviceDgi = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/service-dgis+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/service-dgis?page=0&size=20>; rel="last",<http://localhost/api/service-dgis?page=0&size=20>; rel="first"',
              },
              body: [serviceDgi],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(serviceDgiPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ServiceDgi page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('serviceDgi');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', serviceDgiPageUrl);
      });

      it('edit button click should load edit ServiceDgi page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceDgi');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', serviceDgiPageUrl);
      });

      it('edit button click should load edit ServiceDgi page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceDgi');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', serviceDgiPageUrl);
      });

      it('last delete button click should delete instance of ServiceDgi', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('serviceDgi').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', serviceDgiPageUrl);

        serviceDgi = undefined;
      });
    });
  });

  describe('new ServiceDgi page', () => {
    beforeEach(() => {
      cy.visit(serviceDgiPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServiceDgi');
    });

    it('should create an instance of ServiceDgi', () => {
      cy.get(`[data-cy="nomService"]`).type('arrière corps enseignant prou');
      cy.get(`[data-cy="nomService"]`).should('have.value', 'arrière corps enseignant prou');

      cy.get(`[data-cy="chefService"]`).type('autant du moment que');
      cy.get(`[data-cy="chefService"]`).should('have.value', 'autant du moment que');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        serviceDgi = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', serviceDgiPageUrl);
    });
  });
});
