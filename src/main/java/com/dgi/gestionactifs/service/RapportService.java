package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.service.dto.RapportDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.dgi.gestionactifs.domain.Rapport}.
 */
public interface RapportService {
    RapportDTO save(RapportDTO rapportDTO);

    RapportDTO update(RapportDTO rapportDTO);

    Page<RapportDTO> findAll(Pageable pageable);

    List<RapportDTO> findAll();

    Page<RapportDTO> findByGenerePar(String username, Pageable pageable);

    Page<RapportDTO> findByTypeRapport(String typeRapport, Pageable pageable);

    RapportDTO findOne(Long id);

    void delete(Long id);
}
