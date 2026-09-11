package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.EquipementRecensementAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.EquipementRecensement;
import com.dgi.gestionactifs.domain.Recensement;
import com.dgi.gestionactifs.domain.enumeration.EtatMateriel;
import com.dgi.gestionactifs.repository.EquipementRecensementRepository;
import com.dgi.gestionactifs.service.dto.EquipementRecensementDTO;
import com.dgi.gestionactifs.service.mapper.EquipementRecensementMapper;
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
 * Integration tests for the {@link EquipementRecensementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EquipementRecensementResourceIT {

    private static final EtatMateriel DEFAULT_ETAT_CONSTATE = EtatMateriel.NEUF;
    private static final EtatMateriel UPDATED_ETAT_CONSTATE = EtatMateriel.EN_SERVICE;

    private static final LocalDate DEFAULT_DATE_CONSTAT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CONSTAT = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_CONSTAT = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_EMPLACEMENT_CONSTATE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLACEMENT_CONSTATE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ANOMALIE_CONSTATEE = false;
    private static final Boolean UPDATED_ANOMALIE_CONSTATEE = true;

    private static final String ENTITY_API_URL = "/api/equipement-recensements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EquipementRecensementRepository equipementRecensementRepository;

    @Autowired
    private EquipementRecensementMapper equipementRecensementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEquipementRecensementMockMvc;

    private EquipementRecensement equipementRecensement;

    private EquipementRecensement insertedEquipementRecensement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipementRecensement createEntity(EntityManager em) {
        EquipementRecensement equipementRecensement = new EquipementRecensement()
            .etatConstate(DEFAULT_ETAT_CONSTATE)
            .dateConstat(DEFAULT_DATE_CONSTAT)
            .emplacementConstate(DEFAULT_EMPLACEMENT_CONSTATE)
            .anomalieConstatee(DEFAULT_ANOMALIE_CONSTATEE);
        // Add required entity
        Recensement recensement;
        if (TestUtil.findAll(em, Recensement.class).isEmpty()) {
            recensement = RecensementResourceIT.createEntity();
            em.persist(recensement);
            em.flush();
        } else {
            recensement = TestUtil.findAll(em, Recensement.class).get(0);
        }
        equipementRecensement.setRecensement(recensement);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        equipementRecensement.setActif(actif);
        return equipementRecensement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EquipementRecensement createUpdatedEntity(EntityManager em) {
        EquipementRecensement updatedEquipementRecensement = new EquipementRecensement()
            .etatConstate(UPDATED_ETAT_CONSTATE)
            .dateConstat(UPDATED_DATE_CONSTAT)
            .emplacementConstate(UPDATED_EMPLACEMENT_CONSTATE)
            .anomalieConstatee(UPDATED_ANOMALIE_CONSTATEE);
        // Add required entity
        Recensement recensement;
        if (TestUtil.findAll(em, Recensement.class).isEmpty()) {
            recensement = RecensementResourceIT.createUpdatedEntity();
            em.persist(recensement);
            em.flush();
        } else {
            recensement = TestUtil.findAll(em, Recensement.class).get(0);
        }
        updatedEquipementRecensement.setRecensement(recensement);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createUpdatedEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        updatedEquipementRecensement.setActif(actif);
        return updatedEquipementRecensement;
    }

    @BeforeEach
    void initTest() {
        equipementRecensement = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEquipementRecensement != null) {
            equipementRecensementRepository.delete(insertedEquipementRecensement);
            insertedEquipementRecensement = null;
        }
    }

    @Test
    @Transactional
    void createEquipementRecensement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);
        var returnedEquipementRecensementDTO = om.readValue(
            restEquipementRecensementMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipementRecensementDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EquipementRecensementDTO.class
        );

        // Validate the EquipementRecensement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEquipementRecensement = equipementRecensementMapper.toEntity(returnedEquipementRecensementDTO);
        assertEquipementRecensementUpdatableFieldsEquals(
            returnedEquipementRecensement,
            getPersistedEquipementRecensement(returnedEquipementRecensement)
        );

        insertedEquipementRecensement = returnedEquipementRecensement;
    }

    @Test
    @Transactional
    void createEquipementRecensementWithExistingId() throws Exception {
        // Create the EquipementRecensement with an existing ID
        equipementRecensement.setId(1L);
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEquipementRecensementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipementRecensementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEtatConstateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equipementRecensement.setEtatConstate(null);

        // Create the EquipementRecensement, which fails.
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        restEquipementRecensementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipementRecensementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateConstatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        equipementRecensement.setDateConstat(null);

        // Create the EquipementRecensement, which fails.
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        restEquipementRecensementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipementRecensementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEquipementRecensements() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList
        restEquipementRecensementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipementRecensement.getId().intValue())))
            .andExpect(jsonPath("$.[*].etatConstate").value(hasItem(DEFAULT_ETAT_CONSTATE.toString())))
            .andExpect(jsonPath("$.[*].dateConstat").value(hasItem(DEFAULT_DATE_CONSTAT.toString())))
            .andExpect(jsonPath("$.[*].emplacementConstate").value(hasItem(DEFAULT_EMPLACEMENT_CONSTATE)))
            .andExpect(jsonPath("$.[*].anomalieConstatee").value(hasItem(DEFAULT_ANOMALIE_CONSTATEE)));
    }

    @Test
    @Transactional
    void getEquipementRecensement() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get the equipementRecensement
        restEquipementRecensementMockMvc
            .perform(get(ENTITY_API_URL_ID, equipementRecensement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(equipementRecensement.getId().intValue()))
            .andExpect(jsonPath("$.etatConstate").value(DEFAULT_ETAT_CONSTATE.toString()))
            .andExpect(jsonPath("$.dateConstat").value(DEFAULT_DATE_CONSTAT.toString()))
            .andExpect(jsonPath("$.emplacementConstate").value(DEFAULT_EMPLACEMENT_CONSTATE))
            .andExpect(jsonPath("$.anomalieConstatee").value(DEFAULT_ANOMALIE_CONSTATEE));
    }

    @Test
    @Transactional
    void getEquipementRecensementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        Long id = equipementRecensement.getId();

        defaultEquipementRecensementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEquipementRecensementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEquipementRecensementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEtatConstateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where etatConstate equals to
        defaultEquipementRecensementFiltering(
            "etatConstate.equals=" + DEFAULT_ETAT_CONSTATE,
            "etatConstate.equals=" + UPDATED_ETAT_CONSTATE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEtatConstateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where etatConstate in
        defaultEquipementRecensementFiltering(
            "etatConstate.in=" + DEFAULT_ETAT_CONSTATE + "," + UPDATED_ETAT_CONSTATE,
            "etatConstate.in=" + UPDATED_ETAT_CONSTATE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEtatConstateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where etatConstate is not null
        defaultEquipementRecensementFiltering("etatConstate.specified=true", "etatConstate.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat equals to
        defaultEquipementRecensementFiltering("dateConstat.equals=" + DEFAULT_DATE_CONSTAT, "dateConstat.equals=" + UPDATED_DATE_CONSTAT);
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat in
        defaultEquipementRecensementFiltering(
            "dateConstat.in=" + DEFAULT_DATE_CONSTAT + "," + UPDATED_DATE_CONSTAT,
            "dateConstat.in=" + UPDATED_DATE_CONSTAT
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat is not null
        defaultEquipementRecensementFiltering("dateConstat.specified=true", "dateConstat.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat is greater than or equal to
        defaultEquipementRecensementFiltering(
            "dateConstat.greaterThanOrEqual=" + DEFAULT_DATE_CONSTAT,
            "dateConstat.greaterThanOrEqual=" + UPDATED_DATE_CONSTAT
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat is less than or equal to
        defaultEquipementRecensementFiltering(
            "dateConstat.lessThanOrEqual=" + DEFAULT_DATE_CONSTAT,
            "dateConstat.lessThanOrEqual=" + SMALLER_DATE_CONSTAT
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat is less than
        defaultEquipementRecensementFiltering(
            "dateConstat.lessThan=" + UPDATED_DATE_CONSTAT,
            "dateConstat.lessThan=" + DEFAULT_DATE_CONSTAT
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByDateConstatIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where dateConstat is greater than
        defaultEquipementRecensementFiltering(
            "dateConstat.greaterThan=" + SMALLER_DATE_CONSTAT,
            "dateConstat.greaterThan=" + DEFAULT_DATE_CONSTAT
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEmplacementConstateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where emplacementConstate equals to
        defaultEquipementRecensementFiltering(
            "emplacementConstate.equals=" + DEFAULT_EMPLACEMENT_CONSTATE,
            "emplacementConstate.equals=" + UPDATED_EMPLACEMENT_CONSTATE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEmplacementConstateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where emplacementConstate in
        defaultEquipementRecensementFiltering(
            "emplacementConstate.in=" + DEFAULT_EMPLACEMENT_CONSTATE + "," + UPDATED_EMPLACEMENT_CONSTATE,
            "emplacementConstate.in=" + UPDATED_EMPLACEMENT_CONSTATE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEmplacementConstateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where emplacementConstate is not null
        defaultEquipementRecensementFiltering("emplacementConstate.specified=true", "emplacementConstate.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEmplacementConstateContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where emplacementConstate contains
        defaultEquipementRecensementFiltering(
            "emplacementConstate.contains=" + DEFAULT_EMPLACEMENT_CONSTATE,
            "emplacementConstate.contains=" + UPDATED_EMPLACEMENT_CONSTATE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByEmplacementConstateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where emplacementConstate does not contain
        defaultEquipementRecensementFiltering(
            "emplacementConstate.doesNotContain=" + UPDATED_EMPLACEMENT_CONSTATE,
            "emplacementConstate.doesNotContain=" + DEFAULT_EMPLACEMENT_CONSTATE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByAnomalieConstateeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where anomalieConstatee equals to
        defaultEquipementRecensementFiltering(
            "anomalieConstatee.equals=" + DEFAULT_ANOMALIE_CONSTATEE,
            "anomalieConstatee.equals=" + UPDATED_ANOMALIE_CONSTATEE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByAnomalieConstateeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where anomalieConstatee in
        defaultEquipementRecensementFiltering(
            "anomalieConstatee.in=" + DEFAULT_ANOMALIE_CONSTATEE + "," + UPDATED_ANOMALIE_CONSTATEE,
            "anomalieConstatee.in=" + UPDATED_ANOMALIE_CONSTATEE
        );
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByAnomalieConstateeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        // Get all the equipementRecensementList where anomalieConstatee is not null
        defaultEquipementRecensementFiltering("anomalieConstatee.specified=true", "anomalieConstatee.specified=false");
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByRecensementIsEqualToSomething() throws Exception {
        Recensement recensement;
        if (TestUtil.findAll(em, Recensement.class).isEmpty()) {
            equipementRecensementRepository.saveAndFlush(equipementRecensement);
            recensement = RecensementResourceIT.createEntity();
        } else {
            recensement = TestUtil.findAll(em, Recensement.class).get(0);
        }
        em.persist(recensement);
        em.flush();
        equipementRecensement.setRecensement(recensement);
        equipementRecensementRepository.saveAndFlush(equipementRecensement);
        Long recensementId = recensement.getId();
        // Get all the equipementRecensementList where recensement equals to recensementId
        defaultEquipementRecensementShouldBeFound("recensementId.equals=" + recensementId);

        // Get all the equipementRecensementList where recensement equals to (recensementId + 1)
        defaultEquipementRecensementShouldNotBeFound("recensementId.equals=" + (recensementId + 1));
    }

    @Test
    @Transactional
    void getAllEquipementRecensementsByActifIsEqualToSomething() throws Exception {
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            equipementRecensementRepository.saveAndFlush(equipementRecensement);
            actif = ActifResourceIT.createEntity(em);
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        em.persist(actif);
        em.flush();
        equipementRecensement.setActif(actif);
        equipementRecensementRepository.saveAndFlush(equipementRecensement);
        Long actifId = actif.getId();
        // Get all the equipementRecensementList where actif equals to actifId
        defaultEquipementRecensementShouldBeFound("actifId.equals=" + actifId);

        // Get all the equipementRecensementList where actif equals to (actifId + 1)
        defaultEquipementRecensementShouldNotBeFound("actifId.equals=" + (actifId + 1));
    }

    private void defaultEquipementRecensementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEquipementRecensementShouldBeFound(shouldBeFound);
        defaultEquipementRecensementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEquipementRecensementShouldBeFound(String filter) throws Exception {
        restEquipementRecensementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(equipementRecensement.getId().intValue())))
            .andExpect(jsonPath("$.[*].etatConstate").value(hasItem(DEFAULT_ETAT_CONSTATE.toString())))
            .andExpect(jsonPath("$.[*].dateConstat").value(hasItem(DEFAULT_DATE_CONSTAT.toString())))
            .andExpect(jsonPath("$.[*].emplacementConstate").value(hasItem(DEFAULT_EMPLACEMENT_CONSTATE)))
            .andExpect(jsonPath("$.[*].anomalieConstatee").value(hasItem(DEFAULT_ANOMALIE_CONSTATEE)));

        // Check, that the count call also returns 1
        restEquipementRecensementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEquipementRecensementShouldNotBeFound(String filter) throws Exception {
        restEquipementRecensementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEquipementRecensementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEquipementRecensement() throws Exception {
        // Get the equipementRecensement
        restEquipementRecensementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEquipementRecensement() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equipementRecensement
        EquipementRecensement updatedEquipementRecensement = equipementRecensementRepository
            .findById(equipementRecensement.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEquipementRecensement are not directly saved in db
        em.detach(updatedEquipementRecensement);
        updatedEquipementRecensement
            .etatConstate(UPDATED_ETAT_CONSTATE)
            .dateConstat(UPDATED_DATE_CONSTAT)
            .emplacementConstate(UPDATED_EMPLACEMENT_CONSTATE)
            .anomalieConstatee(UPDATED_ANOMALIE_CONSTATEE);
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(updatedEquipementRecensement);

        restEquipementRecensementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementRecensementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equipementRecensementDTO))
            )
            .andExpect(status().isOk());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEquipementRecensementToMatchAllProperties(updatedEquipementRecensement);
    }

    @Test
    @Transactional
    void putNonExistingEquipementRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipementRecensement.setId(longCount.incrementAndGet());

        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementRecensementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, equipementRecensementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equipementRecensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEquipementRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipementRecensement.setId(longCount.incrementAndGet());

        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementRecensementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(equipementRecensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEquipementRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipementRecensement.setId(longCount.incrementAndGet());

        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementRecensementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(equipementRecensementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEquipementRecensementWithPatch() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equipementRecensement using partial update
        EquipementRecensement partialUpdatedEquipementRecensement = new EquipementRecensement();
        partialUpdatedEquipementRecensement.setId(equipementRecensement.getId());

        partialUpdatedEquipementRecensement.etatConstate(UPDATED_ETAT_CONSTATE).anomalieConstatee(UPDATED_ANOMALIE_CONSTATEE);

        restEquipementRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipementRecensement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEquipementRecensement))
            )
            .andExpect(status().isOk());

        // Validate the EquipementRecensement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEquipementRecensementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEquipementRecensement, equipementRecensement),
            getPersistedEquipementRecensement(equipementRecensement)
        );
    }

    @Test
    @Transactional
    void fullUpdateEquipementRecensementWithPatch() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the equipementRecensement using partial update
        EquipementRecensement partialUpdatedEquipementRecensement = new EquipementRecensement();
        partialUpdatedEquipementRecensement.setId(equipementRecensement.getId());

        partialUpdatedEquipementRecensement
            .etatConstate(UPDATED_ETAT_CONSTATE)
            .dateConstat(UPDATED_DATE_CONSTAT)
            .emplacementConstate(UPDATED_EMPLACEMENT_CONSTATE)
            .anomalieConstatee(UPDATED_ANOMALIE_CONSTATEE);

        restEquipementRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEquipementRecensement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEquipementRecensement))
            )
            .andExpect(status().isOk());

        // Validate the EquipementRecensement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEquipementRecensementUpdatableFieldsEquals(
            partialUpdatedEquipementRecensement,
            getPersistedEquipementRecensement(partialUpdatedEquipementRecensement)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEquipementRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipementRecensement.setId(longCount.incrementAndGet());

        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEquipementRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, equipementRecensementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(equipementRecensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEquipementRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipementRecensement.setId(longCount.incrementAndGet());

        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(equipementRecensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEquipementRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        equipementRecensement.setId(longCount.incrementAndGet());

        // Create the EquipementRecensement
        EquipementRecensementDTO equipementRecensementDTO = equipementRecensementMapper.toDto(equipementRecensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEquipementRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(equipementRecensementDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EquipementRecensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEquipementRecensement() throws Exception {
        // Initialize the database
        insertedEquipementRecensement = equipementRecensementRepository.saveAndFlush(equipementRecensement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the equipementRecensement
        restEquipementRecensementMockMvc
            .perform(delete(ENTITY_API_URL_ID, equipementRecensement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return equipementRecensementRepository.count();
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

    protected EquipementRecensement getPersistedEquipementRecensement(EquipementRecensement equipementRecensement) {
        return equipementRecensementRepository.findById(equipementRecensement.getId()).orElseThrow();
    }

    protected void assertPersistedEquipementRecensementToMatchAllProperties(EquipementRecensement expectedEquipementRecensement) {
        assertEquipementRecensementAllPropertiesEquals(
            expectedEquipementRecensement,
            getPersistedEquipementRecensement(expectedEquipementRecensement)
        );
    }

    protected void assertPersistedEquipementRecensementToMatchUpdatableProperties(EquipementRecensement expectedEquipementRecensement) {
        assertEquipementRecensementAllUpdatablePropertiesEquals(
            expectedEquipementRecensement,
            getPersistedEquipementRecensement(expectedEquipementRecensement)
        );
    }
}
