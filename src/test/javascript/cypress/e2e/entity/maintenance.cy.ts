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

describe('Maintenance e2e test', () => {
  const maintenancePageUrl = '/maintenance';
  let username: string;
  let password: string;
  // const maintenanceSample = {"typeMaintenance":"PREVENTIVE","statut":"OUVERTE"};

  let maintenance;
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
      body: {"codeInventaire":"lectorat","designation":"pardonner nourrir sur","marque":"puisque police smack","modele":"éveiller","numeroSerie":"subito accumuler","codeBarre":"chez","type":"POSTE_TRAVAIL","etat":"EN_SERVICE","localisation":"quelque tandis que","dateAcquisition":"2026-09-02","valeurAcquisition":6197.21},
    }).then(({ body }) => {
      actif = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/maintenances+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/maintenances').as('postEntityRequest');
    cy.intercept('DELETE', '/api/maintenances/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (maintenance) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/maintenances/${maintenance.id}`,
      }).then(() => {
        maintenance = undefined;
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

  it('Maintenances menu should load Maintenances page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('maintenance');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Maintenance').should('exist');
    cy.location('pathname').should('eq', maintenancePageUrl);
  });

  describe('Maintenance page', () => {
    it('should have translated page title', () => {
      cy.visit(maintenancePageUrl);
      cy.getEntityHeading('Maintenance').should('not.contain', 'gestionActifsDgiApp.maintenance.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(maintenancePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Maintenance page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${maintenancePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Maintenance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', maintenancePageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/maintenances',
          body: {
            ...maintenanceSample,
            actif: actif,
          },
        }).then(({ body }) => {
          maintenance = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/maintenances+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/maintenances?page=0&size=20>; rel="last",<http://localhost/api/maintenances?page=0&size=20>; rel="first"',
              },
              body: [maintenance],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(maintenancePageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(maintenancePageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Maintenance page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('maintenance');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', maintenancePageUrl);
      });

      it('edit button click should load edit Maintenance page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Maintenance');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', maintenancePageUrl);
      });

      it('edit button click should load edit Maintenance page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Maintenance');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', maintenancePageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of Maintenance', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('maintenance').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', maintenancePageUrl);

        maintenance = undefined;
      });
    });
  });

  describe('new Maintenance page', () => {
    beforeEach(() => {
      cy.visit(maintenancePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Maintenance');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of Maintenance', () => {
      cy.get(`[data-cy="typeMaintenance"]`).select('PREVENTIVE');

      cy.get(`[data-cy="datePanne"]`).type('2026-09-01');
      cy.get(`[data-cy="datePanne"]`).blur();
      cy.get(`[data-cy="datePanne"]`).should('have.value', '2026-09-01');

      cy.get(`[data-cy="statut"]`).select('CLOTUREE');

      cy.get(`[data-cy="compteRendu"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="compteRendu"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="dateCloture"]`).type('2026-09-02');
      cy.get(`[data-cy="dateCloture"]`).blur();
      cy.get(`[data-cy="dateCloture"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        maintenance = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', maintenancePageUrl);
    });
  });
});
