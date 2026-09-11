package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.HistoriqueActionDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.HistoriqueAction}.
 */
public interface HistoriqueActionService {
    /**
     * Save a historiqueAction.
     *
     * @param historiqueActionDTO the entity to save.
     * @return the persisted entity.
     */
    HistoriqueActionDTO save(HistoriqueActionDTO historiqueActionDTO);

    /**
     * Updates a historiqueAction.
     *
     * @param historiqueActionDTO the entity to update.
     * @return the persisted entity.
     */
    HistoriqueActionDTO update(HistoriqueActionDTO historiqueActionDTO);

    /**
     * Partially updates a historiqueAction.
     *
     * @param historiqueActionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<HistoriqueActionDTO> partialUpdate(HistoriqueActionDTO historiqueActionDTO);

    /**
     * Get the "id" historiqueAction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<HistoriqueActionDTO> findOne(Long id);

    /**
     * Delete the "id" historiqueAction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
