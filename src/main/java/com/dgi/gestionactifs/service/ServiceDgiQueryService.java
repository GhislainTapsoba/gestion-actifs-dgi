package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.repository.ServiceDgiRepository;
import com.dgi.gestionactifs.service.criteria.ServiceDgiCriteria;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import com.dgi.gestionactifs.service.mapper.ServiceDgiMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ServiceDgi} entities in the database.
 * The main input is a {@link ServiceDgiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ServiceDgiDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceDgiQueryService extends QueryService<ServiceDgi> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceDgiQueryService.class);

    private final ServiceDgiRepository serviceDgiRepository;

    private final ServiceDgiMapper serviceDgiMapper;

    public ServiceDgiQueryService(ServiceDgiRepository serviceDgiRepository, ServiceDgiMapper serviceDgiMapper) {
        this.serviceDgiRepository = serviceDgiRepository;
        this.serviceDgiMapper = serviceDgiMapper;
    }

    /**
     * Return a {@link Page} of {@link ServiceDgiDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceDgiDTO> findByCriteria(ServiceDgiCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceDgi> specification = createSpecification(criteria);
        return serviceDgiRepository.findAll(specification, page).map(serviceDgiMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceDgiCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ServiceDgi> specification = createSpecification(criteria);
        return serviceDgiRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceDgiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceDgi> createSpecification(ServiceDgiCriteria criteria) {
        Specification<ServiceDgi> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), ServiceDgi_.id),
                    buildStringSpecification(criteria.getNomService(), ServiceDgi_.nomService),
                    buildStringSpecification(criteria.getChefService(), ServiceDgi_.chefService)
                )
            );
        }
        return specification;
    }
}
