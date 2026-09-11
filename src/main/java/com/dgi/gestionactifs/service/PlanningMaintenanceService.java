package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.PlanningMaintenanceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.PlanningMaintenance}.
 */
public interface PlanningMaintenanceService {
    /**
     * Save a planningMaintenance.
     *
     * @param planningMaintenanceDTO the entity to save.
     * @return the persisted entity.
     */
    PlanningMaintenanceDTO save(PlanningMaintenanceDTO planningMaintenanceDTO);

    /**
     * Updates a planningMaintenance.
     *
     * @param planningMaintenanceDTO the entity to update.
     * @return the persisted entity.
     */
    PlanningMaintenanceDTO update(PlanningMaintenanceDTO planningMaintenanceDTO);

    /**
     * Partially updates a planningMaintenance.
     *
     * @param planningMaintenanceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlanningMaintenanceDTO> partialUpdate(PlanningMaintenanceDTO planningMaintenanceDTO);

    /**
     * Get all the planningMaintenances.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanningMaintenanceDTO> findAll(Pageable pageable);

    /**
     * Get all the planningMaintenances with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlanningMaintenanceDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" planningMaintenance.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlanningMaintenanceDTO> findOne(Long id);

    /**
     * Delete the "id" planningMaintenance.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
