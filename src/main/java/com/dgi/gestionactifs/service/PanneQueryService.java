package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Panne;
import com.dgi.gestionactifs.repository.PanneRepository;
import com.dgi.gestionactifs.service.criteria.PanneCriteria;
import com.dgi.gestionactifs.service.dto.PanneDTO;
import com.dgi.gestionactifs.service.mapper.PanneMapper;
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
 * Service for executing complex queries for {@link Panne} entities in the database.
 * The main input is a {@link PanneCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PanneDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PanneQueryService extends QueryService<Panne> {

    private static final Logger LOG = LoggerFactory.getLogger(PanneQueryService.class);

    private final PanneRepository panneRepository;

    private final PanneMapper panneMapper;

    public PanneQueryService(PanneRepository panneRepository, PanneMapper panneMapper) {
        this.panneRepository = panneRepository;
        this.panneMapper = panneMapper;
    }

    /**
     * Return a {@link Page} of {@link PanneDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PanneDTO> findByCriteria(PanneCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Panne> specification = createSpecification(criteria);
        return panneRepository.findAll(specification, page).map(panneMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PanneCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Panne> specification = createSpecification(criteria);
        return panneRepository.count(specification);
    }

    /**
     * Function to convert {@link PanneCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Panne> createSpecification(PanneCriteria criteria) {
        Specification<Panne> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Panne_.actif, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Panne_.id),
                    buildStringSpecification(criteria.getDescription(), Panne_.description),
                    buildRangeSpecification(criteria.getDateDeclaration(), Panne_.dateDeclaration),
                    buildSpecification(criteria.getStatutPanne(), Panne_.statutPanne),
                    buildSpecification(criteria.getActifId(), root -> root.join(Panne_.actif, JoinType.LEFT).get(Actif_.id))
                )
            );
        }
        return specification;
    }
}
