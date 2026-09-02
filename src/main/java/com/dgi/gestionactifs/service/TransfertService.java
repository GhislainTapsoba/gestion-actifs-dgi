package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.TransfertDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Transfert}.
 */
public interface TransfertService {
    /**
     * Save a transfert.
     *
     * @param transfertDTO the entity to save.
     * @return the persisted entity.
     */
    TransfertDTO save(TransfertDTO transfertDTO);

    /**
     * Updates a transfert.
     *
     * @param transfertDTO the entity to update.
     * @return the persisted entity.
     */
    TransfertDTO update(TransfertDTO transfertDTO);

    /**
     * Partially updates a transfert.
     *
     * @param transfertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TransfertDTO> partialUpdate(TransfertDTO transfertDTO);

    /**
     * Get the "id" transfert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TransfertDTO> findOne(Long id);

    /**
     * Delete the "id" transfert.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
