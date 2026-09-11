package com.dgi.gestionactifs.service.impl;

import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.repository.ServiceDgiRepository;
import com.dgi.gestionactifs.service.ServiceDgiService;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import com.dgi.gestionactifs.service.mapper.ServiceDgiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.dgi.gestionactifs.domain.ServiceDgi}.
 */
@Service
@Transactional
public class ServiceDgiServiceImpl implements ServiceDgiService {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDgiServiceImpl.class);

    private final ServiceDgiRepository serviceDgiRepository;

    private final ServiceDgiMapper serviceDgiMapper;

    public ServiceDgiServiceImpl(ServiceDgiRepository serviceDgiRepository, ServiceDgiMapper serviceDgiMapper) {
        this.serviceDgiRepository = serviceDgiRepository;
        this.serviceDgiMapper = serviceDgiMapper;
    }

    @Override
    public ServiceDgiDTO save(ServiceDgiDTO serviceDgiDTO) {
        LOG.debug("Request to save ServiceDgi : {}", serviceDgiDTO);
        ServiceDgi serviceDgi = serviceDgiMapper.toEntity(serviceDgiDTO);
        serviceDgi = serviceDgiRepository.save(serviceDgi);
        return serviceDgiMapper.toDto(serviceDgi);
    }

    @Override
    public ServiceDgiDTO update(ServiceDgiDTO serviceDgiDTO) {
        LOG.debug("Request to update ServiceDgi : {}", serviceDgiDTO);
        ServiceDgi serviceDgi = serviceDgiMapper.toEntity(serviceDgiDTO);
        serviceDgi = serviceDgiRepository.save(serviceDgi);
        return serviceDgiMapper.toDto(serviceDgi);
    }

    @Override
    public Optional<ServiceDgiDTO> partialUpdate(ServiceDgiDTO serviceDgiDTO) {
        LOG.debug("Request to partially update ServiceDgi : {}", serviceDgiDTO);

        return serviceDgiRepository
            .findById(serviceDgiDTO.getId())
            .map(existingServiceDgi -> {
                serviceDgiMapper.partialUpdate(existingServiceDgi, serviceDgiDTO);

                return existingServiceDgi;
            })
            .map(serviceDgiRepository::save)
            .map(serviceDgiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ServiceDgiDTO> findOne(Long id) {
        LOG.debug("Request to get ServiceDgi : {}", id);
        return serviceDgiRepository.findById(id).map(serviceDgiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ServiceDgi : {}", id);
        serviceDgiRepository.deleteById(id);
    }
}
