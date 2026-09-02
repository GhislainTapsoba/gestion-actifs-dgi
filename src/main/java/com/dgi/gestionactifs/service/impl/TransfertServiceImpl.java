package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.repository.TransfertRepository;
import com.dgi.gestionactifs.service.TransfertService;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.mapper.TransfertMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Transfert}.
 */
@Service
@Transactional
public class TransfertServiceImpl implements TransfertService {

    private static final Logger LOG = LoggerFactory.getLogger(TransfertServiceImpl.class);

    private final TransfertRepository transfertRepository;

    private final TransfertMapper transfertMapper;

    public TransfertServiceImpl(TransfertRepository transfertRepository, TransfertMapper transfertMapper) {
        this.transfertRepository = transfertRepository;
        this.transfertMapper = transfertMapper;
    }

    @Override
    public TransfertDTO save(TransfertDTO transfertDTO) {
        LOG.debug("Request to save Transfert : {}", transfertDTO);
        Transfert transfert = transfertMapper.toEntity(transfertDTO);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    @Override
    public TransfertDTO update(TransfertDTO transfertDTO) {
        LOG.debug("Request to update Transfert : {}", transfertDTO);
        Transfert transfert = transfertMapper.toEntity(transfertDTO);
        transfert = transfertRepository.save(transfert);
        return transfertMapper.toDto(transfert);
    }

    @Override
    public Optional<TransfertDTO> partialUpdate(TransfertDTO transfertDTO) {
        LOG.debug("Request to partially update Transfert : {}", transfertDTO);

        return transfertRepository
            .findById(transfertDTO.getId())
            .map(existingTransfert -> {
                transfertMapper.partialUpdate(existingTransfert, transfertDTO);

                return existingTransfert;
            })
            .map(transfertRepository::save)
            .map(transfertMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TransfertDTO> findOne(Long id) {
        LOG.debug("Request to get Transfert : {}", id);
        return transfertRepository.findById(id).map(transfertMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Transfert : {}", id);
        transfertRepository.deleteById(id);
    }
}
