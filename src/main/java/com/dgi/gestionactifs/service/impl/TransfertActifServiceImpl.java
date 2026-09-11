package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.TransfertActif;
import com.dgi.gestionactifs.repository.TransfertActifRepository;
import com.dgi.gestionactifs.service.TransfertActifService;
import com.dgi.gestionactifs.service.dto.TransfertActifDTO;
import com.dgi.gestionactifs.service.mapper.TransfertActifMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.TransfertActif}.
 */
@Service
@Transactional
public class TransfertActifServiceImpl implements TransfertActifService {

    private static final Logger LOG = LoggerFactory.getLogger(TransfertActifServiceImpl.class);

    private final TransfertActifRepository transfertActifRepository;

    private final TransfertActifMapper transfertActifMapper;

    public TransfertActifServiceImpl(TransfertActifRepository transfertActifRepository, TransfertActifMapper transfertActifMapper) {
        this.transfertActifRepository = transfertActifRepository;
        this.transfertActifMapper = transfertActifMapper;
    }

    @Override
    public TransfertActifDTO save(TransfertActifDTO transfertActifDTO) {
        LOG.debug("Request to save TransfertActif : {}", transfertActifDTO);
        TransfertActif transfertActif = transfertActifMapper.toEntity(transfertActifDTO);
        transfertActif = transfertActifRepository.save(transfertActif);
        return transfertActifMapper.toDto(transfertActif);
    }

    @Override
    public TransfertActifDTO update(TransfertActifDTO transfertActifDTO) {
        LOG.debug("Request to update TransfertActif : {}", transfertActifDTO);
        TransfertActif transfertActif = transfertActifMapper.toEntity(transfertActifDTO);
        transfertActif = transfertActifRepository.save(transfertActif);
        return transfertActifMapper.toDto(transfertActif);
    }

    @Override
    public Optional<TransfertActifDTO> partialUpdate(TransfertActifDTO transfertActifDTO) {
        LOG.debug("Request to partially update TransfertActif : {}", transfertActifDTO);

        return transfertActifRepository
            .findById(transfertActifDTO.getId())
            .map(existingTransfertActif -> {
                transfertActifMapper.partialUpdate(existingTransfertActif, transfertActifDTO);

                return existingTransfertActif;
            })
            .map(transfertActifRepository::save)
            .map(transfertActifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransfertActifDTO> findOne(Long id) {
        LOG.debug("Request to get TransfertActif : {}", id);
        return transfertActifRepository.findById(id).map(transfertActifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete TransfertActif : {}", id);
        transfertActifRepository.deleteById(id);
    }
}
