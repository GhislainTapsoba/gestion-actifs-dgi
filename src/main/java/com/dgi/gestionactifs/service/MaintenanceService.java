package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.MaintenanceDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Maintenance}.
 */
public interface MaintenanceService {
    /**
     * Save a maintenance.
     *
     * @param maintenanceDTO the entity to save.
     * @return the persisted entity.
     */
    MaintenanceDTO save(MaintenanceDTO maintenanceDTO);

    /**
     * Updates a maintenance.
     *
     * @param maintenanceDTO the entity to update.
     * @return the persisted entity.
     */
    MaintenanceDTO update(MaintenanceDTO maintenanceDTO);

    /**
     * Partially updates a maintenance.
     *
     * @param maintenanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaintenanceDTO> partialUpdate(MaintenanceDTO maintenanceDTO);

    /**
     * Get the "id" maintenance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaintenanceDTO> findOne(Long id);

    /**
     * Delete the "id" maintenance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
