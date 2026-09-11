package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.CategorieMaterielDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.CategorieMateriel}.
 */
public interface CategorieMaterielService {
    /**
     * Save a categorieMateriel.
     *
     * @param categorieMaterielDTO the entity to save.
     * @return the persisted entity.
     */
    CategorieMaterielDTO save(CategorieMaterielDTO categorieMaterielDTO);

    /**
     * Updates a categorieMateriel.
     *
     * @param categorieMaterielDTO the entity to update.
     * @return the persisted entity.
     */
    CategorieMaterielDTO update(CategorieMaterielDTO categorieMaterielDTO);

    /**
     * Partially updates a categorieMateriel.
     *
     * @param categorieMaterielDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CategorieMaterielDTO> partialUpdate(CategorieMaterielDTO categorieMaterielDTO);

    /**
     * Get all the categorieMateriels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CategorieMaterielDTO> findAll(Pageable pageable);

    /**
     * Get the "id" categorieMateriel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CategorieMaterielDTO> findOne(Long id);

    /**
     * Delete the "id" categorieMateriel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
