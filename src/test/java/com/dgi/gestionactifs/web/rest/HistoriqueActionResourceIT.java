package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.HistoriqueActionAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static com.dgi.gestionactifs.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.HistoriqueAction;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.domain.enumeration.TypeMouvement;
import com.dgi.gestionactifs.repository.HistoriqueActionRepository;
import com.dgi.gestionactifs.repository.UserRepository;
import com.dgi.gestionactifs.service.dto.HistoriqueActionDTO;
import com.dgi.gestionactifs.service.mapper.HistoriqueActionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link HistoriqueActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueActionResourceIT {

    private static final ZonedDateTime DEFAULT_DATE_ACTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(1788377901095L), ZoneOffset.UTC);
    private static final ZonedDateTime SMALLER_DATE_ACTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final TypeMouvement DEFAULT_TYPE_ACTION = TypeMouvement.CREATION;
    private static final TypeMouvement UPDATED_TYPE_ACTION = TypeMouvement.MODIFICATION;

    private static final String DEFAULT_ENTITE_CIBLEE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITE_CIBLEE = "BBBBBBBBBB";

    private static final String DEFAULT_ANCIENNE_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_ANCIENNE_VALEUR = "BBBBBBBBBB";

    private static final String DEFAULT_NOUVELLE_VALEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOUVELLE_VALEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/historique-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueActionRepository historiqueActionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoriqueActionMapper historiqueActionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueActionMockMvc;

    private HistoriqueAction historiqueAction;

    private HistoriqueAction insertedHistoriqueAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueAction createEntity(EntityManager em) {
        HistoriqueAction historiqueAction = new HistoriqueAction()
            .dateAction(DEFAULT_DATE_ACTION)
            .typeAction(DEFAULT_TYPE_ACTION)
            .entiteCiblee(DEFAULT_ENTITE_CIBLEE)
            .ancienneValeur(DEFAULT_ANCIENNE_VALEUR)
            .nouvelleValeur(DEFAULT_NOUVELLE_VALEUR);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        historiqueAction.setUtilisateur(user);
        return historiqueAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoriqueAction createUpdatedEntity(EntityManager em) {
        HistoriqueAction updatedHistoriqueAction = new HistoriqueAction()
            .dateAction(UPDATED_DATE_ACTION)
            .typeAction(UPDATED_TYPE_ACTION)
            .entiteCiblee(UPDATED_ENTITE_CIBLEE)
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedHistoriqueAction.setUtilisateur(user);
        return updatedHistoriqueAction;
    }

    @BeforeEach
    void initTest() {
        historiqueAction = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoriqueAction != null) {
            historiqueActionRepository.delete(insertedHistoriqueAction);
            insertedHistoriqueAction = null;
        }
    }

    @Test
    @Transactional
    void createHistoriqueAction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);
        var returnedHistoriqueActionDTO = om.readValue(
            restHistoriqueActionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueActionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriqueActionDTO.class
        );

        // Validate the HistoriqueAction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoriqueAction = historiqueActionMapper.toEntity(returnedHistoriqueActionDTO);
        assertHistoriqueActionUpdatableFieldsEquals(returnedHistoriqueAction, getPersistedHistoriqueAction(returnedHistoriqueAction));

        insertedHistoriqueAction = returnedHistoriqueAction;
    }

    @Test
    @Transactional
    void createHistoriqueActionWithExistingId() throws Exception {
        // Create the HistoriqueAction with an existing ID
        historiqueAction.setId(1L);
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueActionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAction.setDateAction(null);

        // Create the HistoriqueAction, which fails.
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        restHistoriqueActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueActionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historiqueAction.setTypeAction(null);

        // Create the HistoriqueAction, which fails.
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        restHistoriqueActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueActionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriqueActions() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList
        restHistoriqueActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(sameInstant(DEFAULT_DATE_ACTION))))
            .andExpect(jsonPath("$.[*].typeAction").value(hasItem(DEFAULT_TYPE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].entiteCiblee").value(hasItem(DEFAULT_ENTITE_CIBLEE)))
            .andExpect(jsonPath("$.[*].ancienneValeur").value(hasItem(DEFAULT_ANCIENNE_VALEUR)))
            .andExpect(jsonPath("$.[*].nouvelleValeur").value(hasItem(DEFAULT_NOUVELLE_VALEUR)));
    }

    @Test
    @Transactional
    void getHistoriqueAction() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get the historiqueAction
        restHistoriqueActionMockMvc
            .perform(get(ENTITY_API_URL_ID, historiqueAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historiqueAction.getId().intValue()))
            .andExpect(jsonPath("$.dateAction").value(sameInstant(DEFAULT_DATE_ACTION)))
            .andExpect(jsonPath("$.typeAction").value(DEFAULT_TYPE_ACTION.toString()))
            .andExpect(jsonPath("$.entiteCiblee").value(DEFAULT_ENTITE_CIBLEE))
            .andExpect(jsonPath("$.ancienneValeur").value(DEFAULT_ANCIENNE_VALEUR))
            .andExpect(jsonPath("$.nouvelleValeur").value(DEFAULT_NOUVELLE_VALEUR));
    }

    @Test
    @Transactional
    void getHistoriqueActionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        Long id = historiqueAction.getId();

        defaultHistoriqueActionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoriqueActionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoriqueActionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction equals to
        defaultHistoriqueActionFiltering("dateAction.equals=" + DEFAULT_DATE_ACTION, "dateAction.equals=" + UPDATED_DATE_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction in
        defaultHistoriqueActionFiltering(
            "dateAction.in=" + DEFAULT_DATE_ACTION + "," + UPDATED_DATE_ACTION,
            "dateAction.in=" + UPDATED_DATE_ACTION
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction is not null
        defaultHistoriqueActionFiltering("dateAction.specified=true", "dateAction.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction is greater than or equal to
        defaultHistoriqueActionFiltering(
            "dateAction.greaterThanOrEqual=" + DEFAULT_DATE_ACTION,
            "dateAction.greaterThanOrEqual=" + UPDATED_DATE_ACTION
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction is less than or equal to
        defaultHistoriqueActionFiltering(
            "dateAction.lessThanOrEqual=" + DEFAULT_DATE_ACTION,
            "dateAction.lessThanOrEqual=" + SMALLER_DATE_ACTION
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction is less than
        defaultHistoriqueActionFiltering("dateAction.lessThan=" + UPDATED_DATE_ACTION, "dateAction.lessThan=" + DEFAULT_DATE_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByDateActionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where dateAction is greater than
        defaultHistoriqueActionFiltering("dateAction.greaterThan=" + SMALLER_DATE_ACTION, "dateAction.greaterThan=" + DEFAULT_DATE_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByTypeActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where typeAction equals to
        defaultHistoriqueActionFiltering("typeAction.equals=" + DEFAULT_TYPE_ACTION, "typeAction.equals=" + UPDATED_TYPE_ACTION);
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByTypeActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where typeAction in
        defaultHistoriqueActionFiltering(
            "typeAction.in=" + DEFAULT_TYPE_ACTION + "," + UPDATED_TYPE_ACTION,
            "typeAction.in=" + UPDATED_TYPE_ACTION
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByTypeActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where typeAction is not null
        defaultHistoriqueActionFiltering("typeAction.specified=true", "typeAction.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByEntiteCibleeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where entiteCiblee equals to
        defaultHistoriqueActionFiltering("entiteCiblee.equals=" + DEFAULT_ENTITE_CIBLEE, "entiteCiblee.equals=" + UPDATED_ENTITE_CIBLEE);
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByEntiteCibleeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where entiteCiblee in
        defaultHistoriqueActionFiltering(
            "entiteCiblee.in=" + DEFAULT_ENTITE_CIBLEE + "," + UPDATED_ENTITE_CIBLEE,
            "entiteCiblee.in=" + UPDATED_ENTITE_CIBLEE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByEntiteCibleeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where entiteCiblee is not null
        defaultHistoriqueActionFiltering("entiteCiblee.specified=true", "entiteCiblee.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByEntiteCibleeContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where entiteCiblee contains
        defaultHistoriqueActionFiltering(
            "entiteCiblee.contains=" + DEFAULT_ENTITE_CIBLEE,
            "entiteCiblee.contains=" + UPDATED_ENTITE_CIBLEE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByEntiteCibleeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where entiteCiblee does not contain
        defaultHistoriqueActionFiltering(
            "entiteCiblee.doesNotContain=" + UPDATED_ENTITE_CIBLEE,
            "entiteCiblee.doesNotContain=" + DEFAULT_ENTITE_CIBLEE
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByAncienneValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where ancienneValeur equals to
        defaultHistoriqueActionFiltering(
            "ancienneValeur.equals=" + DEFAULT_ANCIENNE_VALEUR,
            "ancienneValeur.equals=" + UPDATED_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByAncienneValeurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where ancienneValeur in
        defaultHistoriqueActionFiltering(
            "ancienneValeur.in=" + DEFAULT_ANCIENNE_VALEUR + "," + UPDATED_ANCIENNE_VALEUR,
            "ancienneValeur.in=" + UPDATED_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByAncienneValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where ancienneValeur is not null
        defaultHistoriqueActionFiltering("ancienneValeur.specified=true", "ancienneValeur.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByAncienneValeurContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where ancienneValeur contains
        defaultHistoriqueActionFiltering(
            "ancienneValeur.contains=" + DEFAULT_ANCIENNE_VALEUR,
            "ancienneValeur.contains=" + UPDATED_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByAncienneValeurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where ancienneValeur does not contain
        defaultHistoriqueActionFiltering(
            "ancienneValeur.doesNotContain=" + UPDATED_ANCIENNE_VALEUR,
            "ancienneValeur.doesNotContain=" + DEFAULT_ANCIENNE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByNouvelleValeurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where nouvelleValeur equals to
        defaultHistoriqueActionFiltering(
            "nouvelleValeur.equals=" + DEFAULT_NOUVELLE_VALEUR,
            "nouvelleValeur.equals=" + UPDATED_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByNouvelleValeurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where nouvelleValeur in
        defaultHistoriqueActionFiltering(
            "nouvelleValeur.in=" + DEFAULT_NOUVELLE_VALEUR + "," + UPDATED_NOUVELLE_VALEUR,
            "nouvelleValeur.in=" + UPDATED_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByNouvelleValeurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where nouvelleValeur is not null
        defaultHistoriqueActionFiltering("nouvelleValeur.specified=true", "nouvelleValeur.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByNouvelleValeurContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where nouvelleValeur contains
        defaultHistoriqueActionFiltering(
            "nouvelleValeur.contains=" + DEFAULT_NOUVELLE_VALEUR,
            "nouvelleValeur.contains=" + UPDATED_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByNouvelleValeurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        // Get all the historiqueActionList where nouvelleValeur does not contain
        defaultHistoriqueActionFiltering(
            "nouvelleValeur.doesNotContain=" + UPDATED_NOUVELLE_VALEUR,
            "nouvelleValeur.doesNotContain=" + DEFAULT_NOUVELLE_VALEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriqueActionsByUtilisateurIsEqualToSomething() throws Exception {
        User utilisateur;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            historiqueActionRepository.saveAndFlush(historiqueAction);
            utilisateur = UserResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        historiqueAction.setUtilisateur(utilisateur);
        historiqueActionRepository.saveAndFlush(historiqueAction);
        Long utilisateurId = utilisateur.getId();
        // Get all the historiqueActionList where utilisateur equals to utilisateurId
        defaultHistoriqueActionShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the historiqueActionList where utilisateur equals to (utilisateurId + 1)
        defaultHistoriqueActionShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultHistoriqueActionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoriqueActionShouldBeFound(shouldBeFound);
        defaultHistoriqueActionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueActionShouldBeFound(String filter) throws Exception {
        restHistoriqueActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historiqueAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAction").value(hasItem(sameInstant(DEFAULT_DATE_ACTION))))
            .andExpect(jsonPath("$.[*].typeAction").value(hasItem(DEFAULT_TYPE_ACTION.toString())))
            .andExpect(jsonPath("$.[*].entiteCiblee").value(hasItem(DEFAULT_ENTITE_CIBLEE)))
            .andExpect(jsonPath("$.[*].ancienneValeur").value(hasItem(DEFAULT_ANCIENNE_VALEUR)))
            .andExpect(jsonPath("$.[*].nouvelleValeur").value(hasItem(DEFAULT_NOUVELLE_VALEUR)));

        // Check, that the count call also returns 1
        restHistoriqueActionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueActionShouldNotBeFound(String filter) throws Exception {
        restHistoriqueActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueActionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoriqueAction() throws Exception {
        // Get the historiqueAction
        restHistoriqueActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoriqueAction() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAction
        HistoriqueAction updatedHistoriqueAction = historiqueActionRepository.findById(historiqueAction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoriqueAction are not directly saved in db
        em.detach(updatedHistoriqueAction);
        updatedHistoriqueAction
            .dateAction(UPDATED_DATE_ACTION)
            .typeAction(UPDATED_TYPE_ACTION)
            .entiteCiblee(UPDATED_ENTITE_CIBLEE)
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR);
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(updatedHistoriqueAction);

        restHistoriqueActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueActionDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriqueActionToMatchAllProperties(updatedHistoriqueAction);
    }

    @Test
    @Transactional
    void putNonExistingHistoriqueAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAction.setId(longCount.incrementAndGet());

        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueActionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoriqueAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAction.setId(longCount.incrementAndGet());

        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoriqueAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAction.setId(longCount.incrementAndGet());

        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueActionWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAction using partial update
        HistoriqueAction partialUpdatedHistoriqueAction = new HistoriqueAction();
        partialUpdatedHistoriqueAction.setId(historiqueAction.getId());

        partialUpdatedHistoriqueAction
            .typeAction(UPDATED_TYPE_ACTION)
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR);

        restHistoriqueActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueAction))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueActionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoriqueAction, historiqueAction),
            getPersistedHistoriqueAction(historiqueAction)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueActionWithPatch() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historiqueAction using partial update
        HistoriqueAction partialUpdatedHistoriqueAction = new HistoriqueAction();
        partialUpdatedHistoriqueAction.setId(historiqueAction.getId());

        partialUpdatedHistoriqueAction
            .dateAction(UPDATED_DATE_ACTION)
            .typeAction(UPDATED_TYPE_ACTION)
            .entiteCiblee(UPDATED_ENTITE_CIBLEE)
            .ancienneValeur(UPDATED_ANCIENNE_VALEUR)
            .nouvelleValeur(UPDATED_NOUVELLE_VALEUR);

        restHistoriqueActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoriqueAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoriqueAction))
            )
            .andExpect(status().isOk());

        // Validate the HistoriqueAction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueActionUpdatableFieldsEquals(
            partialUpdatedHistoriqueAction,
            getPersistedHistoriqueAction(partialUpdatedHistoriqueAction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoriqueAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAction.setId(longCount.incrementAndGet());

        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueActionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoriqueAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAction.setId(longCount.incrementAndGet());

        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueActionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoriqueAction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historiqueAction.setId(longCount.incrementAndGet());

        // Create the HistoriqueAction
        HistoriqueActionDTO historiqueActionDTO = historiqueActionMapper.toDto(historiqueAction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueActionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historiqueActionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoriqueAction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoriqueAction() throws Exception {
        // Initialize the database
        insertedHistoriqueAction = historiqueActionRepository.saveAndFlush(historiqueAction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historiqueAction
        restHistoriqueActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, historiqueAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiqueActionRepository.count();
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

    protected HistoriqueAction getPersistedHistoriqueAction(HistoriqueAction historiqueAction) {
        return historiqueActionRepository.findById(historiqueAction.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueActionToMatchAllProperties(HistoriqueAction expectedHistoriqueAction) {
        assertHistoriqueActionAllPropertiesEquals(expectedHistoriqueAction, getPersistedHistoriqueAction(expectedHistoriqueAction));
    }

    protected void assertPersistedHistoriqueActionToMatchUpdatableProperties(HistoriqueAction expectedHistoriqueAction) {
        assertHistoriqueActionAllUpdatablePropertiesEquals(
            expectedHistoriqueAction,
            getPersistedHistoriqueAction(expectedHistoriqueAction)
        );
    }
}
