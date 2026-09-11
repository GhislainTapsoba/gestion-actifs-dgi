package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.AffectationActif;
import com.dgi.gestionactifs.repository.AffectationActifRepository;
import com.dgi.gestionactifs.service.AffectationActifService;
import com.dgi.gestionactifs.service.dto.AffectationActifDTO;
import com.dgi.gestionactifs.service.mapper.AffectationActifMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.AffectationActif}.
 */
@Service
@Transactional
public class AffectationActifServiceImpl implements AffectationActifService {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationActifServiceImpl.class);

    private final AffectationActifRepository affectationActifRepository;

    private final AffectationActifMapper affectationActifMapper;

    public AffectationActifServiceImpl(
        AffectationActifRepository affectationActifRepository,
        AffectationActifMapper affectationActifMapper
    ) {
        this.affectationActifRepository = affectationActifRepository;
        this.affectationActifMapper = affectationActifMapper;
    }

    @Override
    public AffectationActifDTO save(AffectationActifDTO affectationActifDTO) {
        LOG.debug("Request to save AffectationActif : {}", affectationActifDTO);
        AffectationActif affectationActif = affectationActifMapper.toEntity(affectationActifDTO);
        affectationActif = affectationActifRepository.save(affectationActif);
        return affectationActifMapper.toDto(affectationActif);
    }

    @Override
    public AffectationActifDTO update(AffectationActifDTO affectationActifDTO) {
        LOG.debug("Request to update AffectationActif : {}", affectationActifDTO);
        AffectationActif affectationActif = affectationActifMapper.toEntity(affectationActifDTO);
        affectationActif = affectationActifRepository.save(affectationActif);
        return affectationActifMapper.toDto(affectationActif);
    }

    @Override
    public Optional<AffectationActifDTO> partialUpdate(AffectationActifDTO affectationActifDTO) {
        LOG.debug("Request to partially update AffectationActif : {}", affectationActifDTO);

        return affectationActifRepository
            .findById(affectationActifDTO.getId())
            .map(existingAffectationActif -> {
                affectationActifMapper.partialUpdate(existingAffectationActif, affectationActifDTO);

                return existingAffectationActif;
            })
            .map(affectationActifRepository::save)
            .map(affectationActifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AffectationActifDTO> findOne(Long id) {
        LOG.debug("Request to get AffectationActif : {}", id);
        return affectationActifRepository.findById(id).map(affectationActifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AffectationActif : {}", id);
        affectationActifRepository.deleteById(id);
    }
}
