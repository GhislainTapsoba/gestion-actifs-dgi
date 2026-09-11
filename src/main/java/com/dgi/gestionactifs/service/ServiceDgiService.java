package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.ServiceDgi}.
 */
public interface ServiceDgiService {
    /**
     * Save a serviceDgi.
     *
     * @param serviceDgiDTO the entity to save.
     * @return the persisted entity.
     */
    ServiceDgiDTO save(ServiceDgiDTO serviceDgiDTO);

    /**
     * Updates a serviceDgi.
     *
     * @param serviceDgiDTO the entity to update.
     * @return the persisted entity.
     */
    ServiceDgiDTO update(ServiceDgiDTO serviceDgiDTO);

    /**
     * Partially updates a serviceDgi.
     *
     * @param serviceDgiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ServiceDgiDTO> partialUpdate(ServiceDgiDTO serviceDgiDTO);

    /**
     * Get the "id" serviceDgi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ServiceDgiDTO> findOne(Long id);

    /**
     * Delete the "id" serviceDgi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
