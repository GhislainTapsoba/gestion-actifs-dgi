package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Inventaire;
import com.dgi.gestionactifs.repository.InventaireRepository;
import com.dgi.gestionactifs.service.InventaireService;
import com.dgi.gestionactifs.service.dto.InventaireDTO;
import com.dgi.gestionactifs.service.mapper.InventaireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Inventaire}.
 */
@Service
@Transactional
public class InventaireServiceImpl implements InventaireService {

    private static final Logger LOG = LoggerFactory.getLogger(InventaireServiceImpl.class);

    private final InventaireRepository inventaireRepository;

    private final InventaireMapper inventaireMapper;

    public InventaireServiceImpl(InventaireRepository inventaireRepository, InventaireMapper inventaireMapper) {
        this.inventaireRepository = inventaireRepository;
        this.inventaireMapper = inventaireMapper;
    }

    @Override
    public InventaireDTO save(InventaireDTO inventaireDTO) {
        LOG.debug("Request to save Inventaire : {}", inventaireDTO);
        Inventaire inventaire = inventaireMapper.toEntity(inventaireDTO);
        inventaire = inventaireRepository.save(inventaire);
        return inventaireMapper.toDto(inventaire);
    }

    @Override
    public InventaireDTO update(InventaireDTO inventaireDTO) {
        LOG.debug("Request to update Inventaire : {}", inventaireDTO);
        Inventaire inventaire = inventaireMapper.toEntity(inventaireDTO);
        inventaire = inventaireRepository.save(inventaire);
        return inventaireMapper.toDto(inventaire);
    }

    @Override
    public Optional<InventaireDTO> partialUpdate(InventaireDTO inventaireDTO) {
        LOG.debug("Request to partially update Inventaire : {}", inventaireDTO);

        return inventaireRepository
            .findById(inventaireDTO.getId())
            .map(existingInventaire -> {
                inventaireMapper.partialUpdate(existingInventaire, inventaireDTO);

                return existingInventaire;
            })
            .map(inventaireRepository::save)
            .map(inventaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InventaireDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Inventaires");
        return inventaireRepository.findAll(pageable).map(inventaireMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<InventaireDTO> findOne(Long id) {
        LOG.debug("Request to get Inventaire : {}", id);
        return inventaireRepository.findById(id).map(inventaireMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Inventaire : {}", id);
        inventaireRepository.deleteById(id);
    }
}
