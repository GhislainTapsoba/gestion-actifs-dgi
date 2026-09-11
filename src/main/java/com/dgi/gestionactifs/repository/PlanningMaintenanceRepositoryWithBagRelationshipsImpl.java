package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.PlanningMaintenance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PlanningMaintenanceRepositoryWithBagRelationshipsImpl implements PlanningMaintenanceRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String PLANNINGMAINTENANCES_PARAMETER = "planningMaintenances";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<PlanningMaintenance> fetchBagRelationships(Optional<PlanningMaintenance> planningMaintenance) {
        return planningMaintenance.map(this::fetchInterventions);
    }

    @Override
    public Page<PlanningMaintenance> fetchBagRelationships(Page<PlanningMaintenance> planningMaintenances) {
        return new PageImpl<>(
            fetchBagRelationships(planningMaintenances.getContent()),
            planningMaintenances.getPageable(),
            planningMaintenances.getTotalElements()
        );
    }

    @Override
    public List<PlanningMaintenance> fetchBagRelationships(List<PlanningMaintenance> planningMaintenances) {
        return Optional.of(planningMaintenances).map(this::fetchInterventions).orElse(List.of());
    }

    PlanningMaintenance fetchInterventions(PlanningMaintenance result) {
        return entityManager
            .createQuery(
                "select planningMaintenance from PlanningMaintenance planningMaintenance left join fetch planningMaintenance.interventions where planningMaintenance.id = :id",
                PlanningMaintenance.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<PlanningMaintenance> fetchInterventions(List<PlanningMaintenance> planningMaintenances) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, planningMaintenances.size()).forEach(index -> order.put(planningMaintenances.get(index).getId(), index));
        List<PlanningMaintenance> result = entityManager
            .createQuery(
                "select planningMaintenance from PlanningMaintenance planningMaintenance left join fetch planningMaintenance.interventions where planningMaintenance in :planningMaintenances",
                PlanningMaintenance.class
            )
            .setParameter(PLANNINGMAINTENANCES_PARAMETER, planningMaintenances)
            .getResultList();
        result.sort((o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
