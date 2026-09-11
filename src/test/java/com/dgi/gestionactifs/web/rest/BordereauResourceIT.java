package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.BordereauAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.domain.Bordereau;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.domain.enumeration.StatutBordereau;
import com.dgi.gestionactifs.domain.enumeration.TypeBordereau;
import com.dgi.gestionactifs.repository.BordereauRepository;
import com.dgi.gestionactifs.repository.UserRepository;
import com.dgi.gestionactifs.service.dto.BordereauDTO;
import com.dgi.gestionactifs.service.mapper.BordereauMapper;
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
 * Integration tests for the {@link BordereauResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BordereauResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_EMISSION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_EMISSION = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_EMISSION = LocalDate.ofEpochDay(-1L);

    private static final TypeBordereau DEFAULT_TYPE_BORDEREAU = TypeBordereau.AFFECTATION;
    private static final TypeBordereau UPDATED_TYPE_BORDEREAU = TypeBordereau.TRANSFERT;

    private static final StatutBordereau DEFAULT_STATUT_VALIDATION = StatutBordereau.EN_ATTENTE;
    private static final StatutBordereau UPDATED_STATUT_VALIDATION = StatutBordereau.VALIDE;

    private static final LocalDate DEFAULT_DATE_VALIDATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_VALIDATION = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_VALIDATION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/bordereaus";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BordereauRepository bordereauRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BordereauMapper bordereauMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBordereauMockMvc;

    private Bordereau bordereau;

    private Bordereau insertedBordereau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bordereau createEntity(EntityManager em) {
        Bordereau bordereau = new Bordereau()
            .numero(DEFAULT_NUMERO)
            .dateEmission(DEFAULT_DATE_EMISSION)
            .typeBordereau(DEFAULT_TYPE_BORDEREAU)
            .statutValidation(DEFAULT_STATUT_VALIDATION)
            .dateValidation(DEFAULT_DATE_VALIDATION);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        bordereau.setEmetteur(user);
        return bordereau;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bordereau createUpdatedEntity(EntityManager em) {
        Bordereau updatedBordereau = new Bordereau()
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .typeBordereau(UPDATED_TYPE_BORDEREAU)
            .statutValidation(UPDATED_STATUT_VALIDATION)
            .dateValidation(UPDATED_DATE_VALIDATION);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedBordereau.setEmetteur(user);
        return updatedBordereau;
    }

    @BeforeEach
    void initTest() {
        bordereau = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBordereau != null) {
            bordereauRepository.delete(insertedBordereau);
            insertedBordereau = null;
        }
    }

    @Test
    @Transactional
    void createBordereau() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);
        var returnedBordereauDTO = om.readValue(
            restBordereauMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BordereauDTO.class
        );

        // Validate the Bordereau in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBordereau = bordereauMapper.toEntity(returnedBordereauDTO);
        assertBordereauUpdatableFieldsEquals(returnedBordereau, getPersistedBordereau(returnedBordereau));

        insertedBordereau = returnedBordereau;
    }

    @Test
    @Transactional
    void createBordereauWithExistingId() throws Exception {
        // Create the Bordereau with an existing ID
        bordereau.setId(1L);
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBordereauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bordereau.setNumero(null);

        // Create the Bordereau, which fails.
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        restBordereauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateEmissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bordereau.setDateEmission(null);

        // Create the Bordereau, which fails.
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        restBordereauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeBordereauIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bordereau.setTypeBordereau(null);

        // Create the Bordereau, which fails.
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        restBordereauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutValidationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bordereau.setStatutValidation(null);

        // Create the Bordereau, which fails.
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        restBordereauMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBordereaus() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList
        restBordereauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bordereau.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].typeBordereau").value(hasItem(DEFAULT_TYPE_BORDEREAU.toString())))
            .andExpect(jsonPath("$.[*].statutValidation").value(hasItem(DEFAULT_STATUT_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())));
    }

    @Test
    @Transactional
    void getBordereau() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get the bordereau
        restBordereauMockMvc
            .perform(get(ENTITY_API_URL_ID, bordereau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bordereau.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.dateEmission").value(DEFAULT_DATE_EMISSION.toString()))
            .andExpect(jsonPath("$.typeBordereau").value(DEFAULT_TYPE_BORDEREAU.toString()))
            .andExpect(jsonPath("$.statutValidation").value(DEFAULT_STATUT_VALIDATION.toString()))
            .andExpect(jsonPath("$.dateValidation").value(DEFAULT_DATE_VALIDATION.toString()));
    }

    @Test
    @Transactional
    void getBordereausByIdFiltering() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        Long id = bordereau.getId();

        defaultBordereauFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBordereauFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBordereauFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBordereausByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where numero equals to
        defaultBordereauFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllBordereausByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where numero in
        defaultBordereauFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllBordereausByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where numero is not null
        defaultBordereauFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllBordereausByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where numero contains
        defaultBordereauFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllBordereausByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where numero does not contain
        defaultBordereauFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission equals to
        defaultBordereauFiltering("dateEmission.equals=" + DEFAULT_DATE_EMISSION, "dateEmission.equals=" + UPDATED_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission in
        defaultBordereauFiltering(
            "dateEmission.in=" + DEFAULT_DATE_EMISSION + "," + UPDATED_DATE_EMISSION,
            "dateEmission.in=" + UPDATED_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission is not null
        defaultBordereauFiltering("dateEmission.specified=true", "dateEmission.specified=false");
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission is greater than or equal to
        defaultBordereauFiltering(
            "dateEmission.greaterThanOrEqual=" + DEFAULT_DATE_EMISSION,
            "dateEmission.greaterThanOrEqual=" + UPDATED_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission is less than or equal to
        defaultBordereauFiltering(
            "dateEmission.lessThanOrEqual=" + DEFAULT_DATE_EMISSION,
            "dateEmission.lessThanOrEqual=" + SMALLER_DATE_EMISSION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission is less than
        defaultBordereauFiltering("dateEmission.lessThan=" + UPDATED_DATE_EMISSION, "dateEmission.lessThan=" + DEFAULT_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllBordereausByDateEmissionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateEmission is greater than
        defaultBordereauFiltering("dateEmission.greaterThan=" + SMALLER_DATE_EMISSION, "dateEmission.greaterThan=" + DEFAULT_DATE_EMISSION);
    }

    @Test
    @Transactional
    void getAllBordereausByTypeBordereauIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where typeBordereau equals to
        defaultBordereauFiltering("typeBordereau.equals=" + DEFAULT_TYPE_BORDEREAU, "typeBordereau.equals=" + UPDATED_TYPE_BORDEREAU);
    }

    @Test
    @Transactional
    void getAllBordereausByTypeBordereauIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where typeBordereau in
        defaultBordereauFiltering(
            "typeBordereau.in=" + DEFAULT_TYPE_BORDEREAU + "," + UPDATED_TYPE_BORDEREAU,
            "typeBordereau.in=" + UPDATED_TYPE_BORDEREAU
        );
    }

    @Test
    @Transactional
    void getAllBordereausByTypeBordereauIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where typeBordereau is not null
        defaultBordereauFiltering("typeBordereau.specified=true", "typeBordereau.specified=false");
    }

    @Test
    @Transactional
    void getAllBordereausByStatutValidationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where statutValidation equals to
        defaultBordereauFiltering(
            "statutValidation.equals=" + DEFAULT_STATUT_VALIDATION,
            "statutValidation.equals=" + UPDATED_STATUT_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByStatutValidationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where statutValidation in
        defaultBordereauFiltering(
            "statutValidation.in=" + DEFAULT_STATUT_VALIDATION + "," + UPDATED_STATUT_VALIDATION,
            "statutValidation.in=" + UPDATED_STATUT_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByStatutValidationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where statutValidation is not null
        defaultBordereauFiltering("statutValidation.specified=true", "statutValidation.specified=false");
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation equals to
        defaultBordereauFiltering("dateValidation.equals=" + DEFAULT_DATE_VALIDATION, "dateValidation.equals=" + UPDATED_DATE_VALIDATION);
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation in
        defaultBordereauFiltering(
            "dateValidation.in=" + DEFAULT_DATE_VALIDATION + "," + UPDATED_DATE_VALIDATION,
            "dateValidation.in=" + UPDATED_DATE_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation is not null
        defaultBordereauFiltering("dateValidation.specified=true", "dateValidation.specified=false");
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation is greater than or equal to
        defaultBordereauFiltering(
            "dateValidation.greaterThanOrEqual=" + DEFAULT_DATE_VALIDATION,
            "dateValidation.greaterThanOrEqual=" + UPDATED_DATE_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation is less than or equal to
        defaultBordereauFiltering(
            "dateValidation.lessThanOrEqual=" + DEFAULT_DATE_VALIDATION,
            "dateValidation.lessThanOrEqual=" + SMALLER_DATE_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation is less than
        defaultBordereauFiltering(
            "dateValidation.lessThan=" + UPDATED_DATE_VALIDATION,
            "dateValidation.lessThan=" + DEFAULT_DATE_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByDateValidationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        // Get all the bordereauList where dateValidation is greater than
        defaultBordereauFiltering(
            "dateValidation.greaterThan=" + SMALLER_DATE_VALIDATION,
            "dateValidation.greaterThan=" + DEFAULT_DATE_VALIDATION
        );
    }

    @Test
    @Transactional
    void getAllBordereausByTransfertIsEqualToSomething() throws Exception {
        Transfert transfert;
        if (TestUtil.findAll(em, Transfert.class).isEmpty()) {
            bordereauRepository.saveAndFlush(bordereau);
            transfert = TransfertResourceIT.createEntity(em);
        } else {
            transfert = TestUtil.findAll(em, Transfert.class).get(0);
        }
        em.persist(transfert);
        em.flush();
        bordereau.setTransfert(transfert);
        bordereauRepository.saveAndFlush(bordereau);
        Long transfertId = transfert.getId();
        // Get all the bordereauList where transfert equals to transfertId
        defaultBordereauShouldBeFound("transfertId.equals=" + transfertId);

        // Get all the bordereauList where transfert equals to (transfertId + 1)
        defaultBordereauShouldNotBeFound("transfertId.equals=" + (transfertId + 1));
    }

    @Test
    @Transactional
    void getAllBordereausByAffectationIsEqualToSomething() throws Exception {
        Affectation affectation;
        if (TestUtil.findAll(em, Affectation.class).isEmpty()) {
            bordereauRepository.saveAndFlush(bordereau);
            affectation = AffectationResourceIT.createEntity(em);
        } else {
            affectation = TestUtil.findAll(em, Affectation.class).get(0);
        }
        em.persist(affectation);
        em.flush();
        bordereau.setAffectation(affectation);
        bordereauRepository.saveAndFlush(bordereau);
        Long affectationId = affectation.getId();
        // Get all the bordereauList where affectation equals to affectationId
        defaultBordereauShouldBeFound("affectationId.equals=" + affectationId);

        // Get all the bordereauList where affectation equals to (affectationId + 1)
        defaultBordereauShouldNotBeFound("affectationId.equals=" + (affectationId + 1));
    }

    @Test
    @Transactional
    void getAllBordereausByEmetteurIsEqualToSomething() throws Exception {
        User emetteur;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            bordereauRepository.saveAndFlush(bordereau);
            emetteur = UserResourceIT.createEntity();
        } else {
            emetteur = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(emetteur);
        em.flush();
        bordereau.setEmetteur(emetteur);
        bordereauRepository.saveAndFlush(bordereau);
        Long emetteurId = emetteur.getId();
        // Get all the bordereauList where emetteur equals to emetteurId
        defaultBordereauShouldBeFound("emetteurId.equals=" + emetteurId);

        // Get all the bordereauList where emetteur equals to (emetteurId + 1)
        defaultBordereauShouldNotBeFound("emetteurId.equals=" + (emetteurId + 1));
    }

    private void defaultBordereauFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBordereauShouldBeFound(shouldBeFound);
        defaultBordereauShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBordereauShouldBeFound(String filter) throws Exception {
        restBordereauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bordereau.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].dateEmission").value(hasItem(DEFAULT_DATE_EMISSION.toString())))
            .andExpect(jsonPath("$.[*].typeBordereau").value(hasItem(DEFAULT_TYPE_BORDEREAU.toString())))
            .andExpect(jsonPath("$.[*].statutValidation").value(hasItem(DEFAULT_STATUT_VALIDATION.toString())))
            .andExpect(jsonPath("$.[*].dateValidation").value(hasItem(DEFAULT_DATE_VALIDATION.toString())));

        // Check, that the count call also returns 1
        restBordereauMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBordereauShouldNotBeFound(String filter) throws Exception {
        restBordereauMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBordereauMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBordereau() throws Exception {
        // Get the bordereau
        restBordereauMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBordereau() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bordereau
        Bordereau updatedBordereau = bordereauRepository.findById(bordereau.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBordereau are not directly saved in db
        em.detach(updatedBordereau);
        updatedBordereau
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .typeBordereau(UPDATED_TYPE_BORDEREAU)
            .statutValidation(UPDATED_STATUT_VALIDATION)
            .dateValidation(UPDATED_DATE_VALIDATION);
        BordereauDTO bordereauDTO = bordereauMapper.toDto(updatedBordereau);

        restBordereauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bordereauDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bordereauDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBordereauToMatchAllProperties(updatedBordereau);
    }

    @Test
    @Transactional
    void putNonExistingBordereau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bordereau.setId(longCount.incrementAndGet());

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBordereauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bordereauDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bordereauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBordereau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bordereau.setId(longCount.incrementAndGet());

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBordereauMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bordereauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBordereau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bordereau.setId(longCount.incrementAndGet());

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBordereauMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBordereauWithPatch() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bordereau using partial update
        Bordereau partialUpdatedBordereau = new Bordereau();
        partialUpdatedBordereau.setId(bordereau.getId());

        partialUpdatedBordereau.dateEmission(UPDATED_DATE_EMISSION).typeBordereau(UPDATED_TYPE_BORDEREAU);

        restBordereauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBordereau.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBordereau))
            )
            .andExpect(status().isOk());

        // Validate the Bordereau in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBordereauUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBordereau, bordereau),
            getPersistedBordereau(bordereau)
        );
    }

    @Test
    @Transactional
    void fullUpdateBordereauWithPatch() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bordereau using partial update
        Bordereau partialUpdatedBordereau = new Bordereau();
        partialUpdatedBordereau.setId(bordereau.getId());

        partialUpdatedBordereau
            .numero(UPDATED_NUMERO)
            .dateEmission(UPDATED_DATE_EMISSION)
            .typeBordereau(UPDATED_TYPE_BORDEREAU)
            .statutValidation(UPDATED_STATUT_VALIDATION)
            .dateValidation(UPDATED_DATE_VALIDATION);

        restBordereauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBordereau.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBordereau))
            )
            .andExpect(status().isOk());

        // Validate the Bordereau in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBordereauUpdatableFieldsEquals(partialUpdatedBordereau, getPersistedBordereau(partialUpdatedBordereau));
    }

    @Test
    @Transactional
    void patchNonExistingBordereau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bordereau.setId(longCount.incrementAndGet());

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBordereauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bordereauDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bordereauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBordereau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bordereau.setId(longCount.incrementAndGet());

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBordereauMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bordereauDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBordereau() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bordereau.setId(longCount.incrementAndGet());

        // Create the Bordereau
        BordereauDTO bordereauDTO = bordereauMapper.toDto(bordereau);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBordereauMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bordereauDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bordereau in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBordereau() throws Exception {
        // Initialize the database
        insertedBordereau = bordereauRepository.saveAndFlush(bordereau);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bordereau
        restBordereauMockMvc
            .perform(delete(ENTITY_API_URL_ID, bordereau.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bordereauRepository.count();
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

    protected Bordereau getPersistedBordereau(Bordereau bordereau) {
        return bordereauRepository.findById(bordereau.getId()).orElseThrow();
    }

    protected void assertPersistedBordereauToMatchAllProperties(Bordereau expectedBordereau) {
        assertBordereauAllPropertiesEquals(expectedBordereau, getPersistedBordereau(expectedBordereau));
    }

    protected void assertPersistedBordereauToMatchUpdatableProperties(Bordereau expectedBordereau) {
        assertBordereauAllUpdatablePropertiesEquals(expectedBordereau, getPersistedBordereau(expectedBordereau));
    }
}
