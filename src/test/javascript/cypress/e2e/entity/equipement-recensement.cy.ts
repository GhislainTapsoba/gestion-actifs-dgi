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

describe('EquipementRecensement e2e test', () => {
  const equipementRecensementPageUrl = '/equipement-recensement';
  let username: string;
  let password: string;
  // const equipementRecensementSample = {"etatConstate":"EN_PANNE","dateConstat":"2026-09-10"};

  let equipementRecensement;
  // let recensement;
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
      url: '/api/recensements',
      body: {"dateDebut":"2026-09-10","dateFin":"2026-09-09","statut":"PLANIFIER"},
    }).then(({ body }) => {
      recensement = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/actifs',
      body: {"codeInventaire":"d'avec","designation":"découvrir autour oups","marque":"broum clientèle","modele":"ferme drôlement","numeroSerie":"smack à la merci","codeBarre":"pendant pauvre","type":"PERIPHERIQUE","etat":"PERDU_VOLE","localisation":"aujourd'hui quand dring","dateAcquisition":"2026-09-01","valeurAcquisition":32597.42},
    }).then(({ body }) => {
      actif = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/equipement-recensements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/equipement-recensements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/equipement-recensements/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/recensements', {
      statusCode: 200,
      body: [recensement],
    });

    cy.intercept('GET', '/api/actifs', {
      statusCode: 200,
      body: [actif],
    });

  });
   */

  afterEach(() => {
    if (equipementRecensement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/equipement-recensements/${equipementRecensement.id}`,
      }).then(() => {
        equipementRecensement = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (recensement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recensements/${recensement.id}`,
      }).then(() => {
        recensement = undefined;
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

  it('EquipementRecensements menu should load EquipementRecensements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('equipement-recensement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('EquipementRecensement').should('exist');
    cy.location('pathname').should('eq', equipementRecensementPageUrl);
  });

  describe('EquipementRecensement page', () => {
    it('should have translated page title', () => {
      cy.visit(equipementRecensementPageUrl);
      cy.getEntityHeading('EquipementRecensement').should('not.contain', 'gestionActifsDgiApp.equipementRecensement.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(equipementRecensementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create EquipementRecensement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${equipementRecensementPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('EquipementRecensement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', equipementRecensementPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/equipement-recensements',
          body: {
            ...equipementRecensementSample,
            recensement: recensement,
            actif: actif,
          },
        }).then(({ body }) => {
          equipementRecensement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/equipement-recensements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/equipement-recensements?page=0&size=20>; rel="last",<http://localhost/api/equipement-recensements?page=0&size=20>; rel="first"',
              },
              body: [equipementRecensement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(equipementRecensementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(equipementRecensementPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details EquipementRecensement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('equipementRecensement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', equipementRecensementPageUrl);
      });

      it('edit button click should load edit EquipementRecensement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EquipementRecensement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', equipementRecensementPageUrl);
      });

      it('edit button click should load edit EquipementRecensement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('EquipementRecensement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', equipementRecensementPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of EquipementRecensement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('equipementRecensement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', equipementRecensementPageUrl);

        equipementRecensement = undefined;
      });
    });
  });

  describe('new EquipementRecensement page', () => {
    beforeEach(() => {
      cy.visit(equipementRecensementPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('EquipementRecensement');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of EquipementRecensement', () => {
      cy.get(`[data-cy="etatConstate"]`).select('EN_MAINTENANCE');

      cy.get(`[data-cy="dateConstat"]`).type('2026-09-10');
      cy.get(`[data-cy="dateConstat"]`).blur();
      cy.get(`[data-cy="dateConstat"]`).should('have.value', '2026-09-10');

      cy.get(`[data-cy="emplacementConstate"]`).type('lâche');
      cy.get(`[data-cy="emplacementConstate"]`).should('have.value', 'lâche');

      cy.get(`[data-cy="anomalieConstatee"]`).should('not.be.checked');
      cy.get(`[data-cy="anomalieConstatee"]`).click();
      cy.get(`[data-cy="anomalieConstatee"]`).should('be.checked');

      cy.get(`[data-cy="recensement"]`).select(1);
      cy.get(`[data-cy="actif"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        equipementRecensement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', equipementRecensementPageUrl);
    });
  });
});
