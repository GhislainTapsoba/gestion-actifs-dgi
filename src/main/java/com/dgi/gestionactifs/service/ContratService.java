package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.ContratDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Contrat}.
 */
public interface ContratService {
    /**
     * Save a contrat.
     *
     * @param contratDTO the entity to save.
     * @return the persisted entity.
     */
    ContratDTO save(ContratDTO contratDTO);

    /**
     * Updates a contrat.
     *
     * @param contratDTO the entity to update.
     * @return the persisted entity.
     */
    ContratDTO update(ContratDTO contratDTO);

    /**
     * Partially updates a contrat.
     *
     * @param contratDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContratDTO> partialUpdate(ContratDTO contratDTO);

    /**
     * Get all the contrats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ContratDTO> findAll(Pageable pageable);

    /**
     * Get the "id" contrat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContratDTO> findOne(Long id);

    /**
     * Delete the "id" contrat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
