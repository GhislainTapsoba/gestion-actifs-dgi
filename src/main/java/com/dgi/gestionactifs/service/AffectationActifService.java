package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.AffectationActifDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.AffectationActif}.
 */
public interface AffectationActifService {
    /**
     * Save a affectationActif.
     *
     * @param affectationActifDTO the entity to save.
     * @return the persisted entity.
     */
    AffectationActifDTO save(AffectationActifDTO affectationActifDTO);

    /**
     * Updates a affectationActif.
     *
     * @param affectationActifDTO the entity to update.
     * @return the persisted entity.
     */
    AffectationActifDTO update(AffectationActifDTO affectationActifDTO);

    /**
     * Partially updates a affectationActif.
     *
     * @param affectationActifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AffectationActifDTO> partialUpdate(AffectationActifDTO affectationActifDTO);

    /**
     * Get the "id" affectationActif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AffectationActifDTO> findOne(Long id);

    /**
     * Delete the "id" affectationActif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
