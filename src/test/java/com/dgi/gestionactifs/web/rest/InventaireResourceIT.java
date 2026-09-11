package com.dgi.gestionactifs.web.rest;

import static com.dgi.gestionactifs.domain.InventaireAsserts.*;
import static com.dgi.gestionactifs.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.dgi.gestionactifs.IntegrationTest;
import com.dgi.gestionactifs.domain.Inventaire;
import com.dgi.gestionactifs.repository.InventaireRepository;
import com.dgi.gestionactifs.service.dto.InventaireDTO;
import com.dgi.gestionactifs.service.mapper.InventaireMapper;
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
 * Integration tests for the {@link InventaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InventaireResourceIT {

    private static final String DEFAULT_NOM_FICHIER = "AAAAAAAAAA";
    private static final String UPDATED_NOM_FICHIER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_IMPORT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_IMPORT = LocalDate.parse("2026-09-02");

    private static final String ENTITY_API_URL = "/api/inventaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InventaireRepository inventaireRepository;

    @Autowired
    private InventaireMapper inventaireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInventaireMockMvc;

    private Inventaire inventaire;

    private Inventaire insertedInventaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventaire createEntity() {
        return new Inventaire().nomFichier(DEFAULT_NOM_FICHIER).dateImport(DEFAULT_DATE_IMPORT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventaire createUpdatedEntity() {
        return new Inventaire().nomFichier(UPDATED_NOM_FICHIER).dateImport(UPDATED_DATE_IMPORT);
    }

    @BeforeEach
    void initTest() {
        inventaire = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInventaire != null) {
            inventaireRepository.delete(insertedInventaire);
            insertedInventaire = null;
        }
    }

    @Test
    @Transactional
    void createInventaire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);
        var returnedInventaireDTO = om.readValue(
            restInventaireMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventaireDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InventaireDTO.class
        );

        // Validate the Inventaire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInventaire = inventaireMapper.toEntity(returnedInventaireDTO);
        assertInventaireUpdatableFieldsEquals(returnedInventaire, getPersistedInventaire(returnedInventaire));

        insertedInventaire = returnedInventaire;
    }

    @Test
    @Transactional
    void createInventaireWithExistingId() throws Exception {
        // Create the Inventaire with an existing ID
        inventaire.setId(1L);
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInventaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventaireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomFichierIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventaire.setNomFichier(null);

        // Create the Inventaire, which fails.
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        restInventaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateImportIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inventaire.setDateImport(null);

        // Create the Inventaire, which fails.
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        restInventaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventaireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInventaires() throws Exception {
        // Initialize the database
        insertedInventaire = inventaireRepository.saveAndFlush(inventaire);

        // Get all the inventaireList
        restInventaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inventaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomFichier").value(hasItem(DEFAULT_NOM_FICHIER)))
            .andExpect(jsonPath("$.[*].dateImport").value(hasItem(DEFAULT_DATE_IMPORT.toString())));
    }

    @Test
    @Transactional
    void getInventaire() throws Exception {
        // Initialize the database
        insertedInventaire = inventaireRepository.saveAndFlush(inventaire);

        // Get the inventaire
        restInventaireMockMvc
            .perform(get(ENTITY_API_URL_ID, inventaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inventaire.getId().intValue()))
            .andExpect(jsonPath("$.nomFichier").value(DEFAULT_NOM_FICHIER))
            .andExpect(jsonPath("$.dateImport").value(DEFAULT_DATE_IMPORT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInventaire() throws Exception {
        // Get the inventaire
        restInventaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInventaire() throws Exception {
        // Initialize the database
        insertedInventaire = inventaireRepository.saveAndFlush(inventaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventaire
        Inventaire updatedInventaire = inventaireRepository.findById(inventaire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInventaire are not directly saved in db
        em.detach(updatedInventaire);
        updatedInventaire.nomFichier(UPDATED_NOM_FICHIER).dateImport(UPDATED_DATE_IMPORT);
        InventaireDTO inventaireDTO = inventaireMapper.toDto(updatedInventaire);

        restInventaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventaireDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInventaireToMatchAllProperties(updatedInventaire);
    }

    @Test
    @Transactional
    void putNonExistingInventaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventaire.setId(longCount.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inventaireDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInventaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventaire.setId(longCount.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInventaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventaire.setId(longCount.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inventaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInventaireWithPatch() throws Exception {
        // Initialize the database
        insertedInventaire = inventaireRepository.saveAndFlush(inventaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventaire using partial update
        Inventaire partialUpdatedInventaire = new Inventaire();
        partialUpdatedInventaire.setId(inventaire.getId());

        partialUpdatedInventaire.nomFichier(UPDATED_NOM_FICHIER).dateImport(UPDATED_DATE_IMPORT);

        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventaire))
            )
            .andExpect(status().isOk());

        // Validate the Inventaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventaireUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInventaire, inventaire),
            getPersistedInventaire(inventaire)
        );
    }

    @Test
    @Transactional
    void fullUpdateInventaireWithPatch() throws Exception {
        // Initialize the database
        insertedInventaire = inventaireRepository.saveAndFlush(inventaire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inventaire using partial update
        Inventaire partialUpdatedInventaire = new Inventaire();
        partialUpdatedInventaire.setId(inventaire.getId());

        partialUpdatedInventaire.nomFichier(UPDATED_NOM_FICHIER).dateImport(UPDATED_DATE_IMPORT);

        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInventaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInventaire))
            )
            .andExpect(status().isOk());

        // Validate the Inventaire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInventaireUpdatableFieldsEquals(partialUpdatedInventaire, getPersistedInventaire(partialUpdatedInventaire));
    }

    @Test
    @Transactional
    void patchNonExistingInventaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventaire.setId(longCount.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inventaireDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInventaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventaire.setId(longCount.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inventaireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInventaire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inventaire.setId(longCount.incrementAndGet());

        // Create the Inventaire
        InventaireDTO inventaireDTO = inventaireMapper.toDto(inventaire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInventaireMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inventaireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inventaire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInventaire() throws Exception {
        // Initialize the database
        insertedInventaire = inventaireRepository.saveAndFlush(inventaire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inventaire
        restInventaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, inventaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inventaireRepository.count();
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

    protected Inventaire getPersistedInventaire(Inventaire inventaire) {
        return inventaireRepository.findById(inventaire.getId()).orElseThrow();
    }

    protected void assertPersistedInventaireToMatchAllProperties(Inventaire expectedInventaire) {
        assertInventaireAllPropertiesEquals(expectedInventaire, getPersistedInventaire(expectedInventaire));
    }

    protected void assertPersistedInventaireToMatchUpdatableProperties(Inventaire expectedInventaire) {
        assertInventaireAllUpdatablePropertiesEquals(expectedInventaire, getPersistedInventaire(expectedInventaire));
    }
}
