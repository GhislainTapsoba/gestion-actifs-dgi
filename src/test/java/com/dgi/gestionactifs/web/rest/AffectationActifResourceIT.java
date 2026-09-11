package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.AffectationActifAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.domain.AffectationActif;
import com.dgi.gestionactifs.domain.enumeration.StatutAffectation;
import com.dgi.gestionactifs.repository.AffectationActifRepository;
import com.dgi.gestionactifs.service.dto.AffectationActifDTO;
import com.dgi.gestionactifs.service.mapper.AffectationActifMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AffectationActifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AffectationActifResourceIT {

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final StatutAffectation DEFAULT_STATUT = StatutAffectation.ACTIVE;
    private static final StatutAffectation UPDATED_STATUT = StatutAffectation.CLOTUREE;

    private static final String ENTITY_API_URL = "/api/affectation-actifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AffectationActifRepository affectationActifRepository;

    @Autowired
    private AffectationActifMapper affectationActifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAffectationActifMockMvc;

    private AffectationActif affectationActif;

    private AffectationActif insertedAffectationActif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationActif createEntity(EntityManager em) {
        AffectationActif affectationActif = new AffectationActif().observation(DEFAULT_OBSERVATION).statut(DEFAULT_STATUT);
        // Add required entity
        Affectation affectation;
        if (TestUtil.findAll(em, Affectation.class).isEmpty()) {
            affectation = AffectationResourceIT.createEntity(em);
            em.persist(affectation);
            em.flush();
        } else {
            affectation = TestUtil.findAll(em, Affectation.class).get(0);
        }
        affectationActif.setAffectation(affectation);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        affectationActif.setActif(actif);
        return affectationActif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AffectationActif createUpdatedEntity(EntityManager em) {
        AffectationActif updatedAffectationActif = new AffectationActif().observation(UPDATED_OBSERVATION).statut(UPDATED_STATUT);
        // Add required entity
        Affectation affectation;
        if (TestUtil.findAll(em, Affectation.class).isEmpty()) {
            affectation = AffectationResourceIT.createUpdatedEntity(em);
            em.persist(affectation);
            em.flush();
        } else {
            affectation = TestUtil.findAll(em, Affectation.class).get(0);
        }
        updatedAffectationActif.setAffectation(affectation);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createUpdatedEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        updatedAffectationActif.setActif(actif);
        return updatedAffectationActif;
    }

    @BeforeEach
    void initTest() {
        affectationActif = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAffectationActif != null) {
            affectationActifRepository.delete(insertedAffectationActif);
            insertedAffectationActif = null;
        }
    }

    @Test
    @Transactional
    void createAffectationActif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);
        var returnedAffectationActifDTO = om.readValue(
            restAffectationActifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationActifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AffectationActifDTO.class
        );

        // Validate the AffectationActif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAffectationActif = affectationActifMapper.toEntity(returnedAffectationActifDTO);
        assertAffectationActifUpdatableFieldsEquals(returnedAffectationActif, getPersistedAffectationActif(returnedAffectationActif));

        insertedAffectationActif = returnedAffectationActif;
    }

    @Test
    @Transactional
    void createAffectationActifWithExistingId() throws Exception {
        // Create the AffectationActif with an existing ID
        affectationActif.setId(1L);
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAffectationActifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationActifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        affectationActif.setStatut(null);

        // Create the AffectationActif, which fails.
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        restAffectationActifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationActifDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAffectationActifs() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList
        restAffectationActifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectationActif.getId().intValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getAffectationActif() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get the affectationActif
        restAffectationActifMockMvc
            .perform(get(ENTITY_API_URL_ID, affectationActif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(affectationActif.getId().intValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getAffectationActifsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        Long id = affectationActif.getId();

        defaultAffectationActifFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAffectationActifFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAffectationActifFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAffectationActifsByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where observation equals to
        defaultAffectationActifFiltering("observation.equals=" + DEFAULT_OBSERVATION, "observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllAffectationActifsByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where observation in
        defaultAffectationActifFiltering(
            "observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION,
            "observation.in=" + UPDATED_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllAffectationActifsByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where observation is not null
        defaultAffectationActifFiltering("observation.specified=true", "observation.specified=false");
    }

    @Test
    @Transactional
    void getAllAffectationActifsByObservationContainsSomething() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where observation contains
        defaultAffectationActifFiltering("observation.contains=" + DEFAULT_OBSERVATION, "observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllAffectationActifsByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where observation does not contain
        defaultAffectationActifFiltering(
            "observation.doesNotContain=" + UPDATED_OBSERVATION,
            "observation.doesNotContain=" + DEFAULT_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllAffectationActifsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where statut equals to
        defaultAffectationActifFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAffectationActifsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where statut in
        defaultAffectationActifFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllAffectationActifsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        // Get all the affectationActifList where statut is not null
        defaultAffectationActifFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllAffectationActifsByAffectationIsEqualToSomething() throws Exception {
        Affectation affectation;
        if (TestUtil.findAll(em, Affectation.class).isEmpty()) {
            affectationActifRepository.saveAndFlush(affectationActif);
            affectation = AffectationResourceIT.createEntity(em);
        } else {
            affectation = TestUtil.findAll(em, Affectation.class).get(0);
        }
        em.persist(affectation);
        em.flush();
        affectationActif.setAffectation(affectation);
        affectationActifRepository.saveAndFlush(affectationActif);
        Long affectationId = affectation.getId();
        // Get all the affectationActifList where affectation equals to affectationId
        defaultAffectationActifShouldBeFound("affectationId.equals=" + affectationId);

        // Get all the affectationActifList where affectation equals to (affectationId + 1)
        defaultAffectationActifShouldNotBeFound("affectationId.equals=" + (affectationId + 1));
    }

    @Test
    @Transactional
    void getAllAffectationActifsByActifIsEqualToSomething() throws Exception {
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            affectationActifRepository.saveAndFlush(affectationActif);
            actif = ActifResourceIT.createEntity(em);
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        em.persist(actif);
        em.flush();
        affectationActif.setActif(actif);
        affectationActifRepository.saveAndFlush(affectationActif);
        Long actifId = actif.getId();
        // Get all the affectationActifList where actif equals to actifId
        defaultAffectationActifShouldBeFound("actifId.equals=" + actifId);

        // Get all the affectationActifList where actif equals to (actifId + 1)
        defaultAffectationActifShouldNotBeFound("actifId.equals=" + (actifId + 1));
    }

    private void defaultAffectationActifFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAffectationActifShouldBeFound(shouldBeFound);
        defaultAffectationActifShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAffectationActifShouldBeFound(String filter) throws Exception {
        restAffectationActifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(affectationActif.getId().intValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restAffectationActifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAffectationActifShouldNotBeFound(String filter) throws Exception {
        restAffectationActifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAffectationActifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAffectationActif() throws Exception {
        // Get the affectationActif
        restAffectationActifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAffectationActif() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationActif
        AffectationActif updatedAffectationActif = affectationActifRepository.findById(affectationActif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAffectationActif are not directly saved in db
        em.detach(updatedAffectationActif);
        updatedAffectationActif.observation(UPDATED_OBSERVATION).statut(UPDATED_STATUT);
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(updatedAffectationActif);

        restAffectationActifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationActifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationActifDTO))
            )
            .andExpect(status().isOk());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAffectationActifToMatchAllProperties(updatedAffectationActif);
    }

    @Test
    @Transactional
    void putNonExistingAffectationActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationActif.setId(longCount.incrementAndGet());

        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationActifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, affectationActifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAffectationActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationActif.setId(longCount.incrementAndGet());

        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationActifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(affectationActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAffectationActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationActif.setId(longCount.incrementAndGet());

        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationActifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(affectationActifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAffectationActifWithPatch() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationActif using partial update
        AffectationActif partialUpdatedAffectationActif = new AffectationActif();
        partialUpdatedAffectationActif.setId(affectationActif.getId());

        restAffectationActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationActif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAffectationActif))
            )
            .andExpect(status().isOk());

        // Validate the AffectationActif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAffectationActifUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAffectationActif, affectationActif),
            getPersistedAffectationActif(affectationActif)
        );
    }

    @Test
    @Transactional
    void fullUpdateAffectationActifWithPatch() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the affectationActif using partial update
        AffectationActif partialUpdatedAffectationActif = new AffectationActif();
        partialUpdatedAffectationActif.setId(affectationActif.getId());

        partialUpdatedAffectationActif.observation(UPDATED_OBSERVATION).statut(UPDATED_STATUT);

        restAffectationActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAffectationActif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAffectationActif))
            )
            .andExpect(status().isOk());

        // Validate the AffectationActif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAffectationActifUpdatableFieldsEquals(
            partialUpdatedAffectationActif,
            getPersistedAffectationActif(partialUpdatedAffectationActif)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAffectationActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationActif.setId(longCount.incrementAndGet());

        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAffectationActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, affectationActifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAffectationActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationActif.setId(longCount.incrementAndGet());

        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(affectationActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAffectationActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        affectationActif.setId(longCount.incrementAndGet());

        // Create the AffectationActif
        AffectationActifDTO affectationActifDTO = affectationActifMapper.toDto(affectationActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAffectationActifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(affectationActifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AffectationActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAffectationActif() throws Exception {
        // Initialize the database
        insertedAffectationActif = affectationActifRepository.saveAndFlush(affectationActif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the affectationActif
        restAffectationActifMockMvc
            .perform(delete(ENTITY_API_URL_ID, affectationActif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return affectationActifRepository.count();
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

    protected AffectationActif getPersistedAffectationActif(AffectationActif affectationActif) {
        return affectationActifRepository.findById(affectationActif.getId()).orElseThrow();
    }

    protected void assertPersistedAffectationActifToMatchAllProperties(AffectationActif expectedAffectationActif) {
        assertAffectationActifAllPropertiesEquals(expectedAffectationActif, getPersistedAffectationActif(expectedAffectationActif));
    }

    protected void assertPersistedAffectationActifToMatchUpdatableProperties(AffectationActif expectedAffectationActif) {
        assertAffectationActifAllUpdatablePropertiesEquals(
            expectedAffectationActif,
            getPersistedAffectationActif(expectedAffectationActif)
        );
    }
}
