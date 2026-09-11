package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.RecensementAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Recensement;
import com.dgi.gestionactifs.domain.enumeration.StatutRecensement;
import com.dgi.gestionactifs.repository.RecensementRepository;
import com.dgi.gestionactifs.service.dto.RecensementDTO;
import com.dgi.gestionactifs.service.mapper.RecensementMapper;
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
 * Integration tests for the {@link RecensementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecensementResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.parse("2026-09-02");
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final StatutRecensement DEFAULT_STATUT = StatutRecensement.PLANIFIER;
    private static final StatutRecensement UPDATED_STATUT = StatutRecensement.EN_COURS;

    private static final String ENTITY_API_URL = "/api/recensements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecensementRepository recensementRepository;

    @Autowired
    private RecensementMapper recensementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecensementMockMvc;

    private Recensement recensement;

    private Recensement insertedRecensement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recensement createEntity() {
        return new Recensement().dateDebut(DEFAULT_DATE_DEBUT).dateFin(DEFAULT_DATE_FIN).statut(DEFAULT_STATUT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recensement createUpdatedEntity() {
        return new Recensement().dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).statut(UPDATED_STATUT);
    }

    @BeforeEach
    void initTest() {
        recensement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecensement != null) {
            recensementRepository.delete(insertedRecensement);
            insertedRecensement = null;
        }
    }

    @Test
    @Transactional
    void createRecensement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);
        var returnedRecensementDTO = om.readValue(
            restRecensementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RecensementDTO.class
        );

        // Validate the Recensement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRecensement = recensementMapper.toEntity(returnedRecensementDTO);
        assertRecensementUpdatableFieldsEquals(returnedRecensement, getPersistedRecensement(returnedRecensement));

        insertedRecensement = returnedRecensement;
    }

    @Test
    @Transactional
    void createRecensementWithExistingId() throws Exception {
        // Create the Recensement with an existing ID
        recensement.setId(1L);
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecensementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recensement.setDateDebut(null);

        // Create the Recensement, which fails.
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        restRecensementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        recensement.setStatut(null);

        // Create the Recensement, which fails.
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        restRecensementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRecensements() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList
        restRecensementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recensement.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getRecensement() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get the recensement
        restRecensementMockMvc
            .perform(get(ENTITY_API_URL_ID, recensement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recensement.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getRecensementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        Long id = recensement.getId();

        defaultRecensementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecensementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecensementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut equals to
        defaultRecensementFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut in
        defaultRecensementFiltering("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT, "dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut is not null
        defaultRecensementFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut is greater than or equal to
        defaultRecensementFiltering(
            "dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT,
            "dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut is less than or equal to
        defaultRecensementFiltering("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT, "dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut is less than
        defaultRecensementFiltering("dateDebut.lessThan=" + UPDATED_DATE_DEBUT, "dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateDebut is greater than
        defaultRecensementFiltering("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT, "dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin equals to
        defaultRecensementFiltering("dateFin.equals=" + DEFAULT_DATE_FIN, "dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin in
        defaultRecensementFiltering("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN, "dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin is not null
        defaultRecensementFiltering("dateFin.specified=true", "dateFin.specified=false");
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin is greater than or equal to
        defaultRecensementFiltering("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin is less than or equal to
        defaultRecensementFiltering("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN, "dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin is less than
        defaultRecensementFiltering("dateFin.lessThan=" + UPDATED_DATE_FIN, "dateFin.lessThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllRecensementsByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where dateFin is greater than
        defaultRecensementFiltering("dateFin.greaterThan=" + SMALLER_DATE_FIN, "dateFin.greaterThan=" + DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void getAllRecensementsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where statut equals to
        defaultRecensementFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where statut in
        defaultRecensementFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllRecensementsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        // Get all the recensementList where statut is not null
        defaultRecensementFiltering("statut.specified=true", "statut.specified=false");
    }

    private void defaultRecensementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecensementShouldBeFound(shouldBeFound);
        defaultRecensementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecensementShouldBeFound(String filter) throws Exception {
        restRecensementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recensement.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restRecensementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecensementShouldNotBeFound(String filter) throws Exception {
        restRecensementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecensementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecensement() throws Exception {
        // Get the recensement
        restRecensementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecensement() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recensement
        Recensement updatedRecensement = recensementRepository.findById(recensement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecensement are not directly saved in db
        em.detach(updatedRecensement);
        updatedRecensement.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).statut(UPDATED_STATUT);
        RecensementDTO recensementDTO = recensementMapper.toDto(updatedRecensement);

        restRecensementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recensementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recensementDTO))
            )
            .andExpect(status().isOk());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecensementToMatchAllProperties(updatedRecensement);
    }

    @Test
    @Transactional
    void putNonExistingRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensement.setId(longCount.incrementAndGet());

        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecensementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recensementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensement.setId(longCount.incrementAndGet());

        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensement.setId(longCount.incrementAndGet());

        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recensementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecensementWithPatch() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recensement using partial update
        Recensement partialUpdatedRecensement = new Recensement();
        partialUpdatedRecensement.setId(recensement.getId());

        partialUpdatedRecensement.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).statut(UPDATED_STATUT);

        restRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecensement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecensement))
            )
            .andExpect(status().isOk());

        // Validate the Recensement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecensementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRecensement, recensement),
            getPersistedRecensement(recensement)
        );
    }

    @Test
    @Transactional
    void fullUpdateRecensementWithPatch() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recensement using partial update
        Recensement partialUpdatedRecensement = new Recensement();
        partialUpdatedRecensement.setId(recensement.getId());

        partialUpdatedRecensement.dateDebut(UPDATED_DATE_DEBUT).dateFin(UPDATED_DATE_FIN).statut(UPDATED_STATUT);

        restRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecensement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecensement))
            )
            .andExpect(status().isOk());

        // Validate the Recensement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecensementUpdatableFieldsEquals(partialUpdatedRecensement, getPersistedRecensement(partialUpdatedRecensement));
    }

    @Test
    @Transactional
    void patchNonExistingRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensement.setId(longCount.incrementAndGet());

        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recensementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensement.setId(longCount.incrementAndGet());

        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recensementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecensement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recensement.setId(longCount.incrementAndGet());

        // Create the Recensement
        RecensementDTO recensementDTO = recensementMapper.toDto(recensement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecensementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recensementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recensement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecensement() throws Exception {
        // Initialize the database
        insertedRecensement = recensementRepository.saveAndFlush(recensement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recensement
        restRecensementMockMvc
            .perform(delete(ENTITY_API_URL_ID, recensement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recensementRepository.count();
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

    protected Recensement getPersistedRecensement(Recensement recensement) {
        return recensementRepository.findById(recensement.getId()).orElseThrow();
    }

    protected void assertPersistedRecensementToMatchAllProperties(Recensement expectedRecensement) {
        assertRecensementAllPropertiesEquals(expectedRecensement, getPersistedRecensement(expectedRecensement));
    }

    protected void assertPersistedRecensementToMatchUpdatableProperties(Recensement expectedRecensement) {
        assertRecensementAllUpdatablePropertiesEquals(expectedRecensement, getPersistedRecensement(expectedRecensement));
    }
}
