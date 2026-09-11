package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.PanneAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Panne;
import com.dgi.gestionactifs.domain.enumeration.StatutPanne;
import com.dgi.gestionactifs.repository.PanneRepository;
import com.dgi.gestionactifs.service.dto.PanneDTO;
import com.dgi.gestionactifs.service.mapper.PanneMapper;
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
 * Integration tests for the {@link PanneResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PanneResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DECLARATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DECLARATION = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_DECLARATION = LocalDate.ofEpochDay(-1L);

    private static final StatutPanne DEFAULT_STATUT_PANNE = StatutPanne.SIGNALEE;
    private static final StatutPanne UPDATED_STATUT_PANNE = StatutPanne.EN_COURS;

    private static final String ENTITY_API_URL = "/api/pannes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PanneRepository panneRepository;

    @Autowired
    private PanneMapper panneMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPanneMockMvc;

    private Panne panne;

    private Panne insertedPanne;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panne createEntity(EntityManager em) {
        Panne panne = new Panne()
            .description(DEFAULT_DESCRIPTION)
            .dateDeclaration(DEFAULT_DATE_DECLARATION)
            .statutPanne(DEFAULT_STATUT_PANNE);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        panne.setActif(actif);
        return panne;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Panne createUpdatedEntity(EntityManager em) {
        Panne updatedPanne = new Panne()
            .description(UPDATED_DESCRIPTION)
            .dateDeclaration(UPDATED_DATE_DECLARATION)
            .statutPanne(UPDATED_STATUT_PANNE);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createUpdatedEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        updatedPanne.setActif(actif);
        return updatedPanne;
    }

    @BeforeEach
    void initTest() {
        panne = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPanne != null) {
            panneRepository.delete(insertedPanne);
            insertedPanne = null;
        }
    }

    @Test
    @Transactional
    void createPanne() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);
        var returnedPanneDTO = om.readValue(
            restPanneMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PanneDTO.class
        );

        // Validate the Panne in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPanne = panneMapper.toEntity(returnedPanneDTO);
        assertPanneUpdatableFieldsEquals(returnedPanne, getPersistedPanne(returnedPanne));

        insertedPanne = returnedPanne;
    }

    @Test
    @Transactional
    void createPanneWithExistingId() throws Exception {
        // Create the Panne with an existing ID
        panne.setId(1L);
        PanneDTO panneDTO = panneMapper.toDto(panne);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        panne.setDescription(null);

        // Create the Panne, which fails.
        PanneDTO panneDTO = panneMapper.toDto(panne);

        restPanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateDeclarationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        panne.setDateDeclaration(null);

        // Create the Panne, which fails.
        PanneDTO panneDTO = panneMapper.toDto(panne);

        restPanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutPanneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        panne.setStatutPanne(null);

        // Create the Panne, which fails.
        PanneDTO panneDTO = panneMapper.toDto(panne);

        restPanneMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPannes() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList
        restPanneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(panne.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDeclaration").value(hasItem(DEFAULT_DATE_DECLARATION.toString())))
            .andExpect(jsonPath("$.[*].statutPanne").value(hasItem(DEFAULT_STATUT_PANNE.toString())));
    }

    @Test
    @Transactional
    void getPanne() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get the panne
        restPanneMockMvc
            .perform(get(ENTITY_API_URL_ID, panne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(panne.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateDeclaration").value(DEFAULT_DATE_DECLARATION.toString()))
            .andExpect(jsonPath("$.statutPanne").value(DEFAULT_STATUT_PANNE.toString()));
    }

    @Test
    @Transactional
    void getPannesByIdFiltering() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        Long id = panne.getId();

        defaultPanneFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPanneFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPanneFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPannesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where description equals to
        defaultPanneFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPannesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where description in
        defaultPanneFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPannesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where description is not null
        defaultPanneFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllPannesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where description contains
        defaultPanneFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPannesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where description does not contain
        defaultPanneFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration equals to
        defaultPanneFiltering("dateDeclaration.equals=" + DEFAULT_DATE_DECLARATION, "dateDeclaration.equals=" + UPDATED_DATE_DECLARATION);
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration in
        defaultPanneFiltering(
            "dateDeclaration.in=" + DEFAULT_DATE_DECLARATION + "," + UPDATED_DATE_DECLARATION,
            "dateDeclaration.in=" + UPDATED_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration is not null
        defaultPanneFiltering("dateDeclaration.specified=true", "dateDeclaration.specified=false");
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration is greater than or equal to
        defaultPanneFiltering(
            "dateDeclaration.greaterThanOrEqual=" + DEFAULT_DATE_DECLARATION,
            "dateDeclaration.greaterThanOrEqual=" + UPDATED_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration is less than or equal to
        defaultPanneFiltering(
            "dateDeclaration.lessThanOrEqual=" + DEFAULT_DATE_DECLARATION,
            "dateDeclaration.lessThanOrEqual=" + SMALLER_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration is less than
        defaultPanneFiltering(
            "dateDeclaration.lessThan=" + UPDATED_DATE_DECLARATION,
            "dateDeclaration.lessThan=" + DEFAULT_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllPannesByDateDeclarationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where dateDeclaration is greater than
        defaultPanneFiltering(
            "dateDeclaration.greaterThan=" + SMALLER_DATE_DECLARATION,
            "dateDeclaration.greaterThan=" + DEFAULT_DATE_DECLARATION
        );
    }

    @Test
    @Transactional
    void getAllPannesByStatutPanneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where statutPanne equals to
        defaultPanneFiltering("statutPanne.equals=" + DEFAULT_STATUT_PANNE, "statutPanne.equals=" + UPDATED_STATUT_PANNE);
    }

    @Test
    @Transactional
    void getAllPannesByStatutPanneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where statutPanne in
        defaultPanneFiltering(
            "statutPanne.in=" + DEFAULT_STATUT_PANNE + "," + UPDATED_STATUT_PANNE,
            "statutPanne.in=" + UPDATED_STATUT_PANNE
        );
    }

    @Test
    @Transactional
    void getAllPannesByStatutPanneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        // Get all the panneList where statutPanne is not null
        defaultPanneFiltering("statutPanne.specified=true", "statutPanne.specified=false");
    }

    @Test
    @Transactional
    void getAllPannesByActifIsEqualToSomething() throws Exception {
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            panneRepository.saveAndFlush(panne);
            actif = ActifResourceIT.createEntity(em);
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        em.persist(actif);
        em.flush();
        panne.setActif(actif);
        panneRepository.saveAndFlush(panne);
        Long actifId = actif.getId();
        // Get all the panneList where actif equals to actifId
        defaultPanneShouldBeFound("actifId.equals=" + actifId);

        // Get all the panneList where actif equals to (actifId + 1)
        defaultPanneShouldNotBeFound("actifId.equals=" + (actifId + 1));
    }

    private void defaultPanneFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPanneShouldBeFound(shouldBeFound);
        defaultPanneShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPanneShouldBeFound(String filter) throws Exception {
        restPanneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(panne.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateDeclaration").value(hasItem(DEFAULT_DATE_DECLARATION.toString())))
            .andExpect(jsonPath("$.[*].statutPanne").value(hasItem(DEFAULT_STATUT_PANNE.toString())));

        // Check, that the count call also returns 1
        restPanneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPanneShouldNotBeFound(String filter) throws Exception {
        restPanneMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPanneMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPanne() throws Exception {
        // Get the panne
        restPanneMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPanne() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the panne
        Panne updatedPanne = panneRepository.findById(panne.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPanne are not directly saved in db
        em.detach(updatedPanne);
        updatedPanne.description(UPDATED_DESCRIPTION).dateDeclaration(UPDATED_DATE_DECLARATION).statutPanne(UPDATED_STATUT_PANNE);
        PanneDTO panneDTO = panneMapper.toDto(updatedPanne);

        restPanneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, panneDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO))
            )
            .andExpect(status().isOk());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPanneToMatchAllProperties(updatedPanne);
    }

    @Test
    @Transactional
    void putNonExistingPanne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        panne.setId(longCount.incrementAndGet());

        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPanneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, panneDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPanne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        panne.setId(longCount.incrementAndGet());

        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanneMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(panneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPanne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        panne.setId(longCount.incrementAndGet());

        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanneMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(panneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePanneWithPatch() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the panne using partial update
        Panne partialUpdatedPanne = new Panne();
        partialUpdatedPanne.setId(panne.getId());

        partialUpdatedPanne.dateDeclaration(UPDATED_DATE_DECLARATION);

        restPanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPanne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPanne))
            )
            .andExpect(status().isOk());

        // Validate the Panne in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPanneUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPanne, panne), getPersistedPanne(panne));
    }

    @Test
    @Transactional
    void fullUpdatePanneWithPatch() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the panne using partial update
        Panne partialUpdatedPanne = new Panne();
        partialUpdatedPanne.setId(panne.getId());

        partialUpdatedPanne.description(UPDATED_DESCRIPTION).dateDeclaration(UPDATED_DATE_DECLARATION).statutPanne(UPDATED_STATUT_PANNE);

        restPanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPanne.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPanne))
            )
            .andExpect(status().isOk());

        // Validate the Panne in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPanneUpdatableFieldsEquals(partialUpdatedPanne, getPersistedPanne(partialUpdatedPanne));
    }

    @Test
    @Transactional
    void patchNonExistingPanne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        panne.setId(longCount.incrementAndGet());

        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, panneDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(panneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPanne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        panne.setId(longCount.incrementAndGet());

        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanneMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(panneDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPanne() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        panne.setId(longCount.incrementAndGet());

        // Create the Panne
        PanneDTO panneDTO = panneMapper.toDto(panne);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPanneMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(panneDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Panne in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePanne() throws Exception {
        // Initialize the database
        insertedPanne = panneRepository.saveAndFlush(panne);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the panne
        restPanneMockMvc
            .perform(delete(ENTITY_API_URL_ID, panne.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return panneRepository.count();
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

    protected Panne getPersistedPanne(Panne panne) {
        return panneRepository.findById(panne.getId()).orElseThrow();
    }

    protected void assertPersistedPanneToMatchAllProperties(Panne expectedPanne) {
        assertPanneAllPropertiesEquals(expectedPanne, getPersistedPanne(expectedPanne));
    }

    protected void assertPersistedPanneToMatchUpdatableProperties(Panne expectedPanne) {
        assertPanneAllUpdatablePropertiesEquals(expectedPanne, getPersistedPanne(expectedPanne));
    }
}
