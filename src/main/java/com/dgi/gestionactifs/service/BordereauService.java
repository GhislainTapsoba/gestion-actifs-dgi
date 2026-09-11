package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.BordereauDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Bordereau}.
 */
public interface BordereauService {
    /**
     * Save a bordereau.
     *
     * @param bordereauDTO the entity to save.
     * @return the persisted entity.
     */
    BordereauDTO save(BordereauDTO bordereauDTO);

    /**
     * Updates a bordereau.
     *
     * @param bordereauDTO the entity to update.
     * @return the persisted entity.
     */
    BordereauDTO update(BordereauDTO bordereauDTO);

    /**
     * Partially updates a bordereau.
     *
     * @param bordereauDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BordereauDTO> partialUpdate(BordereauDTO bordereauDTO);

    /**
     * Get the "id" bordereau.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BordereauDTO> findOne(Long id);

    /**
     * Delete the "id" bordereau.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
