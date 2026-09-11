package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.repository.TransfertRepository;
import com.dgi.gestionactifs.service.criteria.TransfertCriteria;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.mapper.TransfertMapper;
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
 * Service for executing complex queries for {@link Transfert} entities in the database.
 * The main input is a {@link TransfertCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransfertDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransfertQueryService extends QueryService<Transfert> {

    private static final Logger LOG = LoggerFactory.getLogger(TransfertQueryService.class);

    private final TransfertRepository transfertRepository;

    private final TransfertMapper transfertMapper;

    public TransfertQueryService(TransfertRepository transfertRepository, TransfertMapper transfertMapper) {
        this.transfertRepository = transfertRepository;
        this.transfertMapper = transfertMapper;
    }

    /**
     * Return a {@link Page} of {@link TransfertDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransfertDTO> findByCriteria(TransfertCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transfert> specification = createSpecification(criteria);
        return transfertRepository.findAll(specification, page).map(transfertMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransfertCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Transfert> specification = createSpecification(criteria);
        return transfertRepository.count(specification);
    }

    /**
     * Function to convert {@link TransfertCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transfert> createSpecification(TransfertCriteria criteria) {
        Specification<Transfert> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Transfert_.serviceOrigine, JoinType.LEFT);
                root.fetch(Transfert_.serviceDestinataire, JoinType.LEFT);
                root.fetch(Transfert_.demandeur, JoinType.LEFT);
                root.fetch(Transfert_.validateur, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Transfert_.id),
                    buildRangeSpecification(criteria.getDateTransfert(), Transfert_.dateTransfert),
                    buildSpecification(criteria.getStatut(), Transfert_.statut),
                    buildStringSpecification(criteria.getCommentaireRejet(), Transfert_.commentaireRejet),
                    buildRangeSpecification(criteria.getDateTraitement(), Transfert_.dateTraitement),
                    buildSpecification(criteria.getServiceOrigineId(), root ->
                        root.join(Transfert_.serviceOrigine, JoinType.LEFT).get(ServiceDgi_.id)
                    ),
                    buildSpecification(criteria.getServiceDestinataireId(), root ->
                        root.join(Transfert_.serviceDestinataire, JoinType.LEFT).get(ServiceDgi_.id)
                    ),
                    buildSpecification(criteria.getDemandeurId(), root -> root.join(Transfert_.demandeur, JoinType.LEFT).get(User_.id)),
                    buildSpecification(criteria.getValidateurId(), root -> root.join(Transfert_.validateur, JoinType.LEFT).get(User_.id))
                )
            );
        }
        return specification;
    }
}
