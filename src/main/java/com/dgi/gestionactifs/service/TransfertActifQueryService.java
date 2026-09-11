package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.TransfertActif;
import com.dgi.gestionactifs.repository.TransfertActifRepository;
import com.dgi.gestionactifs.service.criteria.TransfertActifCriteria;
import com.dgi.gestionactifs.service.dto.TransfertActifDTO;
import com.dgi.gestionactifs.service.mapper.TransfertActifMapper;
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
 * Service for executing complex queries for {@link TransfertActif} entities in the database.
 * The main input is a {@link TransfertActifCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransfertActifDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransfertActifQueryService extends QueryService<TransfertActif> {

    private static final Logger LOG = LoggerFactory.getLogger(TransfertActifQueryService.class);

    private final TransfertActifRepository transfertActifRepository;

    private final TransfertActifMapper transfertActifMapper;

    public TransfertActifQueryService(TransfertActifRepository transfertActifRepository, TransfertActifMapper transfertActifMapper) {
        this.transfertActifRepository = transfertActifRepository;
        this.transfertActifMapper = transfertActifMapper;
    }

    /**
     * Return a {@link Page} of {@link TransfertActifDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransfertActifDTO> findByCriteria(TransfertActifCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransfertActif> specification = createSpecification(criteria);
        return transfertActifRepository.findAll(specification, page).map(transfertActifMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransfertActifCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TransfertActif> specification = createSpecification(criteria);
        return transfertActifRepository.count(specification);
    }

    /**
     * Function to convert {@link TransfertActifCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransfertActif> createSpecification(TransfertActifCriteria criteria) {
        Specification<TransfertActif> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(TransfertActif_.transfert, JoinType.LEFT);
                root.fetch(TransfertActif_.actif, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), TransfertActif_.id),
                    buildStringSpecification(criteria.getObservation(), TransfertActif_.observation),
                    buildSpecification(criteria.getTransfertId(), root ->
                        root.join(TransfertActif_.transfert, JoinType.LEFT).get(Transfert_.id)
                    ),
                    buildSpecification(criteria.getActifId(), root -> root.join(TransfertActif_.actif, JoinType.LEFT).get(Actif_.id))
                )
            );
        }
        return specification;
    }
}
