package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.PlanningMaintenance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PlanningMaintenanceRepositoryWithBagRelationships {
    Optional<PlanningMaintenance> fetchBagRelationships(Optional<PlanningMaintenance> planningMaintenance);

    List<PlanningMaintenance> fetchBagRelationships(List<PlanningMaintenance> planningMaintenances);

    Page<PlanningMaintenance> fetchBagRelationships(Page<PlanningMaintenance> planningMaintenances);
}
