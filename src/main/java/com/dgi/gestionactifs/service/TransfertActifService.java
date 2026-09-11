package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.TransfertActifDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.TransfertActif}.
 */
public interface TransfertActifService {
    /**
     * Save a transfertActif.
     *
     * @param transfertActifDTO the entity to save.
     * @return the persisted entity.
     */
    TransfertActifDTO save(TransfertActifDTO transfertActifDTO);

    /**
     * Updates a transfertActif.
     *
     * @param transfertActifDTO the entity to update.
     * @return the persisted entity.
     */
    TransfertActifDTO update(TransfertActifDTO transfertActifDTO);

    /**
     * Partially updates a transfertActif.
     *
     * @param transfertActifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransfertActifDTO> partialUpdate(TransfertActifDTO transfertActifDTO);

    /**
     * Get the "id" transfertActif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransfertActifDTO> findOne(Long id);

    /**
     * Delete the "id" transfertActif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
