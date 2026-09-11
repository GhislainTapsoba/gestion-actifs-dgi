package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Intervention;
import com.dgi.gestionactifs.domain.PlanningMaintenance;
import com.dgi.gestionactifs.service.dto.InterventionDTO;
import com.dgi.gestionactifs.service.dto.PlanningMaintenanceDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanningMaintenance} and its DTO {@link PlanningMaintenanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanningMaintenanceMapper extends EntityMapper<PlanningMaintenanceDTO, PlanningMaintenance> {
    @Mapping(target = "interventions", source = "interventions", qualifiedByName = "interventionIdSet")
    PlanningMaintenanceDTO toDto(PlanningMaintenance s);

    @Mapping(target = "interventions", ignore = true)
    @Mapping(target = "removeIntervention", ignore = true)
    PlanningMaintenance toEntity(PlanningMaintenanceDTO planningMaintenanceDTO);

    @Named("interventionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InterventionDTO toDtoInterventionId(Intervention intervention);

    @Named("interventionIdSet")
    default Set<InterventionDTO> toDtoInterventionIdSet(Set<Intervention> intervention) {
        return intervention.stream().map(this::toDtoInterventionId).collect(Collectors.toSet());
    }
}
