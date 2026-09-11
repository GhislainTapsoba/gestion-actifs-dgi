package com.dgi.gestionactifs.service;

import com.dgi.gestionactifs.domain.*; // for static metamodels
import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.repository.ActifRepository;
import com.dgi.gestionactifs.service.criteria.ActifCriteria;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.mapper.ActifMapper;
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
 * Service for executing complex queries for {@link Actif} entities in the database.
 * The main input is a {@link ActifCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ActifDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActifQueryService extends QueryService<Actif> {

    private static final Logger LOG = LoggerFactory.getLogger(ActifQueryService.class);

    private final ActifRepository actifRepository;

    private final ActifMapper actifMapper;

    public ActifQueryService(ActifRepository actifRepository, ActifMapper actifMapper) {
        this.actifRepository = actifRepository;
        this.actifMapper = actifMapper;
    }

    /**
     * Return a {@link Page} of {@link ActifDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActifDTO> findByCriteria(ActifCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Actif> specification = createSpecification(criteria);
        return actifRepository.findAll(specification, page).map(actifMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActifCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Actif> specification = createSpecification(criteria);
        return actifRepository.count(specification);
    }

    /**
     * Function to convert {@link ActifCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Actif> createSpecification(ActifCriteria criteria) {
        Specification<Actif> specification = Specification.unrestricted();
        specification = specification.and((root, query, builder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch(Actif_.categorie, JoinType.LEFT);
            }
            return null;
        });
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = specification.and(
                Specification.allOf(
                    Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                    buildRangeSpecification(criteria.getId(), Actif_.id),
                    buildStringSpecification(criteria.getCodeInventaire(), Actif_.codeInventaire),
                    buildStringSpecification(criteria.getDesignation(), Actif_.designation),
                    buildStringSpecification(criteria.getMarque(), Actif_.marque),
                    buildStringSpecification(criteria.getModele(), Actif_.modele),
                    buildStringSpecification(criteria.getNumeroSerie(), Actif_.numeroSerie),
                    buildStringSpecification(criteria.getCodeBarre(), Actif_.codeBarre),
                    buildSpecification(criteria.getType(), Actif_.type),
                    buildSpecification(criteria.getEtat(), Actif_.etat),
                    buildStringSpecification(criteria.getLocalisation(), Actif_.localisation),
                    buildRangeSpecification(criteria.getDateAcquisition(), Actif_.dateAcquisition),
                    buildRangeSpecification(criteria.getValeurAcquisition(), Actif_.valeurAcquisition),
                    buildSpecification(criteria.getCategorieId(), root ->
                        root.join(Actif_.categorie, JoinType.LEFT).get(CategorieMateriel_.id)
                    )
                )
            );
        }
        return specification;
    }
}
