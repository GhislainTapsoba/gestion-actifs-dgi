package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Recensement;
import com.dgi.gestionactifs.repository.RecensementRepository;
import com.dgi.gestionactifs.service.criteria.RecensementCriteria;
import com.dgi.gestionactifs.service.dto.RecensementDTO;
import com.dgi.gestionactifs.service.mapper.RecensementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Recensement} entities in the database.
 * The main input is a {@link RecensementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RecensementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecensementQueryService extends QueryService<Recensement> {

    private static final Logger LOG = LoggerFactory.getLogger(RecensementQueryService.class);

    private final RecensementRepository recensementRepository;

    private final RecensementMapper recensementMapper;

    public RecensementQueryService(RecensementRepository recensementRepository, RecensementMapper recensementMapper) {
        this.recensementRepository = recensementRepository;
        this.recensementMapper = recensementMapper;
    }

    /**
     * Return a {@link Page} of {@link RecensementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RecensementDTO> findByCriteria(RecensementCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recensement> specification = createSpecification(criteria);
        return recensementRepository.findAll(specification, page).map(recensementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RecensementCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Recensement> specification = createSpecification(criteria);
        return recensementRepository.count(specification);
    }

    /**
     * Function to convert {@link RecensementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Recensement> createSpecification(RecensementCriteria criteria) {
        Specification<Recensement> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Recensement_.id),
                    buildRangeSpecification(criteria.getDateDebut(), Recensement_.dateDebut),
                    buildRangeSpecification(criteria.getDateFin(), Recensement_.dateFin),
                    buildSpecification(criteria.getStatut(), Recensement_.statut)
                )
            );
        }
        return specification;
    }
}
