package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.ActifDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Actif}.
 */
public interface ActifService {
    /**
     * Save a actif.
     *
     * @param actifDTO the entity to save.
     * @return the persisted entity.
     */
    ActifDTO save(ActifDTO actifDTO);

    /**
     * Updates a actif.
     *
     * @param actifDTO the entity to update.
     * @return the persisted entity.
     */
    ActifDTO update(ActifDTO actifDTO);

    /**
     * Partially updates a actif.
     *
     * @param actifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActifDTO> partialUpdate(ActifDTO actifDTO);

    /**
     * Get the "id" actif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActifDTO> findOne(Long id);

    /**
     * Delete the "id" actif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
