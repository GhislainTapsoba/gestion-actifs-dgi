package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.repository.ActifRepository;
import com.dgi.gestionactifs.service.ActifService;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.mapper.ActifMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Actif}.
 */
@Service
@Transactional
public class ActifServiceImpl implements ActifService {

    private static final Logger LOG = LoggerFactory.getLogger(ActifServiceImpl.class);

    private final ActifRepository actifRepository;

    private final ActifMapper actifMapper;

    public ActifServiceImpl(ActifRepository actifRepository, ActifMapper actifMapper) {
        this.actifRepository = actifRepository;
        this.actifMapper = actifMapper;
    }

    @Override
    public ActifDTO save(ActifDTO actifDTO) {
        LOG.debug("Request to save Actif : {}", actifDTO);
        Actif actif = actifMapper.toEntity(actifDTO);
        actif = actifRepository.save(actif);
        return actifMapper.toDto(actif);
    }

    @Override
    public ActifDTO update(ActifDTO actifDTO) {
        LOG.debug("Request to update Actif : {}", actifDTO);
        Actif actif = actifMapper.toEntity(actifDTO);
        actif = actifRepository.save(actif);
        return actifMapper.toDto(actif);
    }

    @Override
    public Optional<ActifDTO> partialUpdate(ActifDTO actifDTO) {
        LOG.debug("Request to partially update Actif : {}", actifDTO);

        return actifRepository
            .findById(actifDTO.getId())
            .map(existingActif -> {
                actifMapper.partialUpdate(existingActif, actifDTO);

                return existingActif;
            })
            .map(actifRepository::save)
            .map(actifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ActifDTO> findOne(Long id) {
        LOG.debug("Request to get Actif : {}", id);
        return actifRepository.findById(id).map(actifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Actif : {}", id);
        actifRepository.deleteById(id);
    }
}
