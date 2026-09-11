package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.CategorieMaterielAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.CategorieMateriel;
import com.dgi.gestionactifs.repository.CategorieMaterielRepository;
import com.dgi.gestionactifs.service.dto.CategorieMaterielDTO;
import com.dgi.gestionactifs.service.mapper.CategorieMaterielMapper;
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
 * Integration tests for the {@link CategorieMaterielResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategorieMaterielResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/categorie-materiels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CategorieMaterielRepository categorieMaterielRepository;

    @Autowired
    private CategorieMaterielMapper categorieMaterielMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategorieMaterielMockMvc;

    private CategorieMateriel categorieMateriel;

    private CategorieMateriel insertedCategorieMateriel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieMateriel createEntity() {
        return new CategorieMateriel().libelle(DEFAULT_LIBELLE).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategorieMateriel createUpdatedEntity() {
        return new CategorieMateriel().libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    void initTest() {
        categorieMateriel = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCategorieMateriel != null) {
            categorieMaterielRepository.delete(insertedCategorieMateriel);
            insertedCategorieMateriel = null;
        }
    }

    @Test
    @Transactional
    void createCategorieMateriel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);
        var returnedCategorieMaterielDTO = om.readValue(
            restCategorieMaterielMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorieMaterielDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CategorieMaterielDTO.class
        );

        // Validate the CategorieMateriel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCategorieMateriel = categorieMaterielMapper.toEntity(returnedCategorieMaterielDTO);
        assertCategorieMaterielUpdatableFieldsEquals(returnedCategorieMateriel, getPersistedCategorieMateriel(returnedCategorieMateriel));

        insertedCategorieMateriel = returnedCategorieMateriel;
    }

    @Test
    @Transactional
    void createCategorieMaterielWithExistingId() throws Exception {
        // Create the CategorieMateriel with an existing ID
        categorieMateriel.setId(1L);
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieMaterielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorieMaterielDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLibelleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        categorieMateriel.setLibelle(null);

        // Create the CategorieMateriel, which fails.
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        restCategorieMaterielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorieMaterielDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategorieMateriels() throws Exception {
        // Initialize the database
        insertedCategorieMateriel = categorieMaterielRepository.saveAndFlush(categorieMateriel);

        // Get all the categorieMaterielList
        restCategorieMaterielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieMateriel.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategorieMateriel() throws Exception {
        // Initialize the database
        insertedCategorieMateriel = categorieMaterielRepository.saveAndFlush(categorieMateriel);

        // Get the categorieMateriel
        restCategorieMaterielMockMvc
            .perform(get(ENTITY_API_URL_ID, categorieMateriel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categorieMateriel.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCategorieMateriel() throws Exception {
        // Get the categorieMateriel
        restCategorieMaterielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategorieMateriel() throws Exception {
        // Initialize the database
        insertedCategorieMateriel = categorieMaterielRepository.saveAndFlush(categorieMateriel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categorieMateriel
        CategorieMateriel updatedCategorieMateriel = categorieMaterielRepository.findById(categorieMateriel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategorieMateriel are not directly saved in db
        em.detach(updatedCategorieMateriel);
        updatedCategorieMateriel.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(updatedCategorieMateriel);

        restCategorieMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieMaterielDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categorieMaterielDTO))
            )
            .andExpect(status().isOk());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCategorieMaterielToMatchAllProperties(updatedCategorieMateriel);
    }

    @Test
    @Transactional
    void putNonExistingCategorieMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorieMateriel.setId(longCount.incrementAndGet());

        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categorieMaterielDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categorieMaterielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategorieMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorieMateriel.setId(longCount.incrementAndGet());

        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMaterielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(categorieMaterielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategorieMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorieMateriel.setId(longCount.incrementAndGet());

        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMaterielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(categorieMaterielDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategorieMaterielWithPatch() throws Exception {
        // Initialize the database
        insertedCategorieMateriel = categorieMaterielRepository.saveAndFlush(categorieMateriel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categorieMateriel using partial update
        CategorieMateriel partialUpdatedCategorieMateriel = new CategorieMateriel();
        partialUpdatedCategorieMateriel.setId(categorieMateriel.getId());

        partialUpdatedCategorieMateriel.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

        restCategorieMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieMateriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategorieMateriel))
            )
            .andExpect(status().isOk());

        // Validate the CategorieMateriel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategorieMaterielUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCategorieMateriel, categorieMateriel),
            getPersistedCategorieMateriel(categorieMateriel)
        );
    }

    @Test
    @Transactional
    void fullUpdateCategorieMaterielWithPatch() throws Exception {
        // Initialize the database
        insertedCategorieMateriel = categorieMaterielRepository.saveAndFlush(categorieMateriel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the categorieMateriel using partial update
        CategorieMateriel partialUpdatedCategorieMateriel = new CategorieMateriel();
        partialUpdatedCategorieMateriel.setId(categorieMateriel.getId());

        partialUpdatedCategorieMateriel.libelle(UPDATED_LIBELLE).description(UPDATED_DESCRIPTION);

        restCategorieMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategorieMateriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCategorieMateriel))
            )
            .andExpect(status().isOk());

        // Validate the CategorieMateriel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCategorieMaterielUpdatableFieldsEquals(
            partialUpdatedCategorieMateriel,
            getPersistedCategorieMateriel(partialUpdatedCategorieMateriel)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCategorieMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorieMateriel.setId(longCount.incrementAndGet());

        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategorieMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categorieMaterielDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categorieMaterielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategorieMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorieMateriel.setId(longCount.incrementAndGet());

        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMaterielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(categorieMaterielDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategorieMateriel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        categorieMateriel.setId(longCount.incrementAndGet());

        // Create the CategorieMateriel
        CategorieMaterielDTO categorieMaterielDTO = categorieMaterielMapper.toDto(categorieMateriel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategorieMaterielMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(categorieMaterielDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategorieMateriel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategorieMateriel() throws Exception {
        // Initialize the database
        insertedCategorieMateriel = categorieMaterielRepository.saveAndFlush(categorieMateriel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the categorieMateriel
        restCategorieMaterielMockMvc
            .perform(delete(ENTITY_API_URL_ID, categorieMateriel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return categorieMaterielRepository.count();
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

    protected CategorieMateriel getPersistedCategorieMateriel(CategorieMateriel categorieMateriel) {
        return categorieMaterielRepository.findById(categorieMateriel.getId()).orElseThrow();
    }

    protected void assertPersistedCategorieMaterielToMatchAllProperties(CategorieMateriel expectedCategorieMateriel) {
        assertCategorieMaterielAllPropertiesEquals(expectedCategorieMateriel, getPersistedCategorieMateriel(expectedCategorieMateriel));
    }

    protected void assertPersistedCategorieMaterielToMatchUpdatableProperties(CategorieMateriel expectedCategorieMateriel) {
        assertCategorieMaterielAllUpdatablePropertiesEquals(
            expectedCategorieMateriel,
            getPersistedCategorieMateriel(expectedCategorieMateriel)
        );
    }
}
