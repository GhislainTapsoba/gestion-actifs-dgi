package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Bordereau;
import com.dgi.gestionactifs.repository.BordereauRepository;
import com.dgi.gestionactifs.service.BordereauService;
import com.dgi.gestionactifs.service.dto.BordereauDTO;
import com.dgi.gestionactifs.service.mapper.BordereauMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Bordereau}.
 */
@Service
@Transactional
public class BordereauServiceImpl implements BordereauService {

    private static final Logger LOG = LoggerFactory.getLogger(BordereauServiceImpl.class);

    private final BordereauRepository bordereauRepository;

    private final BordereauMapper bordereauMapper;

    public BordereauServiceImpl(BordereauRepository bordereauRepository, BordereauMapper bordereauMapper) {
        this.bordereauRepository = bordereauRepository;
        this.bordereauMapper = bordereauMapper;
    }

    @Override
    public BordereauDTO save(BordereauDTO bordereauDTO) {
        LOG.debug("Request to save Bordereau : {}", bordereauDTO);
        Bordereau bordereau = bordereauMapper.toEntity(bordereauDTO);
        bordereau = bordereauRepository.save(bordereau);
        return bordereauMapper.toDto(bordereau);
    }

    @Override
    public BordereauDTO update(BordereauDTO bordereauDTO) {
        LOG.debug("Request to update Bordereau : {}", bordereauDTO);
        Bordereau bordereau = bordereauMapper.toEntity(bordereauDTO);
        bordereau = bordereauRepository.save(bordereau);
        return bordereauMapper.toDto(bordereau);
    }

    @Override
    public Optional<BordereauDTO> partialUpdate(BordereauDTO bordereauDTO) {
        LOG.debug("Request to partially update Bordereau : {}", bordereauDTO);

        return bordereauRepository
            .findById(bordereauDTO.getId())
            .map(existingBordereau -> {
                bordereauMapper.partialUpdate(existingBordereau, bordereauDTO);

                return existingBordereau;
            })
            .map(bordereauRepository::save)
            .map(bordereauMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BordereauDTO> findOne(Long id) {
        LOG.debug("Request to get Bordereau : {}", id);
        return bordereauRepository.findById(id).map(bordereauMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bordereau : {}", id);
        bordereauRepository.deleteById(id);
    }
}
