package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Rapport;
import com.dgi.gestionactifs.repository.RapportRepository;
import com.dgi.gestionactifs.service.RapportService;
import com.dgi.gestionactifs.service.dto.RapportDTO;
import com.dgi.gestionactifs.service.mapper.RapportMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Rapport}.
 */
@Service
@Transactional
public class RapportServiceImpl implements RapportService {

    private static final Logger LOG = LoggerFactory.getLogger(RapportServiceImpl.class);

    private final RapportRepository rapportRepository;

    private final RapportMapper rapportMapper;

    public RapportServiceImpl(RapportRepository rapportRepository, RapportMapper rapportMapper) {
        this.rapportRepository = rapportRepository;
        this.rapportMapper = rapportMapper;
    }

    @Override
    public RapportDTO save(RapportDTO rapportDTO) {
        LOG.debug("Request to save Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        if (rapport.getDateGeneration() == null) {
            rapport.setDateGeneration(Instant.now());
        }
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDto(rapport);
    }

    @Override
    public RapportDTO update(RapportDTO rapportDTO) {
        LOG.debug("Request to update Rapport : {}", rapportDTO);
        Rapport rapport = rapportMapper.toEntity(rapportDTO);
        rapport = rapportRepository.save(rapport);
        return rapportMapper.toDto(rapport);
    }

    @Override
    public Page<RapportDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Rapports");
        return rapportRepository.findAll(pageable).map(rapportMapper::toDto);
    }

    @Override
    public List<RapportDTO> findAll() {
        LOG.debug("Request to get all Rapports");
        return rapportRepository.findAll().stream().map(rapportMapper::toDto).toList();
    }

    @Override
    public Page<RapportDTO> findByGenerePar(String username, Pageable pageable) {
        LOG.debug("Request to get Rapports by genere par: {}", username);
        return rapportRepository.findByGenerePar(username, pageable).map(rapportMapper::toDto);
    }

    @Override
    public Page<RapportDTO> findByTypeRapport(String typeRapport, Pageable pageable) {
        LOG.debug("Request to get Rapports by type rapport: {}", typeRapport);
        return rapportRepository.findByTypeRapport(typeRapport, pageable).map(rapportMapper::toDto);
    }

    @Override
    public RapportDTO findOne(Long id) {
        LOG.debug("Request to get Rapport : {}", id);
        Optional<Rapport> rapport = rapportRepository.findById(id);
        return rapport.map(rapportMapper::toDto).orElse(null);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Rapport : {}", id);
        rapportRepository.deleteById(id);
    }
}
