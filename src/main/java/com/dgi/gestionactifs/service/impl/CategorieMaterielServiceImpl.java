package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.CategorieMateriel;
import com.dgi.gestionactifs.repository.CategorieMaterielRepository;
import com.dgi.gestionactifs.service.CategorieMaterielService;
import com.dgi.gestionactifs.service.dto.CategorieMaterielDTO;
import com.dgi.gestionactifs.service.mapper.CategorieMaterielMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.CategorieMateriel}.
 */
@Service
@Transactional
public class CategorieMaterielServiceImpl implements CategorieMaterielService {

    private static final Logger LOG = LoggerFactory.getLogger(CategorieMaterielServiceImpl.class);

    private final CategorieMaterielRepository categorieMaterielRepository;

    private final CategorieMaterielMapper categorieMaterielMapper;

    public CategorieMaterielServiceImpl(
        CategorieMaterielRepository categorieMaterielRepository,
        CategorieMaterielMapper categorieMaterielMapper
    ) {
        this.categorieMaterielRepository = categorieMaterielRepository;
        this.categorieMaterielMapper = categorieMaterielMapper;
    }

    @Override
    public CategorieMaterielDTO save(CategorieMaterielDTO categorieMaterielDTO) {
        LOG.debug("Request to save CategorieMateriel : {}", categorieMaterielDTO);
        CategorieMateriel categorieMateriel = categorieMaterielMapper.toEntity(categorieMaterielDTO);
        categorieMateriel = categorieMaterielRepository.save(categorieMateriel);
        return categorieMaterielMapper.toDto(categorieMateriel);
    }

    @Override
    public CategorieMaterielDTO update(CategorieMaterielDTO categorieMaterielDTO) {
        LOG.debug("Request to update CategorieMateriel : {}", categorieMaterielDTO);
        CategorieMateriel categorieMateriel = categorieMaterielMapper.toEntity(categorieMaterielDTO);
        categorieMateriel = categorieMaterielRepository.save(categorieMateriel);
        return categorieMaterielMapper.toDto(categorieMateriel);
    }

    @Override
    public Optional<CategorieMaterielDTO> partialUpdate(CategorieMaterielDTO categorieMaterielDTO) {
        LOG.debug("Request to partially update CategorieMateriel : {}", categorieMaterielDTO);

        return categorieMaterielRepository
            .findById(categorieMaterielDTO.getId())
            .map(existingCategorieMateriel -> {
                categorieMaterielMapper.partialUpdate(existingCategorieMateriel, categorieMaterielDTO);

                return existingCategorieMateriel;
            })
            .map(categorieMaterielRepository::save)
            .map(categorieMaterielMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategorieMaterielDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CategorieMateriels");
        return categorieMaterielRepository.findAll(pageable).map(categorieMaterielMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategorieMaterielDTO> findOne(Long id) {
        LOG.debug("Request to get CategorieMateriel : {}", id);
        return categorieMaterielRepository.findById(id).map(categorieMaterielMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete CategorieMateriel : {}", id);
        categorieMaterielRepository.deleteById(id);
    }
}
