package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.RecensementDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Recensement}.
 */
public interface RecensementService {
    /**
     * Save a recensement.
     *
     * @param recensementDTO the entity to save.
     * @return the persisted entity.
     */
    RecensementDTO save(RecensementDTO recensementDTO);

    /**
     * Updates a recensement.
     *
     * @param recensementDTO the entity to update.
     * @return the persisted entity.
     */
    RecensementDTO update(RecensementDTO recensementDTO);

    /**
     * Partially updates a recensement.
     *
     * @param recensementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RecensementDTO> partialUpdate(RecensementDTO recensementDTO);

    /**
     * Get the "id" recensement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RecensementDTO> findOne(Long id);

    /**
     * Delete the "id" recensement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
