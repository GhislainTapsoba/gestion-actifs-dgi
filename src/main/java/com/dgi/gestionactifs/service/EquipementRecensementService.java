package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.EquipementRecensementDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.EquipementRecensement}.
 */
public interface EquipementRecensementService {
    /**
     * Save a equipementRecensement.
     *
     * @param equipementRecensementDTO the entity to save.
     * @return the persisted entity.
     */
    EquipementRecensementDTO save(EquipementRecensementDTO equipementRecensementDTO);

    /**
     * Updates a equipementRecensement.
     *
     * @param equipementRecensementDTO the entity to update.
     * @return the persisted entity.
     */
    EquipementRecensementDTO update(EquipementRecensementDTO equipementRecensementDTO);

    /**
     * Partially updates a equipementRecensement.
     *
     * @param equipementRecensementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EquipementRecensementDTO> partialUpdate(EquipementRecensementDTO equipementRecensementDTO);

    /**
     * Get the "id" equipementRecensement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EquipementRecensementDTO> findOne(Long id);

    /**
     * Delete the "id" equipementRecensement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
