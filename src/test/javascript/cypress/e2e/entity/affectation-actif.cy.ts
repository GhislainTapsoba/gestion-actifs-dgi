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

describe('AffectationActif e2e test', () => {
  const affectationActifPageUrl = '/affectation-actif';
  let username: string;
  let password: string;
  // const affectationActifSample = {"statut":"CLOTUREE"};

  let affectationActif;
  // let affectation;
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
      url: '/api/affectations',
      body: {"dateAffectation":"2026-09-02","motif":"ding","dateRestitution":"2026-09-02"},
    }).then(({ body }) => {
      affectation = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/actifs',
      body: {"codeInventaire":"affable insolite","designation":"ferme jadis avant-hier","marque":"obtenir dans la mesure où hier","modele":"pin-pon partout","numeroSerie":"sans","codeBarre":"parce que ouin parce que","type":"RESEAU","etat":"EN_MAINTENANCE","localisation":"guère durer","dateAcquisition":"2026-09-01","valeurAcquisition":17275.16},
    }).then(({ body }) => {
      actif = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/affectation-actifs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/affectation-actifs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/affectation-actifs/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/affectations', {
      statusCode: 200,
      body: [affectation],
    });

    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });

  });
   */

  afterEach(() => {
    if (affectationActif) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/affectation-actifs/${affectationActif.id}`,
      }).then(() => {
        affectationActif = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (affectation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/affectations/${affectation.id}`,
      }).then(() => {
        affectation = undefined;
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

  it('AffectationActifs menu should load AffectationActifs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('affectation-actif');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AffectationActif').should('exist');
    cy.location('pathname').should('eq', affectationActifPageUrl);
  });

  describe('AffectationActif page', () => {
    it('should have translated page title', () => {
      cy.visit(affectationActifPageUrl);
      cy.getEntityHeading('AffectationActif').should('not.contain', 'gestionActifsDgiApp.affectationActif.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(affectationActifPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AffectationActif page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${affectationActifPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('AffectationActif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationActifPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/affectation-actifs',
          body: {
            ...affectationActifSample,
            affectation: affectation,
            actif: actif,
          },
        }).then(({ body }) => {
          affectationActif = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/affectation-actifs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/affectation-actifs?page=0&size=20>; rel="last",<http://localhost/api/affectation-actifs?page=0&size=20>; rel="first"',
              },
              body: [affectationActif],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(affectationActifPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(affectationActifPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details AffectationActif page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('affectationActif');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationActifPageUrl);
      });

      it('edit button click should load edit AffectationActif page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AffectationActif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationActifPageUrl);
      });

      it('edit button click should load edit AffectationActif page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AffectationActif');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationActifPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of AffectationActif', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('affectationActif').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', affectationActifPageUrl);

        affectationActif = undefined;
      });
    });
  });

  describe('new AffectationActif page', () => {
    beforeEach(() => {
      cy.visit(affectationActifPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AffectationActif');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of AffectationActif', () => {
      cy.get(`[data-cy="observation"]`).type('dehors mince');
      cy.get(`[data-cy="observation"]`).should('have.value', 'dehors mince');

      cy.get(`[data-cy="statut"]`).select('ACTIVE');

      cy.get(`[data-cy="affectation"]`).select(1);
      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        affectationActif = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', affectationActifPageUrl);
    });
  });
});
