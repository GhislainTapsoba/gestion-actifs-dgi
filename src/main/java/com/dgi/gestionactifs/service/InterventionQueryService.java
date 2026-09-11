package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Intervention;
import com.dgi.gestionactifs.repository.InterventionRepository;
import com.dgi.gestionactifs.service.criteria.InterventionCriteria;
import com.dgi.gestionactifs.service.dto.InterventionDTO;
import com.dgi.gestionactifs.service.mapper.InterventionMapper;
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
 * Service for executing complex queries for {@link Intervention} entities in the database.
 * The main input is a {@link InterventionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InterventionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterventionQueryService extends QueryService<Intervention> {

    private static final Logger LOG = LoggerFactory.getLogger(InterventionQueryService.class);

    private final InterventionRepository interventionRepository;

    private final InterventionMapper interventionMapper;

    public InterventionQueryService(InterventionRepository interventionRepository, InterventionMapper interventionMapper) {
        this.interventionRepository = interventionRepository;
        this.interventionMapper = interventionMapper;
    }

    /**
     * Return a {@link Page} of {@link InterventionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionDTO> findByCriteria(InterventionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.findAll(specification, page).map(interventionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterventionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.count(specification);
    }

    /**
     * Function to convert {@link InterventionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Intervention> createSpecification(InterventionCriteria criteria) {
        Specification<Intervention> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Intervention_.panne, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Intervention_.id),
                    buildRangeSpecification(criteria.getDateDeclaration(), Intervention_.dateDeclaration),
                    buildSpecification(criteria.getTypeIntervention(), Intervention_.typeIntervention),
                    buildSpecification(criteria.getStatut(), Intervention_.statut),
                    buildStringSpecification(criteria.getDescription(), Intervention_.description),
                    buildSpecification(criteria.getPanneId(), root -> root.join(Intervention_.panne, JoinType.LEFT).get(Panne_.id)),
                    buildSpecification(criteria.getPlanningId(), root ->
                        root.join(Intervention_.plannings, JoinType.LEFT).get(PlanningMaintenance_.id)
                    )
                )
            );
        }
        return specification;
    }
}
