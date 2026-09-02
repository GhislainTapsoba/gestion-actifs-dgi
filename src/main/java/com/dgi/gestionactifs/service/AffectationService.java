package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.AffectationDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Affectation}.
 */
public interface AffectationService {
    /**
     * Save a affectation.
     *
     * @param affectationDTO the entity to save.
     * @return the persisted entity.
     */
    AffectationDTO save(AffectationDTO affectationDTO);

    /**
     * Updates a affectation.
     *
     * @param affectationDTO the entity to update.
     * @return the persisted entity.
     */
    AffectationDTO update(AffectationDTO affectationDTO);

    /**
     * Partially updates a affectation.
     *
     * @param affectationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AffectationDTO> partialUpdate(AffectationDTO affectationDTO);

    /**
     * Get the "id" affectation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AffectationDTO> findOne(Long id);

    /**
     * Delete the "id" affectation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
