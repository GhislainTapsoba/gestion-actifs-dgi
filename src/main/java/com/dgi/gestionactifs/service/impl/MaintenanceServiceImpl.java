package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Maintenance;
import com.dgi.gestionactifs.repository.MaintenanceRepository;
import com.dgi.gestionactifs.service.MaintenanceService;
import com.dgi.gestionactifs.service.dto.MaintenanceDTO;
import com.dgi.gestionactifs.service.mapper.MaintenanceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Maintenance}.
 */
@Service
@Transactional
public class MaintenanceServiceImpl implements MaintenanceService {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceServiceImpl.class);

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository, MaintenanceMapper maintenanceMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    @Override
    public MaintenanceDTO save(MaintenanceDTO maintenanceDTO) {
        LOG.debug("Request to save Maintenance : {}", maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);
        maintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    public MaintenanceDTO update(MaintenanceDTO maintenanceDTO) {
        LOG.debug("Request to update Maintenance : {}", maintenanceDTO);
        Maintenance maintenance = maintenanceMapper.toEntity(maintenanceDTO);
        maintenance = maintenanceRepository.save(maintenance);
        return maintenanceMapper.toDto(maintenance);
    }

    @Override
    public Optional<MaintenanceDTO> partialUpdate(MaintenanceDTO maintenanceDTO) {
        LOG.debug("Request to partially update Maintenance : {}", maintenanceDTO);

        return maintenanceRepository
            .findById(maintenanceDTO.getId())
            .map(existingMaintenance -> {
                maintenanceMapper.partialUpdate(existingMaintenance, maintenanceDTO);

                return existingMaintenance;
            })
            .map(maintenanceRepository::save)
            .map(maintenanceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaintenanceDTO> findOne(Long id) {
        LOG.debug("Request to get Maintenance : {}", id);
        return maintenanceRepository.findById(id).map(maintenanceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Maintenance : {}", id);
        maintenanceRepository.deleteById(id);
    }
}
