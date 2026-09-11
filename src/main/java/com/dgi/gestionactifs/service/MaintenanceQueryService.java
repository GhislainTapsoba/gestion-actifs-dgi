package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Maintenance;
import com.dgi.gestionactifs.repository.MaintenanceRepository;
import com.dgi.gestionactifs.service.criteria.MaintenanceCriteria;
import com.dgi.gestionactifs.service.dto.MaintenanceDTO;
import com.dgi.gestionactifs.service.mapper.MaintenanceMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Maintenance} entities in the database.
 * The main input is a {@link MaintenanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link MaintenanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaintenanceQueryService extends QueryService<Maintenance> {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceQueryService.class);

    private final MaintenanceRepository maintenanceRepository;

    private final MaintenanceMapper maintenanceMapper;

    public MaintenanceQueryService(MaintenanceRepository maintenanceRepository, MaintenanceMapper maintenanceMapper) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenanceMapper = maintenanceMapper;
    }

    /**
     * Return a {@link Page} of {@link MaintenanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MaintenanceDTO> findByCriteria(MaintenanceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Maintenance> specification = createSpecification(criteria);
        return maintenanceRepository.findAll(specification, page).map(maintenanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaintenanceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Maintenance> specification = createSpecification(criteria);
        return maintenanceRepository.count(specification);
    }

    /**
     * Function to convert {@link MaintenanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Maintenance> createSpecification(MaintenanceCriteria criteria) {
        Specification<Maintenance> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Maintenance_.actif, JoinType.LEFT);
                root.fetch(Maintenance_.technicien, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Maintenance_.id),
                    buildSpecification(criteria.getTypeMaintenance(), Maintenance_.typeMaintenance),
                    buildRangeSpecification(criteria.getDatePanne(), Maintenance_.datePanne),
                    buildSpecification(criteria.getStatut(), Maintenance_.statut),
                    buildRangeSpecification(criteria.getDateCloture(), Maintenance_.dateCloture),
                    buildSpecification(criteria.getActifId(), root -> root.join(Maintenance_.actif, JoinType.LEFT).get(Actif_.id)),
                    buildSpecification(criteria.getTechnicienId(), root -> root.join(Maintenance_.technicien, JoinType.LEFT).get(User_.id))
                )
            );
        }
        return specification;
    }
}
