package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.TransfertAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.domain.enumeration.StatutTransfert;
import com.dgi.gestionactifs.repository.TransfertRepository;
import com.dgi.gestionactifs.repository.UserRepository;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.mapper.TransfertMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link TransfertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransfertResourceIT {

    private static final LocalDate DEFAULT_DATE_TRANSFERT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TRANSFERT = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_TRANSFERT = LocalDate.ofEpochDay(-1L);

    private static final StatutTransfert DEFAULT_STATUT = StatutTransfert.EN_ATTENTE;
    private static final StatutTransfert UPDATED_STATUT = StatutTransfert.VALIDE;

    private static final String DEFAULT_COMMENTAIRE_REJET = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE_REJET = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_TRAITEMENT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TRAITEMENT = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_TRAITEMENT = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/transferts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransfertRepository transfertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransfertMapper transfertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransfertMockMvc;

    private Transfert transfert;

    private Transfert insertedTransfert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfert createEntity(EntityManager em) {
        Transfert transfert = new Transfert()
            .dateTransfert(DEFAULT_DATE_TRANSFERT)
            .statut(DEFAULT_STATUT)
            .commentaireRejet(DEFAULT_COMMENTAIRE_REJET)
            .dateTraitement(DEFAULT_DATE_TRAITEMENT);
        // Add required entity
        ServiceDgi serviceDgi;
        if (TestUtil.findAll(em, ServiceDgi.class).isEmpty()) {
            serviceDgi = ServiceDgiResourceIT.createEntity();
            em.persist(serviceDgi);
            em.flush();
        } else {
            serviceDgi = TestUtil.findAll(em, ServiceDgi.class).get(0);
        }
        transfert.setServiceOrigine(serviceDgi);
        // Add required entity
        transfert.setServiceDestinataire(serviceDgi);
        return transfert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfert createUpdatedEntity(EntityManager em) {
        Transfert updatedTransfert = new Transfert()
            .dateTransfert(UPDATED_DATE_TRANSFERT)
            .statut(UPDATED_STATUT)
            .commentaireRejet(UPDATED_COMMENTAIRE_REJET)
            .dateTraitement(UPDATED_DATE_TRAITEMENT);
        // Add required entity
        ServiceDgi serviceDgi;
        if (TestUtil.findAll(em, ServiceDgi.class).isEmpty()) {
            serviceDgi = ServiceDgiResourceIT.createUpdatedEntity();
            em.persist(serviceDgi);
            em.flush();
        } else {
            serviceDgi = TestUtil.findAll(em, ServiceDgi.class).get(0);
        }
        updatedTransfert.setServiceOrigine(serviceDgi);
        // Add required entity
        updatedTransfert.setServiceDestinataire(serviceDgi);
        return updatedTransfert;
    }

    @BeforeEach
    void initTest() {
        transfert = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTransfert != null) {
            transfertRepository.delete(insertedTransfert);
            insertedTransfert = null;
        }
    }

    @Test
    @Transactional
    void createTransfert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);
        var returnedTransfertDTO = om.readValue(
            restTransfertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransfertDTO.class
        );

        // Validate the Transfert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransfert = transfertMapper.toEntity(returnedTransfertDTO);
        assertTransfertUpdatableFieldsEquals(returnedTransfert, getPersistedTransfert(returnedTransfert));

        insertedTransfert = returnedTransfert;
    }

    @Test
    @Transactional
    void createTransfertWithExistingId() throws Exception {
        // Create the Transfert with an existing ID
        transfert.setId(1L);
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateTransfertIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transfert.setDateTransfert(null);

        // Create the Transfert, which fails.
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        restTransfertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transfert.setStatut(null);

        // Create the Transfert, which fails.
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        restTransfertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransferts() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfert.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTransfert").value(hasItem(DEFAULT_DATE_TRANSFERT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].commentaireRejet").value(hasItem(DEFAULT_COMMENTAIRE_REJET)))
            .andExpect(jsonPath("$.[*].dateTraitement").value(hasItem(DEFAULT_DATE_TRAITEMENT.toString())));
    }

    @Test
    @Transactional
    void getTransfert() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get the transfert
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL_ID, transfert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transfert.getId().intValue()))
            .andExpect(jsonPath("$.dateTransfert").value(DEFAULT_DATE_TRANSFERT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.commentaireRejet").value(DEFAULT_COMMENTAIRE_REJET))
            .andExpect(jsonPath("$.dateTraitement").value(DEFAULT_DATE_TRAITEMENT.toString()));
    }

    @Test
    @Transactional
    void getTransfertsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        Long id = transfert.getId();

        defaultTransfertFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransfertFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransfertFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert equals to
        defaultTransfertFiltering("dateTransfert.equals=" + DEFAULT_DATE_TRANSFERT, "dateTransfert.equals=" + UPDATED_DATE_TRANSFERT);
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert in
        defaultTransfertFiltering(
            "dateTransfert.in=" + DEFAULT_DATE_TRANSFERT + "," + UPDATED_DATE_TRANSFERT,
            "dateTransfert.in=" + UPDATED_DATE_TRANSFERT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert is not null
        defaultTransfertFiltering("dateTransfert.specified=true", "dateTransfert.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert is greater than or equal to
        defaultTransfertFiltering(
            "dateTransfert.greaterThanOrEqual=" + DEFAULT_DATE_TRANSFERT,
            "dateTransfert.greaterThanOrEqual=" + UPDATED_DATE_TRANSFERT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert is less than or equal to
        defaultTransfertFiltering(
            "dateTransfert.lessThanOrEqual=" + DEFAULT_DATE_TRANSFERT,
            "dateTransfert.lessThanOrEqual=" + SMALLER_DATE_TRANSFERT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert is less than
        defaultTransfertFiltering("dateTransfert.lessThan=" + UPDATED_DATE_TRANSFERT, "dateTransfert.lessThan=" + DEFAULT_DATE_TRANSFERT);
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTransfertIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTransfert is greater than
        defaultTransfertFiltering(
            "dateTransfert.greaterThan=" + SMALLER_DATE_TRANSFERT,
            "dateTransfert.greaterThan=" + DEFAULT_DATE_TRANSFERT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where statut equals to
        defaultTransfertFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllTransfertsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where statut in
        defaultTransfertFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllTransfertsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where statut is not null
        defaultTransfertFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfertsByCommentaireRejetIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where commentaireRejet equals to
        defaultTransfertFiltering(
            "commentaireRejet.equals=" + DEFAULT_COMMENTAIRE_REJET,
            "commentaireRejet.equals=" + UPDATED_COMMENTAIRE_REJET
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByCommentaireRejetIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where commentaireRejet in
        defaultTransfertFiltering(
            "commentaireRejet.in=" + DEFAULT_COMMENTAIRE_REJET + "," + UPDATED_COMMENTAIRE_REJET,
            "commentaireRejet.in=" + UPDATED_COMMENTAIRE_REJET
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByCommentaireRejetIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where commentaireRejet is not null
        defaultTransfertFiltering("commentaireRejet.specified=true", "commentaireRejet.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfertsByCommentaireRejetContainsSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where commentaireRejet contains
        defaultTransfertFiltering(
            "commentaireRejet.contains=" + DEFAULT_COMMENTAIRE_REJET,
            "commentaireRejet.contains=" + UPDATED_COMMENTAIRE_REJET
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByCommentaireRejetNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where commentaireRejet does not contain
        defaultTransfertFiltering(
            "commentaireRejet.doesNotContain=" + UPDATED_COMMENTAIRE_REJET,
            "commentaireRejet.doesNotContain=" + DEFAULT_COMMENTAIRE_REJET
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement equals to
        defaultTransfertFiltering("dateTraitement.equals=" + DEFAULT_DATE_TRAITEMENT, "dateTraitement.equals=" + UPDATED_DATE_TRAITEMENT);
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement in
        defaultTransfertFiltering(
            "dateTraitement.in=" + DEFAULT_DATE_TRAITEMENT + "," + UPDATED_DATE_TRAITEMENT,
            "dateTraitement.in=" + UPDATED_DATE_TRAITEMENT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement is not null
        defaultTransfertFiltering("dateTraitement.specified=true", "dateTraitement.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement is greater than or equal to
        defaultTransfertFiltering(
            "dateTraitement.greaterThanOrEqual=" + DEFAULT_DATE_TRAITEMENT,
            "dateTraitement.greaterThanOrEqual=" + UPDATED_DATE_TRAITEMENT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement is less than or equal to
        defaultTransfertFiltering(
            "dateTraitement.lessThanOrEqual=" + DEFAULT_DATE_TRAITEMENT,
            "dateTraitement.lessThanOrEqual=" + SMALLER_DATE_TRAITEMENT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement is less than
        defaultTransfertFiltering(
            "dateTraitement.lessThan=" + UPDATED_DATE_TRAITEMENT,
            "dateTraitement.lessThan=" + DEFAULT_DATE_TRAITEMENT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByDateTraitementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        // Get all the transfertList where dateTraitement is greater than
        defaultTransfertFiltering(
            "dateTraitement.greaterThan=" + SMALLER_DATE_TRAITEMENT,
            "dateTraitement.greaterThan=" + DEFAULT_DATE_TRAITEMENT
        );
    }

    @Test
    @Transactional
    void getAllTransfertsByServiceOrigineIsEqualToSomething() throws Exception {
        ServiceDgi serviceOrigine;
        if (TestUtil.findAll(em, ServiceDgi.class).isEmpty()) {
            transfertRepository.saveAndFlush(transfert);
            serviceOrigine = ServiceDgiResourceIT.createEntity();
        } else {
            serviceOrigine = TestUtil.findAll(em, ServiceDgi.class).get(0);
        }
        em.persist(serviceOrigine);
        em.flush();
        transfert.setServiceOrigine(serviceOrigine);
        transfertRepository.saveAndFlush(transfert);
        Long serviceOrigineId = serviceOrigine.getId();
        // Get all the transfertList where serviceOrigine equals to serviceOrigineId
        defaultTransfertShouldBeFound("serviceOrigineId.equals=" + serviceOrigineId);

        // Get all the transfertList where serviceOrigine equals to (serviceOrigineId + 1)
        defaultTransfertShouldNotBeFound("serviceOrigineId.equals=" + (serviceOrigineId + 1));
    }

    @Test
    @Transactional
    void getAllTransfertsByServiceDestinataireIsEqualToSomething() throws Exception {
        ServiceDgi serviceDestinataire;
        if (TestUtil.findAll(em, ServiceDgi.class).isEmpty()) {
            transfertRepository.saveAndFlush(transfert);
            serviceDestinataire = ServiceDgiResourceIT.createEntity();
        } else {
            serviceDestinataire = TestUtil.findAll(em, ServiceDgi.class).get(0);
        }
        em.persist(serviceDestinataire);
        em.flush();
        transfert.setServiceDestinataire(serviceDestinataire);
        transfertRepository.saveAndFlush(transfert);
        Long serviceDestinataireId = serviceDestinataire.getId();
        // Get all the transfertList where serviceDestinataire equals to serviceDestinataireId
        defaultTransfertShouldBeFound("serviceDestinataireId.equals=" + serviceDestinataireId);

        // Get all the transfertList where serviceDestinataire equals to (serviceDestinataireId + 1)
        defaultTransfertShouldNotBeFound("serviceDestinataireId.equals=" + (serviceDestinataireId + 1));
    }

    @Test
    @Transactional
    void getAllTransfertsByDemandeurIsEqualToSomething() throws Exception {
        User demandeur;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            transfertRepository.saveAndFlush(transfert);
            demandeur = UserResourceIT.createEntity();
        } else {
            demandeur = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(demandeur);
        em.flush();
        transfert.setDemandeur(demandeur);
        transfertRepository.saveAndFlush(transfert);
        Long demandeurId = demandeur.getId();
        // Get all the transfertList where demandeur equals to demandeurId
        defaultTransfertShouldBeFound("demandeurId.equals=" + demandeurId);

        // Get all the transfertList where demandeur equals to (demandeurId + 1)
        defaultTransfertShouldNotBeFound("demandeurId.equals=" + (demandeurId + 1));
    }

    @Test
    @Transactional
    void getAllTransfertsByValidateurIsEqualToSomething() throws Exception {
        User validateur;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            transfertRepository.saveAndFlush(transfert);
            validateur = UserResourceIT.createEntity();
        } else {
            validateur = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(validateur);
        em.flush();
        transfert.setValidateur(validateur);
        transfertRepository.saveAndFlush(transfert);
        Long validateurId = validateur.getId();
        // Get all the transfertList where validateur equals to validateurId
        defaultTransfertShouldBeFound("validateurId.equals=" + validateurId);

        // Get all the transfertList where validateur equals to (validateurId + 1)
        defaultTransfertShouldNotBeFound("validateurId.equals=" + (validateurId + 1));
    }

    private void defaultTransfertFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransfertShouldBeFound(shouldBeFound);
        defaultTransfertShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransfertShouldBeFound(String filter) throws Exception {
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfert.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateTransfert").value(hasItem(DEFAULT_DATE_TRANSFERT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].commentaireRejet").value(hasItem(DEFAULT_COMMENTAIRE_REJET)))
            .andExpect(jsonPath("$.[*].dateTraitement").value(hasItem(DEFAULT_DATE_TRAITEMENT.toString())));

        // Check, that the count call also returns 1
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransfertShouldNotBeFound(String filter) throws Exception {
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransfertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransfert() throws Exception {
        // Get the transfert
        restTransfertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransfert() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transfert
        Transfert updatedTransfert = transfertRepository.findById(transfert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransfert are not directly saved in db
        em.detach(updatedTransfert);
        updatedTransfert
            .dateTransfert(UPDATED_DATE_TRANSFERT)
            .statut(UPDATED_STATUT)
            .commentaireRejet(UPDATED_COMMENTAIRE_REJET)
            .dateTraitement(UPDATED_DATE_TRAITEMENT);
        TransfertDTO transfertDTO = transfertMapper.toDto(updatedTransfert);

        restTransfertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transfertDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransfertToMatchAllProperties(updatedTransfert);
    }

    @Test
    @Transactional
    void putNonExistingTransfert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfert.setId(longCount.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransfert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfert.setId(longCount.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransfert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfert.setId(longCount.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransfertWithPatch() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transfert using partial update
        Transfert partialUpdatedTransfert = new Transfert();
        partialUpdatedTransfert.setId(transfert.getId());

        partialUpdatedTransfert
            .dateTransfert(UPDATED_DATE_TRANSFERT)
            .commentaireRejet(UPDATED_COMMENTAIRE_REJET)
            .dateTraitement(UPDATED_DATE_TRAITEMENT);

        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransfert))
            )
            .andExpect(status().isOk());

        // Validate the Transfert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransfertUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransfert, transfert),
            getPersistedTransfert(transfert)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransfertWithPatch() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transfert using partial update
        Transfert partialUpdatedTransfert = new Transfert();
        partialUpdatedTransfert.setId(transfert.getId());

        partialUpdatedTransfert
            .dateTransfert(UPDATED_DATE_TRANSFERT)
            .statut(UPDATED_STATUT)
            .commentaireRejet(UPDATED_COMMENTAIRE_REJET)
            .dateTraitement(UPDATED_DATE_TRAITEMENT);

        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransfert))
            )
            .andExpect(status().isOk());

        // Validate the Transfert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransfertUpdatableFieldsEquals(partialUpdatedTransfert, getPersistedTransfert(partialUpdatedTransfert));
    }

    @Test
    @Transactional
    void patchNonExistingTransfert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfert.setId(longCount.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transfertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransfert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfert.setId(longCount.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transfertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransfert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfert.setId(longCount.incrementAndGet());

        // Create the Transfert
        TransfertDTO transfertDTO = transfertMapper.toDto(transfert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transfertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transfert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransfert() throws Exception {
        // Initialize the database
        insertedTransfert = transfertRepository.saveAndFlush(transfert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transfert
        restTransfertMockMvc
            .perform(delete(ENTITY_API_URL_ID, transfert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transfertRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Transfert getPersistedTransfert(Transfert transfert) {
        return transfertRepository.findById(transfert.getId()).orElseThrow();
    }

    protected void assertPersistedTransfertToMatchAllProperties(Transfert expectedTransfert) {
        assertTransfertAllPropertiesEquals(expectedTransfert, getPersistedTransfert(expectedTransfert));
    }

    protected void assertPersistedTransfertToMatchUpdatableProperties(Transfert expectedTransfert) {
        assertTransfertAllUpdatablePropertiesEquals(expectedTransfert, getPersistedTransfert(expectedTransfert));
    }
}
