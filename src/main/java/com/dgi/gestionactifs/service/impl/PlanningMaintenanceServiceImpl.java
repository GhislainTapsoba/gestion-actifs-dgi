package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.PlanningMaintenance;
import com.dgi.gestionactifs.repository.PlanningMaintenanceRepository;
import com.dgi.gestionactifs.service.PlanningMaintenanceService;
import com.dgi.gestionactifs.service.dto.PlanningMaintenanceDTO;
import com.dgi.gestionactifs.service.mapper.PlanningMaintenanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.PlanningMaintenance}.
 */
@Service
@Transactional
public class PlanningMaintenanceServiceImpl implements PlanningMaintenanceService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanningMaintenanceServiceImpl.class);

    private final PlanningMaintenanceRepository planningMaintenanceRepository;

    private final PlanningMaintenanceMapper planningMaintenanceMapper;

    public PlanningMaintenanceServiceImpl(
        PlanningMaintenanceRepository planningMaintenanceRepository,
        PlanningMaintenanceMapper planningMaintenanceMapper
    ) {
        this.planningMaintenanceRepository = planningMaintenanceRepository;
        this.planningMaintenanceMapper = planningMaintenanceMapper;
    }

    @Override
    public PlanningMaintenanceDTO save(PlanningMaintenanceDTO planningMaintenanceDTO) {
        LOG.debug("Request to save PlanningMaintenance : {}", planningMaintenanceDTO);
        PlanningMaintenance planningMaintenance = planningMaintenanceMapper.toEntity(planningMaintenanceDTO);
        planningMaintenance = planningMaintenanceRepository.save(planningMaintenance);
        return planningMaintenanceMapper.toDto(planningMaintenance);
    }

    @Override
    public PlanningMaintenanceDTO update(PlanningMaintenanceDTO planningMaintenanceDTO) {
        LOG.debug("Request to update PlanningMaintenance : {}", planningMaintenanceDTO);
        PlanningMaintenance planningMaintenance = planningMaintenanceMapper.toEntity(planningMaintenanceDTO);
        planningMaintenance = planningMaintenanceRepository.save(planningMaintenance);
        return planningMaintenanceMapper.toDto(planningMaintenance);
    }

    @Override
    public Optional<PlanningMaintenanceDTO> partialUpdate(PlanningMaintenanceDTO planningMaintenanceDTO) {
        LOG.debug("Request to partially update PlanningMaintenance : {}", planningMaintenanceDTO);

        return planningMaintenanceRepository
            .findById(planningMaintenanceDTO.getId())
            .map(existingPlanningMaintenance -> {
                planningMaintenanceMapper.partialUpdate(existingPlanningMaintenance, planningMaintenanceDTO);

                return existingPlanningMaintenance;
            })
            .map(planningMaintenanceRepository::save)
            .map(planningMaintenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlanningMaintenanceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlanningMaintenances");
        return planningMaintenanceRepository.findAll(pageable).map(planningMaintenanceMapper::toDto);
    }

    public Page<PlanningMaintenanceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return planningMaintenanceRepository.findAllWithEagerRelationships(pageable).map(planningMaintenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanningMaintenanceDTO> findOne(Long id) {
        LOG.debug("Request to get PlanningMaintenance : {}", id);
        return planningMaintenanceRepository.findOneWithEagerRelationships(id).map(planningMaintenanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete PlanningMaintenance : {}", id);
        planningMaintenanceRepository.deleteById(id);
    }
}
