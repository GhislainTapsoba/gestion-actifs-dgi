package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.repository.AffectationRepository;
import com.dgi.gestionactifs.service.AffectationService;
import com.dgi.gestionactifs.service.dto.AffectationDTO;
import com.dgi.gestionactifs.service.mapper.AffectationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Affectation}.
 */
@Service
@Transactional
public class AffectationServiceImpl implements AffectationService {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationServiceImpl.class);

    private final AffectationRepository affectationRepository;

    private final AffectationMapper affectationMapper;

    public AffectationServiceImpl(AffectationRepository affectationRepository, AffectationMapper affectationMapper) {
        this.affectationRepository = affectationRepository;
        this.affectationMapper = affectationMapper;
    }

    @Override
    public AffectationDTO save(AffectationDTO affectationDTO) {
        LOG.debug("Request to save Affectation : {}", affectationDTO);
        Affectation affectation = affectationMapper.toEntity(affectationDTO);
        affectation = affectationRepository.save(affectation);
        return affectationMapper.toDto(affectation);
    }

    @Override
    public AffectationDTO update(AffectationDTO affectationDTO) {
        LOG.debug("Request to update Affectation : {}", affectationDTO);
        Affectation affectation = affectationMapper.toEntity(affectationDTO);
        affectation = affectationRepository.save(affectation);
        return affectationMapper.toDto(affectation);
    }

    @Override
    public Optional<AffectationDTO> partialUpdate(AffectationDTO affectationDTO) {
        LOG.debug("Request to partially update Affectation : {}", affectationDTO);

        return affectationRepository
            .findById(affectationDTO.getId())
            .map(existingAffectation -> {
                affectationMapper.partialUpdate(existingAffectation, affectationDTO);

                return existingAffectation;
            })
            .map(affectationRepository::save)
            .map(affectationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AffectationDTO> findOne(Long id) {
        LOG.debug("Request to get Affectation : {}", id);
        return affectationRepository.findById(id).map(affectationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Affectation : {}", id);
        affectationRepository.deleteById(id);
    }
}
