package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.Panne;
import com.dgi.gestionactifs.repository.PanneRepository;
import com.dgi.gestionactifs.service.PanneService;
import com.dgi.gestionactifs.service.dto.PanneDTO;
import com.dgi.gestionactifs.service.mapper.PanneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.Panne}.
 */
@Service
@Transactional
public class PanneServiceImpl implements PanneService {

    private static final Logger LOG = LoggerFactory.getLogger(PanneServiceImpl.class);

    private final PanneRepository panneRepository;

    private final PanneMapper panneMapper;

    public PanneServiceImpl(PanneRepository panneRepository, PanneMapper panneMapper) {
        this.panneRepository = panneRepository;
        this.panneMapper = panneMapper;
    }

    @Override
    public PanneDTO save(PanneDTO panneDTO) {
        LOG.debug("Request to save Panne : {}", panneDTO);
        Panne panne = panneMapper.toEntity(panneDTO);
        panne = panneRepository.save(panne);
        return panneMapper.toDto(panne);
    }

    @Override
    public PanneDTO update(PanneDTO panneDTO) {
        LOG.debug("Request to update Panne : {}", panneDTO);
        Panne panne = panneMapper.toEntity(panneDTO);
        panne = panneRepository.save(panne);
        return panneMapper.toDto(panne);
    }

    @Override
    public Optional<PanneDTO> partialUpdate(PanneDTO panneDTO) {
        LOG.debug("Request to partially update Panne : {}", panneDTO);

        return panneRepository
            .findById(panneDTO.getId())
            .map(existingPanne -> {
                panneMapper.partialUpdate(existingPanne, panneDTO);

                return existingPanne;
            })
            .map(panneRepository::save)
            .map(panneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PanneDTO> findOne(Long id) {
        LOG.debug("Request to get Panne : {}", id);
        return panneRepository.findById(id).map(panneMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Panne : {}", id);
        panneRepository.deleteById(id);
    }
}
