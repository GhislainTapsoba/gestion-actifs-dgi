package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.InterventionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Intervention}.
 */
public interface InterventionService {
    /**
     * Save a intervention.
     *
     * @param interventionDTO the entity to save.
     * @return the persisted entity.
     */
    InterventionDTO save(InterventionDTO interventionDTO);

    /**
     * Updates a intervention.
     *
     * @param interventionDTO the entity to update.
     * @return the persisted entity.
     */
    InterventionDTO update(InterventionDTO interventionDTO);

    /**
     * Partially updates a intervention.
     *
     * @param interventionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InterventionDTO> partialUpdate(InterventionDTO interventionDTO);

    /**
     * Get the "id" intervention.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InterventionDTO> findOne(Long id);

    /**
     * Delete the "id" intervention.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
