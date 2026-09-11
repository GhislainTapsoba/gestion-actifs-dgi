package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.EquipementRecensement;
import com.dgi.gestionactifs.repository.EquipementRecensementRepository;
import com.dgi.gestionactifs.service.criteria.EquipementRecensementCriteria;
import com.dgi.gestionactifs.service.dto.EquipementRecensementDTO;
import com.dgi.gestionactifs.service.mapper.EquipementRecensementMapper;
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
 * Service for executing complex queries for {@link EquipementRecensement} entities in the database.
 * The main input is a {@link EquipementRecensementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EquipementRecensementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EquipementRecensementQueryService extends QueryService<EquipementRecensement> {

    private static final Logger LOG = LoggerFactory.getLogger(EquipementRecensementQueryService.class);

    private final EquipementRecensementRepository equipementRecensementRepository;

    private final EquipementRecensementMapper equipementRecensementMapper;

    public EquipementRecensementQueryService(
        EquipementRecensementRepository equipementRecensementRepository,
        EquipementRecensementMapper equipementRecensementMapper
    ) {
        this.equipementRecensementRepository = equipementRecensementRepository;
        this.equipementRecensementMapper = equipementRecensementMapper;
    }

    /**
     * Return a {@link Page} of {@link EquipementRecensementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EquipementRecensementDTO> findByCriteria(EquipementRecensementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EquipementRecensement> specification = createSpecification(criteria);
        return equipementRecensementRepository.findAll(specification, page).map(equipementRecensementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EquipementRecensementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EquipementRecensement> specification = createSpecification(criteria);
        return equipementRecensementRepository.count(specification);
    }

    /**
     * Function to convert {@link EquipementRecensementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EquipementRecensement> createSpecification(EquipementRecensementCriteria criteria) {
        Specification<EquipementRecensement> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(EquipementRecensement_.recensement, JoinType.LEFT);
                root.fetch(EquipementRecensement_.actif, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), EquipementRecensement_.id),
                    buildSpecification(criteria.getEtatConstate(), EquipementRecensement_.etatConstate),
                    buildRangeSpecification(criteria.getDateConstat(), EquipementRecensement_.dateConstat),
                    buildStringSpecification(criteria.getEmplacementConstate(), EquipementRecensement_.emplacementConstate),
                    buildSpecification(criteria.getAnomalieConstatee(), EquipementRecensement_.anomalieConstatee),
                    buildSpecification(criteria.getRecensementId(), root ->
                        root.join(EquipementRecensement_.recensement, JoinType.LEFT).get(Recensement_.id)
                    ),
                    buildSpecification(criteria.getActifId(), root -> root.join(EquipementRecensement_.actif, JoinType.LEFT).get(Actif_.id))
                )
            );
        }
        return specification;
    }
}
