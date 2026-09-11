package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Recensement;
import com.dgi.gestionactifs.repository.RecensementRepository;
import com.dgi.gestionactifs.service.RecensementService;
import com.dgi.gestionactifs.service.dto.RecensementDTO;
import com.dgi.gestionactifs.service.mapper.RecensementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Recensement}.
 */
@Service
@Transactional
public class RecensementServiceImpl implements RecensementService {

    private static final Logger LOG = LoggerFactory.getLogger(RecensementServiceImpl.class);

    private final RecensementRepository recensementRepository;

    private final RecensementMapper recensementMapper;

    public RecensementServiceImpl(RecensementRepository recensementRepository, RecensementMapper recensementMapper) {
        this.recensementRepository = recensementRepository;
        this.recensementMapper = recensementMapper;
    }

    @Override
    public RecensementDTO save(RecensementDTO recensementDTO) {
        LOG.debug("Request to save Recensement : {}", recensementDTO);
        Recensement recensement = recensementMapper.toEntity(recensementDTO);
        recensement = recensementRepository.save(recensement);
        return recensementMapper.toDto(recensement);
    }

    @Override
    public RecensementDTO update(RecensementDTO recensementDTO) {
        LOG.debug("Request to update Recensement : {}", recensementDTO);
        Recensement recensement = recensementMapper.toEntity(recensementDTO);
        recensement = recensementRepository.save(recensement);
        return recensementMapper.toDto(recensement);
    }

    @Override
    public Optional<RecensementDTO> partialUpdate(RecensementDTO recensementDTO) {
        LOG.debug("Request to partially update Recensement : {}", recensementDTO);

        return recensementRepository
            .findById(recensementDTO.getId())
            .map(existingRecensement -> {
                recensementMapper.partialUpdate(existingRecensement, recensementDTO);

                return existingRecensement;
            })
            .map(recensementRepository::save)
            .map(recensementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecensementDTO> findOne(Long id) {
        LOG.debug("Request to get Recensement : {}", id);
        return recensementRepository.findById(id).map(recensementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Recensement : {}", id);
        recensementRepository.deleteById(id);
    }
}
