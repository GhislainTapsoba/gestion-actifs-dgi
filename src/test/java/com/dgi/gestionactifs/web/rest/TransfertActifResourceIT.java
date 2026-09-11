package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.TransfertActifAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.TransfertActif;
import com.dgi.gestionactifs.repository.TransfertActifRepository;
import com.dgi.gestionactifs.service.dto.TransfertActifDTO;
import com.dgi.gestionactifs.service.mapper.TransfertActifMapper;
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
 * Integration tests for the {@link TransfertActifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransfertActifResourceIT {

    private static final String DEFAULT_OBSERVATION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transfert-actifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransfertActifRepository transfertActifRepository;

    @Autowired
    private TransfertActifMapper transfertActifMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransfertActifMockMvc;

    private TransfertActif transfertActif;

    private TransfertActif insertedTransfertActif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransfertActif createEntity(EntityManager em) {
        TransfertActif transfertActif = new TransfertActif().observation(DEFAULT_OBSERVATION);
        // Add required entity
        Transfert transfert;
        if (TestUtil.findAll(em, Transfert.class).isEmpty()) {
            transfert = TransfertResourceIT.createEntity(em);
            em.persist(transfert);
            em.flush();
        } else {
            transfert = TestUtil.findAll(em, Transfert.class).get(0);
        }
        transfertActif.setTransfert(transfert);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        transfertActif.setActif(actif);
        return transfertActif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransfertActif createUpdatedEntity(EntityManager em) {
        TransfertActif updatedTransfertActif = new TransfertActif().observation(UPDATED_OBSERVATION);
        // Add required entity
        Transfert transfert;
        if (TestUtil.findAll(em, Transfert.class).isEmpty()) {
            transfert = TransfertResourceIT.createUpdatedEntity(em);
            em.persist(transfert);
            em.flush();
        } else {
            transfert = TestUtil.findAll(em, Transfert.class).get(0);
        }
        updatedTransfertActif.setTransfert(transfert);
        // Add required entity
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            actif = ActifResourceIT.createUpdatedEntity(em);
            em.persist(actif);
            em.flush();
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        updatedTransfertActif.setActif(actif);
        return updatedTransfertActif;
    }

    @BeforeEach
    void initTest() {
        transfertActif = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTransfertActif != null) {
            transfertActifRepository.delete(insertedTransfertActif);
            insertedTransfertActif = null;
        }
    }

    @Test
    @Transactional
    void createTransfertActif() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);
        var returnedTransfertActifDTO = om.readValue(
            restTransfertActifMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertActifDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransfertActifDTO.class
        );

        // Validate the TransfertActif in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransfertActif = transfertActifMapper.toEntity(returnedTransfertActifDTO);
        assertTransfertActifUpdatableFieldsEquals(returnedTransfertActif, getPersistedTransfertActif(returnedTransfertActif));

        insertedTransfertActif = returnedTransfertActif;
    }

    @Test
    @Transactional
    void createTransfertActifWithExistingId() throws Exception {
        // Create the TransfertActif with an existing ID
        transfertActif.setId(1L);
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransfertActifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertActifDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTransfertActifs() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get all the transfertActifList
        restTransfertActifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfertActif.getId().intValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));
    }

    @Test
    @Transactional
    void getTransfertActif() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get the transfertActif
        restTransfertActifMockMvc
            .perform(get(ENTITY_API_URL_ID, transfertActif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transfertActif.getId().intValue()))
            .andExpect(jsonPath("$.observation").value(DEFAULT_OBSERVATION));
    }

    @Test
    @Transactional
    void getTransfertActifsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        Long id = transfertActif.getId();

        defaultTransfertActifFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransfertActifFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransfertActifFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransfertActifsByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get all the transfertActifList where observation equals to
        defaultTransfertActifFiltering("observation.equals=" + DEFAULT_OBSERVATION, "observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllTransfertActifsByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get all the transfertActifList where observation in
        defaultTransfertActifFiltering(
            "observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION,
            "observation.in=" + UPDATED_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllTransfertActifsByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get all the transfertActifList where observation is not null
        defaultTransfertActifFiltering("observation.specified=true", "observation.specified=false");
    }

    @Test
    @Transactional
    void getAllTransfertActifsByObservationContainsSomething() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get all the transfertActifList where observation contains
        defaultTransfertActifFiltering("observation.contains=" + DEFAULT_OBSERVATION, "observation.contains=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllTransfertActifsByObservationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        // Get all the transfertActifList where observation does not contain
        defaultTransfertActifFiltering(
            "observation.doesNotContain=" + UPDATED_OBSERVATION,
            "observation.doesNotContain=" + DEFAULT_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllTransfertActifsByTransfertIsEqualToSomething() throws Exception {
        Transfert transfert;
        if (TestUtil.findAll(em, Transfert.class).isEmpty()) {
            transfertActifRepository.saveAndFlush(transfertActif);
            transfert = TransfertResourceIT.createEntity(em);
        } else {
            transfert = TestUtil.findAll(em, Transfert.class).get(0);
        }
        em.persist(transfert);
        em.flush();
        transfertActif.setTransfert(transfert);
        transfertActifRepository.saveAndFlush(transfertActif);
        Long transfertId = transfert.getId();
        // Get all the transfertActifList where transfert equals to transfertId
        defaultTransfertActifShouldBeFound("transfertId.equals=" + transfertId);

        // Get all the transfertActifList where transfert equals to (transfertId + 1)
        defaultTransfertActifShouldNotBeFound("transfertId.equals=" + (transfertId + 1));
    }

    @Test
    @Transactional
    void getAllTransfertActifsByActifIsEqualToSomething() throws Exception {
        Actif actif;
        if (TestUtil.findAll(em, Actif.class).isEmpty()) {
            transfertActifRepository.saveAndFlush(transfertActif);
            actif = ActifResourceIT.createEntity(em);
        } else {
            actif = TestUtil.findAll(em, Actif.class).get(0);
        }
        em.persist(actif);
        em.flush();
        transfertActif.setActif(actif);
        transfertActifRepository.saveAndFlush(transfertActif);
        Long actifId = actif.getId();
        // Get all the transfertActifList where actif equals to actifId
        defaultTransfertActifShouldBeFound("actifId.equals=" + actifId);

        // Get all the transfertActifList where actif equals to (actifId + 1)
        defaultTransfertActifShouldNotBeFound("actifId.equals=" + (actifId + 1));
    }

    private void defaultTransfertActifFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransfertActifShouldBeFound(shouldBeFound);
        defaultTransfertActifShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransfertActifShouldBeFound(String filter) throws Exception {
        restTransfertActifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfertActif.getId().intValue())))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(DEFAULT_OBSERVATION)));

        // Check, that the count call also returns 1
        restTransfertActifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransfertActifShouldNotBeFound(String filter) throws Exception {
        restTransfertActifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransfertActifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransfertActif() throws Exception {
        // Get the transfertActif
        restTransfertActifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransfertActif() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transfertActif
        TransfertActif updatedTransfertActif = transfertActifRepository.findById(transfertActif.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransfertActif are not directly saved in db
        em.detach(updatedTransfertActif);
        updatedTransfertActif.observation(UPDATED_OBSERVATION);
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(updatedTransfertActif);

        restTransfertActifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfertActifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transfertActifDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransfertActifToMatchAllProperties(updatedTransfertActif);
    }

    @Test
    @Transactional
    void putNonExistingTransfertActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfertActif.setId(longCount.incrementAndGet());

        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfertActifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transfertActifDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transfertActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransfertActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfertActif.setId(longCount.incrementAndGet());

        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertActifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transfertActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransfertActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfertActif.setId(longCount.incrementAndGet());

        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertActifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transfertActifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransfertActifWithPatch() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transfertActif using partial update
        TransfertActif partialUpdatedTransfertActif = new TransfertActif();
        partialUpdatedTransfertActif.setId(transfertActif.getId());

        restTransfertActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfertActif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransfertActif))
            )
            .andExpect(status().isOk());

        // Validate the TransfertActif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransfertActifUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransfertActif, transfertActif),
            getPersistedTransfertActif(transfertActif)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransfertActifWithPatch() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transfertActif using partial update
        TransfertActif partialUpdatedTransfertActif = new TransfertActif();
        partialUpdatedTransfertActif.setId(transfertActif.getId());

        partialUpdatedTransfertActif.observation(UPDATED_OBSERVATION);

        restTransfertActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransfertActif.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransfertActif))
            )
            .andExpect(status().isOk());

        // Validate the TransfertActif in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransfertActifUpdatableFieldsEquals(partialUpdatedTransfertActif, getPersistedTransfertActif(partialUpdatedTransfertActif));
    }

    @Test
    @Transactional
    void patchNonExistingTransfertActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfertActif.setId(longCount.incrementAndGet());

        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransfertActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transfertActifDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transfertActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransfertActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfertActif.setId(longCount.incrementAndGet());

        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertActifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transfertActifDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransfertActif() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transfertActif.setId(longCount.incrementAndGet());

        // Create the TransfertActif
        TransfertActifDTO transfertActifDTO = transfertActifMapper.toDto(transfertActif);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransfertActifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transfertActifDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransfertActif in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransfertActif() throws Exception {
        // Initialize the database
        insertedTransfertActif = transfertActifRepository.saveAndFlush(transfertActif);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transfertActif
        restTransfertActifMockMvc
            .perform(delete(ENTITY_API_URL_ID, transfertActif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transfertActifRepository.count();
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

    protected TransfertActif getPersistedTransfertActif(TransfertActif transfertActif) {
        return transfertActifRepository.findById(transfertActif.getId()).orElseThrow();
    }

    protected void assertPersistedTransfertActifToMatchAllProperties(TransfertActif expectedTransfertActif) {
        assertTransfertActifAllPropertiesEquals(expectedTransfertActif, getPersistedTransfertActif(expectedTransfertActif));
    }

    protected void assertPersistedTransfertActifToMatchUpdatableProperties(TransfertActif expectedTransfertActif) {
        assertTransfertActifAllUpdatablePropertiesEquals(expectedTransfertActif, getPersistedTransfertActif(expectedTransfertActif));
    }
}
