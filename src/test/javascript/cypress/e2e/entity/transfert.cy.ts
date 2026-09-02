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

describe('Transfert e2e test', () => {
  const transfertPageUrl = '/transfert';
  let username: string;
  let password: string;
  const transfertSample = { dateDemande: '2026-09-02', statut: 'EN_ATTENTE' };

  let transfert;
  let actif;

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
      url: '/api/actifs',
      body: {
        identifiantUnique: 'au défaut de un peu gens',
        codeBarreQR: 'pff à partir de de sorte que',
        type: 'POSTE_TRAVAIL',
        etat: 'EN_MAINTENANCE',
        localisation: 'résoudre',
        dateAcquisition: '2026-09-02',
      },
    }).then(({ body }) => {
      actif = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transferts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transferts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transferts/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });
  });

  afterEach(() => {
    if (transfert) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transferts/${transfert.id}`,
      }).then(() => {
        transfert = undefined;
      });
    }
  });

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

  it('Transferts menu should load Transferts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transfert');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Transfert').should('exist');
    cy.location('pathname').should('eq', transfertPageUrl);
  });

  describe('Transfert page', () => {
    it('should have translated page title', () => {
      cy.visit(transfertPageUrl);
      cy.getEntityHeading('Transfert').should('not.contain', 'gestionActifsDgiApp.transfert.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transfertPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Transfert page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${transfertPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Transfert');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transferts',
          body: {
            ...transfertSample,
            actif,
          },
        }).then(({ body }) => {
          transfert = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transferts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/transferts?page=0&size=20>; rel="last",<http://localhost/api/transferts?page=0&size=20>; rel="first"',
              },
              body: [transfert],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(transfertPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Transfert page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transfert');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertPageUrl);
      });

      it('edit button click should load edit Transfert page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transfert');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertPageUrl);
      });

      it('edit button click should load edit Transfert page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transfert');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertPageUrl);
      });

      it('last delete button click should delete instance of Transfert', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transfert').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', transfertPageUrl);

        transfert = undefined;
      });
    });
  });

  describe('new Transfert page', () => {
    beforeEach(() => {
      cy.visit(transfertPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Transfert');
    });

    it('should create an instance of Transfert', () => {
      cy.get(`[data-cy="dateDemande"]`).type('2026-09-02');
      cy.get(`[data-cy="dateDemande"]`).blur();
      cy.get(`[data-cy="dateDemande"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="statut"]`).select('REJETE');

      cy.get(`[data-cy="commentaireRejet"]`).type('hôte sous');
      cy.get(`[data-cy="commentaireRejet"]`).should('have.value', 'hôte sous');

      cy.get(`[data-cy="dateTraitement"]`).type('2026-09-02');
      cy.get(`[data-cy="dateTraitement"]`).blur();
      cy.get(`[data-cy="dateTraitement"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        transfert = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', transfertPageUrl);
    });
  });
});
