package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.EquipementRecensement;
import com.dgi.gestionactifs.repository.EquipementRecensementRepository;
import com.dgi.gestionactifs.service.EquipementRecensementService;
import com.dgi.gestionactifs.service.dto.EquipementRecensementDTO;
import com.dgi.gestionactifs.service.mapper.EquipementRecensementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.EquipementRecensement}.
 */
@Service
@Transactional
public class EquipementRecensementServiceImpl implements EquipementRecensementService {

    private static final Logger LOG = LoggerFactory.getLogger(EquipementRecensementServiceImpl.class);

    private final EquipementRecensementRepository equipementRecensementRepository;

    private final EquipementRecensementMapper equipementRecensementMapper;

    public EquipementRecensementServiceImpl(
        EquipementRecensementRepository equipementRecensementRepository,
        EquipementRecensementMapper equipementRecensementMapper
    ) {
        this.equipementRecensementRepository = equipementRecensementRepository;
        this.equipementRecensementMapper = equipementRecensementMapper;
    }

    @Override
    public EquipementRecensementDTO save(EquipementRecensementDTO equipementRecensementDTO) {
        LOG.debug("Request to save EquipementRecensement : {}", equipementRecensementDTO);
        EquipementRecensement equipementRecensement = equipementRecensementMapper.toEntity(equipementRecensementDTO);
        equipementRecensement = equipementRecensementRepository.save(equipementRecensement);
        return equipementRecensementMapper.toDto(equipementRecensement);
    }

    @Override
    public EquipementRecensementDTO update(EquipementRecensementDTO equipementRecensementDTO) {
        LOG.debug("Request to update EquipementRecensement : {}", equipementRecensementDTO);
        EquipementRecensement equipementRecensement = equipementRecensementMapper.toEntity(equipementRecensementDTO);
        equipementRecensement = equipementRecensementRepository.save(equipementRecensement);
        return equipementRecensementMapper.toDto(equipementRecensement);
    }

    @Override
    public Optional<EquipementRecensementDTO> partialUpdate(EquipementRecensementDTO equipementRecensementDTO) {
        LOG.debug("Request to partially update EquipementRecensement : {}", equipementRecensementDTO);

        return equipementRecensementRepository
            .findById(equipementRecensementDTO.getId())
            .map(existingEquipementRecensement -> {
                equipementRecensementMapper.partialUpdate(existingEquipementRecensement, equipementRecensementDTO);

                return existingEquipementRecensement;
            })
            .map(equipementRecensementRepository::save)
            .map(equipementRecensementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipementRecensementDTO> findOne(Long id) {
        LOG.debug("Request to get EquipementRecensement : {}", id);
        return equipementRecensementRepository.findById(id).map(equipementRecensementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete EquipementRecensement : {}", id);
        equipementRecensementRepository.deleteById(id);
    }
}
