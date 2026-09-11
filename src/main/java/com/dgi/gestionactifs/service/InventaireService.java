package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.InventaireDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Inventaire}.
 */
public interface InventaireService {
    /**
     * Save a inventaire.
     *
     * @param inventaireDTO the entity to save.
     * @return the persisted entity.
     */
    InventaireDTO save(InventaireDTO inventaireDTO);

    /**
     * Updates a inventaire.
     *
     * @param inventaireDTO the entity to update.
     * @return the persisted entity.
     */
    InventaireDTO update(InventaireDTO inventaireDTO);

    /**
     * Partially updates a inventaire.
     *
     * @param inventaireDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InventaireDTO> partialUpdate(InventaireDTO inventaireDTO);

    /**
     * Get all the inventaires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<InventaireDTO> findAll(Pageable pageable);

    /**
     * Get the "id" inventaire.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InventaireDTO> findOne(Long id);

    /**
     * Delete the "id" inventaire.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
