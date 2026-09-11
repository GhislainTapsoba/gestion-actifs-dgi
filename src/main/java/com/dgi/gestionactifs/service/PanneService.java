package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.PanneDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Panne}.
 */
public interface PanneService {
    /**
     * Save a panne.
     *
     * @param panneDTO the entity to save.
     * @return the persisted entity.
     */
    PanneDTO save(PanneDTO panneDTO);

    /**
     * Updates a panne.
     *
     * @param panneDTO the entity to update.
     * @return the persisted entity.
     */
    PanneDTO update(PanneDTO panneDTO);

    /**
     * Partially updates a panne.
     *
     * @param panneDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PanneDTO> partialUpdate(PanneDTO panneDTO);

    /**
     * Get the "id" panne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PanneDTO> findOne(Long id);

    /**
     * Delete the "id" panne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
