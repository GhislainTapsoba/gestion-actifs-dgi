package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.InterventionAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Intervention;
import com.dgi.gestionactifs.domain.Panne;
import com.dgi.gestionactifs.domain.PlanningMaintenance;
import com.dgi.gestionactifs.domain.enumeration.StatutIntervention;
import com.dgi.gestionactifs.domain.enumeration.TypeIntervention;
import com.dgi.gestionactifs.repository.InterventionRepository;
import com.dgi.gestionactifs.service.dto.InterventionDTO;
import com.dgi.gestionactifs.service.mapper.InterventionMapper;
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
 * Integration tests for the {@link InterventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InterventionResourceIT {

    private static final LocalDate DEFAULT_DATE_DECLARATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DECLARATION = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_DECLARATION = LocalDate.ofEpochDay(-1L);

    private static final TypeIntervention DEFAULT_TYPE_INTERVENTION = TypeIntervention.PREVENTIVE;
    private static final TypeIntervention UPDATED_TYPE_INTERVENTION = TypeIntervention.CORRECTIVE;

    private static final StatutIntervention DEFAULT_STATUT = StatutIntervention.EN_COURS;
    private static final StatutIntervention UPDATED_STATUT = StatutIntervention.CLOTUREE;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private InterventionMapper interventionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    private Intervention insertedIntervention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .dateDeclaration(DEFAULT_DATE_DECLARATION)
            .typeIntervention(DEFAULT_TYPE_INTERVENTION)
            .statut(DEFAULT_STATUT)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Panne panne;
        if (TestUtil.findAll(em, Panne.class).isEmpty()) {
            panne = PanneResourceIT.createEntity(em);
            em.persist(panne);
            em.flush();
        } else {
            panne = TestUtil.findAll(em, Panne.class).get(0);
        }
        intervention.setPanne(panne);
        return intervention;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createUpdatedEntity(EntityManager em) {
        Intervention updatedIntervention = new Intervention()
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .typeIntervention(UPDATED_TYPE_INTERVENTION)
            .statut(UPDATED_STATUT)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Panne panne;
        if (TestUtil.findAll(em, Panne.class).isEmpty()) {
            panne = PanneResourceIT.createUpdatedEntity(em);
            em.persist(panne);
            em.flush();
        } else {
            panne = TestUtil.findAll(em, Panne.class).get(0);
        }
        updatedIntervention.setPanne(panne);
        return updatedIntervention;
    }

    @BeforeEach
    void initTest() {
        intervention = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedIntervention != null) {
            interventionRepository.delete(insertedIntervention);
            insertedIntervention = null;
        }
    }

    @Test
    @Transactional
    void createIntervention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);
        var returnedInterventionDTO = om.readValue(
            restInterventionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interventionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InterventionDTO.class
        );

        // Validate the Intervention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIntervention = interventionMapper.toEntity(returnedInterventionDTO);
        assertInterventionUpdatableFieldsEquals(returnedIntervention, getPersistedIntervention(returnedIntervention));

        insertedIntervention = returnedIntervention;
    }

    @Test
    @Transactional
    void createInterventionWithExistingId() throws Exception {
        // Create the Intervention with an existing ID
        intervention.setId(1L);
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDeclarationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intervention.setDateDeclaration(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeInterventionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intervention.setTypeIntervention(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        intervention.setStatut(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInterventions() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDeclaration").value(hasItem(DEFAULT_DATE_DECLARATION.toString())))
            .andExpect(jsonPath("$.[*].typeIntervention").value(hasItem(DEFAULT_TYPE_INTERVENTION.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getIntervention() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get the intervention
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL_ID, intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(intervention.getId().intValue()))
            .andExpect(jsonPath("$.dateDeclaration").value(DEFAULT_DATE_DECLARATION.toString()))
            .andExpect(jsonPath("$.typeIntervention").value(DEFAULT_TYPE_INTERVENTION.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getInterventionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        Long id = intervention.getId();

        defaultInterventionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInterventionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInterventionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration equals to
        defaultInterventionFiltering(
            "dateDeclaration.equals=" + DEFAULT_DATE_DECLARATION,
            "dateDeclaration.equals=" + UPDATED_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration in
        defaultInterventionFiltering(
            "dateDeclaration.in=" + DEFAULT_DATE_DECLARATION + "," + UPDATED_DATE_DECLARATION,
            "dateDeclaration.in=" + UPDATED_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration is not null
        defaultInterventionFiltering("dateDeclaration.specified=true", "dateDeclaration.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration is greater than or equal to
        defaultInterventionFiltering(
            "dateDeclaration.greaterThanOrEqual=" + DEFAULT_DATE_DECLARATION,
            "dateDeclaration.greaterThanOrEqual=" + UPDATED_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration is less than or equal to
        defaultInterventionFiltering(
            "dateDeclaration.lessThanOrEqual=" + DEFAULT_DATE_DECLARATION,
            "dateDeclaration.lessThanOrEqual=" + SMALLER_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration is less than
        defaultInterventionFiltering(
            "dateDeclaration.lessThan=" + UPDATED_DATE_DECLARATION,
            "dateDeclaration.lessThan=" + DEFAULT_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByDateDeclarationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDeclaration is greater than
        defaultInterventionFiltering(
            "dateDeclaration.greaterThan=" + SMALLER_DATE_DECLARATION,
            "dateDeclaration.greaterThan=" + DEFAULT_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByTypeInterventionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where typeIntervention equals to
        defaultInterventionFiltering(
            "typeIntervention.equals=" + DEFAULT_TYPE_INTERVENTION,
            "typeIntervention.equals=" + UPDATED_TYPE_INTERVENTION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByTypeInterventionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where typeIntervention in
        defaultInterventionFiltering(
            "typeIntervention.in=" + DEFAULT_TYPE_INTERVENTION + "," + UPDATED_TYPE_INTERVENTION,
            "typeIntervention.in=" + UPDATED_TYPE_INTERVENTION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByTypeInterventionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where typeIntervention is not null
        defaultInterventionFiltering("typeIntervention.specified=true", "typeIntervention.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where statut equals to
        defaultInterventionFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllInterventionsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where statut in
        defaultInterventionFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllInterventionsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where statut is not null
        defaultInterventionFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description equals to
        defaultInterventionFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description in
        defaultInterventionFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description is not null
        defaultInterventionFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInterventionsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description contains
        defaultInterventionFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInterventionsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description does not contain
        defaultInterventionFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInterventionsByPanneIsEqualToSomething() throws Exception {
        Panne panne;
        if (TestUtil.findAll(em, Panne.class).isEmpty()) {
            interventionRepository.saveAndFlush(intervention);
            panne = PanneResourceIT.createEntity(em);
        } else {
            panne = TestUtil.findAll(em, Panne.class).get(0);
        }
        em.persist(panne);
        em.flush();
        intervention.setPanne(panne);
        interventionRepository.saveAndFlush(intervention);
        Long panneId = panne.getId();
        // Get all the interventionList where panne equals to panneId
        defaultInterventionShouldBeFound("panneId.equals=" + panneId);

        // Get all the interventionList where panne equals to (panneId + 1)
        defaultInterventionShouldNotBeFound("panneId.equals=" + (panneId + 1));
    }

    @Test
    @Transactional
    void getAllInterventionsByPlanningIsEqualToSomething() throws Exception {
        PlanningMaintenance planning;
        if (TestUtil.findAll(em, PlanningMaintenance.class).isEmpty()) {
            interventionRepository.saveAndFlush(intervention);
            planning = PlanningMaintenanceResourceIT.createEntity();
        } else {
            planning = TestUtil.findAll(em, PlanningMaintenance.class).get(0);
        }
        em.persist(planning);
        em.flush();
        intervention.addPlanning(planning);
        interventionRepository.saveAndFlush(intervention);
        Long planningId = planning.getId();
        // Get all the interventionList where planning equals to planningId
        defaultInterventionShouldBeFound("planningId.equals=" + planningId);

        // Get all the interventionList where planning equals to (planningId + 1)
        defaultInterventionShouldNotBeFound("planningId.equals=" + (planningId + 1));
    }

    private void defaultInterventionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInterventionShouldBeFound(shouldBeFound);
        defaultInterventionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterventionShouldBeFound(String filter) throws Exception {
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDeclaration").value(hasItem(DEFAULT_DATE_DECLARATION.toString())))
            .andExpect(jsonPath("$.[*].typeIntervention").value(hasItem(DEFAULT_TYPE_INTERVENTION.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterventionShouldNotBeFound(String filter) throws Exception {
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIntervention() throws Exception {
        // Get the intervention
        restInterventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntervention() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intervention
        Intervention updatedIntervention = interventionRepository.findById(intervention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIntervention are not directly saved in db
        em.detach(updatedIntervention);
        updatedIntervention
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .typeIntervention(UPDATED_TYPE_INTERVENTION)
            .statut(UPDATED_STATUT)
            .description(UPDATED_DESCRIPTION);
        InterventionDTO interventionDTO = interventionMapper.toDto(updatedIntervention);

        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(interventionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInterventionToMatchAllProperties(updatedIntervention);
    }

    @Test
    @Transactional
    void putNonExistingIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(interventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention.typeIntervention(UPDATED_TYPE_INTERVENTION).statut(UPDATED_STATUT);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInterventionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIntervention, intervention),
            getPersistedIntervention(intervention)
        );
    }

    @Test
    @Transactional
    void fullUpdateInterventionWithPatch() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the intervention using partial update
        Intervention partialUpdatedIntervention = new Intervention();
        partialUpdatedIntervention.setId(intervention.getId());

        partialUpdatedIntervention
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .typeIntervention(UPDATED_TYPE_INTERVENTION)
            .statut(UPDATED_STATUT)
            .description(UPDATED_DESCRIPTION);

        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntervention))
            )
            .andExpect(status().isOk());

        // Validate the Intervention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInterventionUpdatableFieldsEquals(partialUpdatedIntervention, getPersistedIntervention(partialUpdatedIntervention));
    }

    @Test
    @Transactional
    void patchNonExistingIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interventionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(interventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        intervention.setId(longCount.incrementAndGet());

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterventionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(interventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Intervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntervention() throws Exception {
        // Initialize the database
        insertedIntervention = interventionRepository.saveAndFlush(intervention);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the intervention
        restInterventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, intervention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return interventionRepository.count();
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

    protected Intervention getPersistedIntervention(Intervention intervention) {
        return interventionRepository.findById(intervention.getId()).orElseThrow();
    }

    protected void assertPersistedInterventionToMatchAllProperties(Intervention expectedIntervention) {
        assertInterventionAllPropertiesEquals(expectedIntervention, getPersistedIntervention(expectedIntervention));
    }

    protected void assertPersistedInterventionToMatchUpdatableProperties(Intervention expectedIntervention) {
        assertInterventionAllUpdatablePropertiesEquals(expectedIntervention, getPersistedIntervention(expectedIntervention));
    }
}
