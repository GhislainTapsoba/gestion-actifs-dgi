package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.AffectationActif;
import com.dgi.gestionactifs.repository.AffectationActifRepository;
import com.dgi.gestionactifs.service.criteria.AffectationActifCriteria;
import com.dgi.gestionactifs.service.dto.AffectationActifDTO;
import com.dgi.gestionactifs.service.mapper.AffectationActifMapper;
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
 * Service for executing complex queries for {@link AffectationActif} entities in the database.
 * The main input is a {@link AffectationActifCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AffectationActifDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AffectationActifQueryService extends QueryService<AffectationActif> {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationActifQueryService.class);

    private final AffectationActifRepository affectationActifRepository;

    private final AffectationActifMapper affectationActifMapper;

    public AffectationActifQueryService(
        AffectationActifRepository affectationActifRepository,
        AffectationActifMapper affectationActifMapper
    ) {
        this.affectationActifRepository = affectationActifRepository;
        this.affectationActifMapper = affectationActifMapper;
    }

    /**
     * Return a {@link Page} of {@link AffectationActifDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AffectationActifDTO> findByCriteria(AffectationActifCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AffectationActif> specification = createSpecification(criteria);
        return affectationActifRepository.findAll(specification, page).map(affectationActifMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AffectationActifCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<AffectationActif> specification = createSpecification(criteria);
        return affectationActifRepository.count(specification);
    }

    /**
     * Function to convert {@link AffectationActifCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AffectationActif> createSpecification(AffectationActifCriteria criteria) {
        Specification<AffectationActif> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(AffectationActif_.affectation, JoinType.LEFT);
                root.fetch(AffectationActif_.actif, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), AffectationActif_.id),
                    buildStringSpecification(criteria.getObservation(), AffectationActif_.observation),
                    buildSpecification(criteria.getStatut(), AffectationActif_.statut),
                    buildSpecification(criteria.getAffectationId(), root ->
                        root.join(AffectationActif_.affectation, JoinType.LEFT).get(Affectation_.id)
                    ),
                    buildSpecification(criteria.getActifId(), root -> root.join(AffectationActif_.actif, JoinType.LEFT).get(Actif_.id))
                )
            );
        }
        return specification;
    }
}
