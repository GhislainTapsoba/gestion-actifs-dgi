package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Intervention;
import com.dgi.gestionactifs.repository.InterventionRepository;
import com.dgi.gestionactifs.service.InterventionService;
import com.dgi.gestionactifs.service.dto.InterventionDTO;
import com.dgi.gestionactifs.service.mapper.InterventionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Intervention}.
 */
@Service
@Transactional
public class InterventionServiceImpl implements InterventionService {

    private static final Logger LOG = LoggerFactory.getLogger(InterventionServiceImpl.class);

    private final InterventionRepository interventionRepository;

    private final InterventionMapper interventionMapper;

    public InterventionServiceImpl(InterventionRepository interventionRepository, InterventionMapper interventionMapper) {
        this.interventionRepository = interventionRepository;
        this.interventionMapper = interventionMapper;
    }

    @Override
    public InterventionDTO save(InterventionDTO interventionDTO) {
        LOG.debug("Request to save Intervention : {}", interventionDTO);
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention = interventionRepository.save(intervention);
        return interventionMapper.toDto(intervention);
    }

    @Override
    public InterventionDTO update(InterventionDTO interventionDTO) {
        LOG.debug("Request to update Intervention : {}", interventionDTO);
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention = interventionRepository.save(intervention);
        return interventionMapper.toDto(intervention);
    }

    @Override
    public Optional<InterventionDTO> partialUpdate(InterventionDTO interventionDTO) {
        LOG.debug("Request to partially update Intervention : {}", interventionDTO);

        return interventionRepository
            .findById(interventionDTO.getId())
            .map(existingIntervention -> {
                interventionMapper.partialUpdate(existingIntervention, interventionDTO);

                return existingIntervention;
            })
            .map(interventionRepository::save)
            .map(interventionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InterventionDTO> findOne(Long id) {
        LOG.debug("Request to get Intervention : {}", id);
        return interventionRepository.findById(id).map(interventionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Intervention : {}", id);
        interventionRepository.deleteById(id);
    }
}
