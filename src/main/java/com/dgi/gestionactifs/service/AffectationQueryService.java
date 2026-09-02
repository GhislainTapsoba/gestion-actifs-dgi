package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.repository.AffectationRepository;
import com.dgi.gestionactifs.service.criteria.AffectationCriteria;
import com.dgi.gestionactifs.service.dto.AffectationDTO;
import com.dgi.gestionactifs.service.mapper.AffectationMapper;
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
 * Service for executing complex queries for {@link Affectation} entities in the database.
 * The main input is a {@link AffectationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link AffectationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AffectationQueryService extends QueryService<Affectation> {

    private static final Logger LOG = LoggerFactory.getLogger(AffectationQueryService.class);

    private final AffectationRepository affectationRepository;

    private final AffectationMapper affectationMapper;

    public AffectationQueryService(AffectationRepository affectationRepository, AffectationMapper affectationMapper) {
        this.affectationRepository = affectationRepository;
        this.affectationMapper = affectationMapper;
    }

    /**
     * Return a {@link Page} of {@link AffectationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AffectationDTO> findByCriteria(AffectationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Affectation> specification = createSpecification(criteria);
        return affectationRepository.findAll(specification, page).map(affectationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AffectationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Affectation> specification = createSpecification(criteria);
        return affectationRepository.count(specification);
    }

    /**
     * Function to convert {@link AffectationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Affectation> createSpecification(AffectationCriteria criteria) {
        Specification<Affectation> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Affectation_.utilisateur, JoinType.LEFT);
                root.fetch(Affectation_.actif, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Affectation_.id),
                    buildRangeSpecification(criteria.getDateAffectation(), Affectation_.dateAffectation),
                    buildRangeSpecification(criteria.getDateRestitution(), Affectation_.dateRestitution),
                    buildStringSpecification(criteria.getNumeroBordereau(), Affectation_.numeroBordereau),
                    buildSpecification(criteria.getUtilisateurId(), root ->
                        root.join(Affectation_.utilisateur, JoinType.LEFT).get(User_.id)
                    ),
                    buildSpecification(criteria.getActifId(), root -> root.join(Affectation_.actif, JoinType.LEFT).get(Actif_.id))
                )
            );
        }
        return specification;
    }
}
