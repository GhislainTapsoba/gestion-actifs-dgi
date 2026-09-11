package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.PlanningMaintenanceAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.PlanningMaintenance;
import com.dgi.gestionactifs.domain.enumeration.StatutPlanning;
import com.dgi.gestionactifs.repository.PlanningMaintenanceRepository;
import com.dgi.gestionactifs.service.PlanningMaintenanceService;
import com.dgi.gestionactifs.service.dto.PlanningMaintenanceDTO;
import com.dgi.gestionactifs.service.mapper.PlanningMaintenanceMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Integration tests for the {@link PlanningMaintenanceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanningMaintenanceResourceIT {

    private static final LocalDate DEFAULT_DATE_PREVUE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_PREVUE = LocalDate.parse("2026-09-02");

    private static final String DEFAULT_PERIODICITE = "AAAAAAAAAA";
    private static final String UPDATED_PERIODICITE = "BBBBBBBBBB";

    private static final StatutPlanning DEFAULT_STATUT = StatutPlanning.PLANIFIER;
    private static final StatutPlanning UPDATED_STATUT = StatutPlanning.EN_COURS;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/planning-maintenances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanningMaintenanceRepository planningMaintenanceRepository;

    @Mock
    private PlanningMaintenanceRepository planningMaintenanceRepositoryMock;

    @Autowired
    private PlanningMaintenanceMapper planningMaintenanceMapper;

    @Mock
    private PlanningMaintenanceService planningMaintenanceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanningMaintenanceMockMvc;

    private PlanningMaintenance planningMaintenance;

    private PlanningMaintenance insertedPlanningMaintenance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanningMaintenance createEntity() {
        return new PlanningMaintenance()
            .datePrevue(DEFAULT_DATE_PREVUE)
            .periodicite(DEFAULT_PERIODICITE)
            .statut(DEFAULT_STATUT)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanningMaintenance createUpdatedEntity() {
        return new PlanningMaintenance()
            .datePrevue(UPDATED_DATE_PREVUE)
            .periodicite(UPDATED_PERIODICITE)
            .statut(UPDATED_STATUT)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        planningMaintenance = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlanningMaintenance != null) {
            planningMaintenanceRepository.delete(insertedPlanningMaintenance);
            insertedPlanningMaintenance = null;
        }
    }

    @Test
    @Transactional
    void createPlanningMaintenance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);
        var returnedPlanningMaintenanceDTO = om.readValue(
            restPlanningMaintenanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningMaintenanceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanningMaintenanceDTO.class
        );

        // Validate the PlanningMaintenance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlanningMaintenance = planningMaintenanceMapper.toEntity(returnedPlanningMaintenanceDTO);
        assertPlanningMaintenanceUpdatableFieldsEquals(
            returnedPlanningMaintenance,
            getPersistedPlanningMaintenance(returnedPlanningMaintenance)
        );

        insertedPlanningMaintenance = returnedPlanningMaintenance;
    }

    @Test
    @Transactional
    void createPlanningMaintenanceWithExistingId() throws Exception {
        // Create the PlanningMaintenance with an existing ID
        planningMaintenance.setId(1L);
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanningMaintenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningMaintenanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDatePrevueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planningMaintenance.setDatePrevue(null);

        // Create the PlanningMaintenance, which fails.
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        restPlanningMaintenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningMaintenanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planningMaintenance.setStatut(null);

        // Create the PlanningMaintenance, which fails.
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        restPlanningMaintenanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningMaintenanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanningMaintenances() throws Exception {
        // Initialize the database
        insertedPlanningMaintenance = planningMaintenanceRepository.saveAndFlush(planningMaintenance);

        // Get all the planningMaintenanceList
        restPlanningMaintenanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planningMaintenance.getId().intValue())))
            .andExpect(jsonPath("$.[*].datePrevue").value(hasItem(DEFAULT_DATE_PREVUE.toString())))
            .andExpect(jsonPath("$.[*].periodicite").value(hasItem(DEFAULT_PERIODICITE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanningMaintenancesWithEagerRelationshipsIsEnabled() throws Exception {
        when(planningMaintenanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanningMaintenanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planningMaintenanceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanningMaintenancesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planningMaintenanceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanningMaintenanceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(planningMaintenanceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlanningMaintenance() throws Exception {
        // Initialize the database
        insertedPlanningMaintenance = planningMaintenanceRepository.saveAndFlush(planningMaintenance);

        // Get the planningMaintenance
        restPlanningMaintenanceMockMvc
            .perform(get(ENTITY_API_URL_ID, planningMaintenance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planningMaintenance.getId().intValue()))
            .andExpect(jsonPath("$.datePrevue").value(DEFAULT_DATE_PREVUE.toString()))
            .andExpect(jsonPath("$.periodicite").value(DEFAULT_PERIODICITE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPlanningMaintenance() throws Exception {
        // Get the planningMaintenance
        restPlanningMaintenanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanningMaintenance() throws Exception {
        // Initialize the database
        insertedPlanningMaintenance = planningMaintenanceRepository.saveAndFlush(planningMaintenance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planningMaintenance
        PlanningMaintenance updatedPlanningMaintenance = planningMaintenanceRepository.findById(planningMaintenance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanningMaintenance are not directly saved in db
        em.detach(updatedPlanningMaintenance);
        updatedPlanningMaintenance
            .datePrevue(UPDATED_DATE_PREVUE)
            .periodicite(UPDATED_PERIODICITE)
            .statut(UPDATED_STATUT)
            .description(UPDATED_DESCRIPTION);
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(updatedPlanningMaintenance);

        restPlanningMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planningMaintenanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planningMaintenanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanningMaintenanceToMatchAllProperties(updatedPlanningMaintenance);
    }

    @Test
    @Transactional
    void putNonExistingPlanningMaintenance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planningMaintenance.setId(longCount.incrementAndGet());

        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planningMaintenanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planningMaintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanningMaintenance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planningMaintenance.setId(longCount.incrementAndGet());

        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMaintenanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planningMaintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanningMaintenance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planningMaintenance.setId(longCount.incrementAndGet());

        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMaintenanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planningMaintenanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanningMaintenanceWithPatch() throws Exception {
        // Initialize the database
        insertedPlanningMaintenance = planningMaintenanceRepository.saveAndFlush(planningMaintenance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planningMaintenance using partial update
        PlanningMaintenance partialUpdatedPlanningMaintenance = new PlanningMaintenance();
        partialUpdatedPlanningMaintenance.setId(planningMaintenance.getId());

        partialUpdatedPlanningMaintenance.datePrevue(UPDATED_DATE_PREVUE);

        restPlanningMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanningMaintenance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanningMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the PlanningMaintenance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanningMaintenanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlanningMaintenance, planningMaintenance),
            getPersistedPlanningMaintenance(planningMaintenance)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlanningMaintenanceWithPatch() throws Exception {
        // Initialize the database
        insertedPlanningMaintenance = planningMaintenanceRepository.saveAndFlush(planningMaintenance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planningMaintenance using partial update
        PlanningMaintenance partialUpdatedPlanningMaintenance = new PlanningMaintenance();
        partialUpdatedPlanningMaintenance.setId(planningMaintenance.getId());

        partialUpdatedPlanningMaintenance
            .datePrevue(UPDATED_DATE_PREVUE)
            .periodicite(UPDATED_PERIODICITE)
            .statut(UPDATED_STATUT)
            .description(UPDATED_DESCRIPTION);

        restPlanningMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanningMaintenance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanningMaintenance))
            )
            .andExpect(status().isOk());

        // Validate the PlanningMaintenance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanningMaintenanceUpdatableFieldsEquals(
            partialUpdatedPlanningMaintenance,
            getPersistedPlanningMaintenance(partialUpdatedPlanningMaintenance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPlanningMaintenance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planningMaintenance.setId(longCount.incrementAndGet());

        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanningMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planningMaintenanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planningMaintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanningMaintenance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planningMaintenance.setId(longCount.incrementAndGet());

        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planningMaintenanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanningMaintenance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planningMaintenance.setId(longCount.incrementAndGet());

        // Create the PlanningMaintenance
        PlanningMaintenanceDTO planningMaintenanceDTO = planningMaintenanceMapper.toDto(planningMaintenance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanningMaintenanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planningMaintenanceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanningMaintenance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanningMaintenance() throws Exception {
        // Initialize the database
        insertedPlanningMaintenance = planningMaintenanceRepository.saveAndFlush(planningMaintenance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planningMaintenance
        restPlanningMaintenanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, planningMaintenance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planningMaintenanceRepository.count();
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

    protected PlanningMaintenance getPersistedPlanningMaintenance(PlanningMaintenance planningMaintenance) {
        return planningMaintenanceRepository.findById(planningMaintenance.getId()).orElseThrow();
    }

    protected void assertPersistedPlanningMaintenanceToMatchAllProperties(PlanningMaintenance expectedPlanningMaintenance) {
        assertPlanningMaintenanceAllPropertiesEquals(
            expectedPlanningMaintenance,
            getPersistedPlanningMaintenance(expectedPlanningMaintenance)
        );
    }

    protected void assertPersistedPlanningMaintenanceToMatchUpdatableProperties(PlanningMaintenance expectedPlanningMaintenance) {
        assertPlanningMaintenanceAllUpdatablePropertiesEquals(
            expectedPlanningMaintenance,
            getPersistedPlanningMaintenance(expectedPlanningMaintenance)
        );
    }
}
