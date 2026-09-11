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

describe('TransfertActif e2e test', () => {
  const transfertActifPageUrl = '/transfert-actif';
  let username: string;
  let password: string;
  // const transfertActifSample = {};

  let transfertActif;
  // let transfert;
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
      url: '/api/transferts',
      body: {"dateTransfert":"2026-09-02","statut":"EN_ATTENTE","commentaireRejet":"alentour broum franchir","dateTraitement":"2026-09-02"},
    }).then(({ body }) => {
      transfert = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/actifs',
      body: {"codeInventaire":"trop peu","designation":"dense secours","marque":"pas mal alors que assez","modele":"affable","numeroSerie":"retrouver lectorat","codeBarre":"triste selon ressentir","type":"IMPRIMANTE","etat":"PERDU_VOLE","localisation":"quoique","dateAcquisition":"2026-09-02","valeurAcquisition":3178.97},
    }).then(({ body }) => {
      actif = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/transfert-actifs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transfert-actifs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transfert-actifs/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/transferts', {
      statusCode: 200,
      body: [transfert],
    });

    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });

  });
   */

  afterEach(() => {
    if (transfertActif) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transfert-actifs/${transfertActif.id}`,
      }).then(() => {
        transfertActif = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (transfert) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transferts/${transfert.id}`,
      }).then(() => {
        transfert = undefined;
      });
    }
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

  it('TransfertActifs menu should load TransfertActifs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transfert-actif');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TransfertActif').should('exist');
    cy.location('pathname').should('eq', transfertActifPageUrl);
  });

  describe('TransfertActif page', () => {
    it('should have translated page title', () => {
      cy.visit(transfertActifPageUrl);
      cy.getEntityHeading('TransfertActif').should('not.contain', 'gestionActifsDgiApp.transfertActif.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transfertActifPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TransfertActif page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${transfertActifPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('TransfertActif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertActifPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transfert-actifs',
          body: {
            ...transfertActifSample,
            transfert: transfert,
            actif: actif,
          },
        }).then(({ body }) => {
          transfertActif = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transfert-actifs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/transfert-actifs?page=0&size=20>; rel="last",<http://localhost/api/transfert-actifs?page=0&size=20>; rel="first"',
              },
              body: [transfertActif],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transfertActifPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(transfertActifPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details TransfertActif page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transfertActif');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertActifPageUrl);
      });

      it('edit button click should load edit TransfertActif page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransfertActif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertActifPageUrl);
      });

      it('edit button click should load edit TransfertActif page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TransfertActif');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertActifPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of TransfertActif', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transfertActif').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertActifPageUrl);

        transfertActif = undefined;
      });
    });
  });

  describe('new TransfertActif page', () => {
    beforeEach(() => {
      cy.visit(transfertActifPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TransfertActif');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of TransfertActif', () => {
      cy.get(`[data-cy="observation"]`).type('pratiquer agréable');
      cy.get(`[data-cy="observation"]`).should('have.value', 'pratiquer agréable');

      cy.get(`[data-cy="transfert"]`).select(1);
      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        transfertActif = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', transfertActifPageUrl);
    });
  });
});
