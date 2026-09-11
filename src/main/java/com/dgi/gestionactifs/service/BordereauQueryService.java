package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Bordereau;
import com.dgi.gestionactifs.repository.BordereauRepository;
import com.dgi.gestionactifs.service.criteria.BordereauCriteria;
import com.dgi.gestionactifs.service.dto.BordereauDTO;
import com.dgi.gestionactifs.service.mapper.BordereauMapper;
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
 * Service for executing complex queries for {@link Bordereau} entities in the database.
 * The main input is a {@link BordereauCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BordereauDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BordereauQueryService extends QueryService<Bordereau> {

    private static final Logger LOG = LoggerFactory.getLogger(BordereauQueryService.class);

    private final BordereauRepository bordereauRepository;

    private final BordereauMapper bordereauMapper;

    public BordereauQueryService(BordereauRepository bordereauRepository, BordereauMapper bordereauMapper) {
        this.bordereauRepository = bordereauRepository;
        this.bordereauMapper = bordereauMapper;
    }

    /**
     * Return a {@link Page} of {@link BordereauDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BordereauDTO> findByCriteria(BordereauCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Bordereau> specification = createSpecification(criteria);
        return bordereauRepository.findAll(specification, page).map(bordereauMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BordereauCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Bordereau> specification = createSpecification(criteria);
        return bordereauRepository.count(specification);
    }

    /**
     * Function to convert {@link BordereauCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Bordereau> createSpecification(BordereauCriteria criteria) {
        Specification<Bordereau> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Bordereau_.transfert, JoinType.LEFT);
                root.fetch(Bordereau_.affectation, JoinType.LEFT);
                root.fetch(Bordereau_.emetteur, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Bordereau_.id),
                    buildStringSpecification(criteria.getNumero(), Bordereau_.numero),
                    buildRangeSpecification(criteria.getDateEmission(), Bordereau_.dateEmission),
                    buildSpecification(criteria.getTypeBordereau(), Bordereau_.typeBordereau),
                    buildSpecification(criteria.getStatutValidation(), Bordereau_.statutValidation),
                    buildRangeSpecification(criteria.getDateValidation(), Bordereau_.dateValidation),
                    buildSpecification(criteria.getTransfertId(), root ->
                        root.join(Bordereau_.transfert, JoinType.LEFT).get(Transfert_.id)
                    ),
                    buildSpecification(criteria.getAffectationId(), root ->
                        root.join(Bordereau_.affectation, JoinType.LEFT).get(Affectation_.id)
                    ),
                    buildSpecification(criteria.getEmetteurId(), root -> root.join(Bordereau_.emetteur, JoinType.LEFT).get(User_.id))
                )
            );
        }
        return specification;
    }
}
