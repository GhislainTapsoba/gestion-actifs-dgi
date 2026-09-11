package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.ServiceDgiAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.repository.ServiceDgiRepository;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import com.dgi.gestionactifs.service.mapper.ServiceDgiMapper;
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
 * Integration tests for the {@link ServiceDgiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceDgiResourceIT {

    private static final String DEFAULT_NOM_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SERVICE = "BBBBBBBBBB";

    private static final String DEFAULT_CHEF_SERVICE = "AAAAAAAAAA";
    private static final String UPDATED_CHEF_SERVICE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/service-dgis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceDgiRepository serviceDgiRepository;

    @Autowired
    private ServiceDgiMapper serviceDgiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceDgiMockMvc;

    private ServiceDgi serviceDgi;

    private ServiceDgi insertedServiceDgi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceDgi createEntity() {
        return new ServiceDgi().nomService(DEFAULT_NOM_SERVICE).chefService(DEFAULT_CHEF_SERVICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceDgi createUpdatedEntity() {
        return new ServiceDgi().nomService(UPDATED_NOM_SERVICE).chefService(UPDATED_CHEF_SERVICE);
    }

    @BeforeEach
    void initTest() {
        serviceDgi = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceDgi != null) {
            serviceDgiRepository.delete(insertedServiceDgi);
            insertedServiceDgi = null;
        }
    }

    @Test
    @Transactional
    void createServiceDgi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);
        var returnedServiceDgiDTO = om.readValue(
            restServiceDgiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceDgiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceDgiDTO.class
        );

        // Validate the ServiceDgi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceDgi = serviceDgiMapper.toEntity(returnedServiceDgiDTO);
        assertServiceDgiUpdatableFieldsEquals(returnedServiceDgi, getPersistedServiceDgi(returnedServiceDgi));

        insertedServiceDgi = returnedServiceDgi;
    }

    @Test
    @Transactional
    void createServiceDgiWithExistingId() throws Exception {
        // Create the ServiceDgi with an existing ID
        serviceDgi.setId(1L);
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceDgiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceDgiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomServiceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceDgi.setNomService(null);

        // Create the ServiceDgi, which fails.
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        restServiceDgiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceDgiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceDgis() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList
        restServiceDgiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceDgi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomService").value(hasItem(DEFAULT_NOM_SERVICE)))
            .andExpect(jsonPath("$.[*].chefService").value(hasItem(DEFAULT_CHEF_SERVICE)));
    }

    @Test
    @Transactional
    void getServiceDgi() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get the serviceDgi
        restServiceDgiMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceDgi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceDgi.getId().intValue()))
            .andExpect(jsonPath("$.nomService").value(DEFAULT_NOM_SERVICE))
            .andExpect(jsonPath("$.chefService").value(DEFAULT_CHEF_SERVICE));
    }

    @Test
    @Transactional
    void getServiceDgisByIdFiltering() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        Long id = serviceDgi.getId();

        defaultServiceDgiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultServiceDgiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultServiceDgiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllServiceDgisByNomServiceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where nomService equals to
        defaultServiceDgiFiltering("nomService.equals=" + DEFAULT_NOM_SERVICE, "nomService.equals=" + UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void getAllServiceDgisByNomServiceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where nomService in
        defaultServiceDgiFiltering(
            "nomService.in=" + DEFAULT_NOM_SERVICE + "," + UPDATED_NOM_SERVICE,
            "nomService.in=" + UPDATED_NOM_SERVICE
        );
    }

    @Test
    @Transactional
    void getAllServiceDgisByNomServiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where nomService is not null
        defaultServiceDgiFiltering("nomService.specified=true", "nomService.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceDgisByNomServiceContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where nomService contains
        defaultServiceDgiFiltering("nomService.contains=" + DEFAULT_NOM_SERVICE, "nomService.contains=" + UPDATED_NOM_SERVICE);
    }

    @Test
    @Transactional
    void getAllServiceDgisByNomServiceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where nomService does not contain
        defaultServiceDgiFiltering("nomService.doesNotContain=" + UPDATED_NOM_SERVICE, "nomService.doesNotContain=" + DEFAULT_NOM_SERVICE);
    }

    @Test
    @Transactional
    void getAllServiceDgisByChefServiceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where chefService equals to
        defaultServiceDgiFiltering("chefService.equals=" + DEFAULT_CHEF_SERVICE, "chefService.equals=" + UPDATED_CHEF_SERVICE);
    }

    @Test
    @Transactional
    void getAllServiceDgisByChefServiceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where chefService in
        defaultServiceDgiFiltering(
            "chefService.in=" + DEFAULT_CHEF_SERVICE + "," + UPDATED_CHEF_SERVICE,
            "chefService.in=" + UPDATED_CHEF_SERVICE
        );
    }

    @Test
    @Transactional
    void getAllServiceDgisByChefServiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where chefService is not null
        defaultServiceDgiFiltering("chefService.specified=true", "chefService.specified=false");
    }

    @Test
    @Transactional
    void getAllServiceDgisByChefServiceContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where chefService contains
        defaultServiceDgiFiltering("chefService.contains=" + DEFAULT_CHEF_SERVICE, "chefService.contains=" + UPDATED_CHEF_SERVICE);
    }

    @Test
    @Transactional
    void getAllServiceDgisByChefServiceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        // Get all the serviceDgiList where chefService does not contain
        defaultServiceDgiFiltering(
            "chefService.doesNotContain=" + UPDATED_CHEF_SERVICE,
            "chefService.doesNotContain=" + DEFAULT_CHEF_SERVICE
        );
    }

    private void defaultServiceDgiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultServiceDgiShouldBeFound(shouldBeFound);
        defaultServiceDgiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServiceDgiShouldBeFound(String filter) throws Exception {
        restServiceDgiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceDgi.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomService").value(hasItem(DEFAULT_NOM_SERVICE)))
            .andExpect(jsonPath("$.[*].chefService").value(hasItem(DEFAULT_CHEF_SERVICE)));

        // Check, that the count call also returns 1
        restServiceDgiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServiceDgiShouldNotBeFound(String filter) throws Exception {
        restServiceDgiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServiceDgiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingServiceDgi() throws Exception {
        // Get the serviceDgi
        restServiceDgiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceDgi() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceDgi
        ServiceDgi updatedServiceDgi = serviceDgiRepository.findById(serviceDgi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceDgi are not directly saved in db
        em.detach(updatedServiceDgi);
        updatedServiceDgi.nomService(UPDATED_NOM_SERVICE).chefService(UPDATED_CHEF_SERVICE);
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(updatedServiceDgi);

        restServiceDgiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceDgiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceDgiDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceDgiToMatchAllProperties(updatedServiceDgi);
    }

    @Test
    @Transactional
    void putNonExistingServiceDgi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceDgi.setId(longCount.incrementAndGet());

        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceDgiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceDgiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceDgiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceDgi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceDgi.setId(longCount.incrementAndGet());

        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDgiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceDgiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceDgi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceDgi.setId(longCount.incrementAndGet());

        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDgiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceDgiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceDgiWithPatch() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceDgi using partial update
        ServiceDgi partialUpdatedServiceDgi = new ServiceDgi();
        partialUpdatedServiceDgi.setId(serviceDgi.getId());

        partialUpdatedServiceDgi.nomService(UPDATED_NOM_SERVICE);

        restServiceDgiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceDgi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceDgi))
            )
            .andExpect(status().isOk());

        // Validate the ServiceDgi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceDgiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceDgi, serviceDgi),
            getPersistedServiceDgi(serviceDgi)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceDgiWithPatch() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceDgi using partial update
        ServiceDgi partialUpdatedServiceDgi = new ServiceDgi();
        partialUpdatedServiceDgi.setId(serviceDgi.getId());

        partialUpdatedServiceDgi.nomService(UPDATED_NOM_SERVICE).chefService(UPDATED_CHEF_SERVICE);

        restServiceDgiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceDgi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceDgi))
            )
            .andExpect(status().isOk());

        // Validate the ServiceDgi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceDgiUpdatableFieldsEquals(partialUpdatedServiceDgi, getPersistedServiceDgi(partialUpdatedServiceDgi));
    }

    @Test
    @Transactional
    void patchNonExistingServiceDgi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceDgi.setId(longCount.incrementAndGet());

        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceDgiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceDgiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceDgiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceDgi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceDgi.setId(longCount.incrementAndGet());

        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDgiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceDgiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceDgi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceDgi.setId(longCount.incrementAndGet());

        // Create the ServiceDgi
        ServiceDgiDTO serviceDgiDTO = serviceDgiMapper.toDto(serviceDgi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceDgiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceDgiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceDgi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceDgi() throws Exception {
        // Initialize the database
        insertedServiceDgi = serviceDgiRepository.saveAndFlush(serviceDgi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceDgi
        restServiceDgiMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceDgi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceDgiRepository.count();
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

    protected ServiceDgi getPersistedServiceDgi(ServiceDgi serviceDgi) {
        return serviceDgiRepository.findById(serviceDgi.getId()).orElseThrow();
    }

    protected void assertPersistedServiceDgiToMatchAllProperties(ServiceDgi expectedServiceDgi) {
        assertServiceDgiAllPropertiesEquals(expectedServiceDgi, getPersistedServiceDgi(expectedServiceDgi));
    }

    protected void assertPersistedServiceDgiToMatchUpdatableProperties(ServiceDgi expectedServiceDgi) {
        assertServiceDgiAllUpdatablePropertiesEquals(expectedServiceDgi, getPersistedServiceDgi(expectedServiceDgi));
    }
}
