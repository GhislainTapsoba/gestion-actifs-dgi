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

describe('Actif e2e test', () => {
  const actifPageUrl = '/actif';
  let username: string;
  let password: string;
  const actifSample = {
    codeInventaire: 'terne comme tant que',
    designation: 'hirsute sitôt que du fait que',
    type: 'IMPRIMANTE',
    etat: 'REFORME',
  };

  let actif;
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
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/categorie-materiels',
      body: { libelle: 'communauté étudiante hôte', description: 'par suite de brusque aux environs de' },
    }).then(({ body }) => {
      categorieMateriel = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/actifs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/actifs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/actifs/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/categorie-materiels', {
      statusCode: 200,
      body: [categorieMateriel],
    });
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

  it('Actifs menu should load Actifs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('actif');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Actif').should('exist');
    cy.location('pathname').should('eq', actifPageUrl);
  });

  describe('Actif page', () => {
    it('should have translated page title', () => {
      cy.visit(actifPageUrl);
      cy.getEntityHeading('Actif').should('not.contain', 'gestionActifsDgiApp.actif.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(actifPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Actif page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${actifPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Actif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', actifPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/actifs',
          body: {
            ...actifSample,
            categorie: categorieMateriel,
          },
        }).then(({ body }) => {
          actif = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/actifs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/actifs?page=0&size=20>; rel="last",<http://localhost/api/actifs?page=0&size=20>; rel="first"',
              },
              body: [actif],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(actifPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Actif page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('actif');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', actifPageUrl);
      });

      it('edit button click should load edit Actif page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Actif');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', actifPageUrl);
      });

      it('edit button click should load edit Actif page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Actif');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', actifPageUrl);
      });

      it('last delete button click should delete instance of Actif', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('actif').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', actifPageUrl);

        actif = undefined;
      });
    });
  });

  describe('new Actif page', () => {
    beforeEach(() => {
      cy.visit(actifPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Actif');
    });

    it('should create an instance of Actif', () => {
      cy.get(`[data-cy="codeInventaire"]`).type('rectorat clac');
      cy.get(`[data-cy="codeInventaire"]`).should('have.value', 'rectorat clac');

      cy.get(`[data-cy="designation"]`).type('réserver');
      cy.get(`[data-cy="designation"]`).should('have.value', 'réserver');

      cy.get(`[data-cy="marque"]`).type('volontiers aïe déjà');
      cy.get(`[data-cy="marque"]`).should('have.value', 'volontiers aïe déjà');

      cy.get(`[data-cy="modele"]`).type('corps enseignant oups vroum');
      cy.get(`[data-cy="modele"]`).should('have.value', 'corps enseignant oups vroum');

      cy.get(`[data-cy="numeroSerie"]`).type('adversaire auparavant partenaire');
      cy.get(`[data-cy="numeroSerie"]`).should('have.value', 'adversaire auparavant partenaire');

      cy.get(`[data-cy="codeBarre"]`).type('opposer');
      cy.get(`[data-cy="codeBarre"]`).should('have.value', 'opposer');

      cy.get(`[data-cy="type"]`).select('PERIPHERIQUE');

      cy.get(`[data-cy="etat"]`).select('EN_MAINTENANCE');

      cy.get(`[data-cy="localisation"]`).type('responsable dérouler');
      cy.get(`[data-cy="localisation"]`).should('have.value', 'responsable dérouler');

      cy.get(`[data-cy="dateAcquisition"]`).type('2026-09-02');
      cy.get(`[data-cy="dateAcquisition"]`).blur();
      cy.get(`[data-cy="dateAcquisition"]`).should('have.value', '2026-09-02');

      cy.get(`[data-cy="valeurAcquisition"]`).type('25587.19');
      cy.get(`[data-cy="valeurAcquisition"]`).should('have.value', '25587.19');

      cy.get(`[data-cy="categorie"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        actif = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', actifPageUrl);
    });
  });
});
