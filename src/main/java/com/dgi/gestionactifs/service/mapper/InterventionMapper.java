package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Intervention;
import com.dgi.gestionactifs.domain.Panne;
import com.dgi.gestionactifs.domain.PlanningMaintenance;
import com.dgi.gestionactifs.service.dto.InterventionDTO;
import com.dgi.gestionactifs.service.dto.PanneDTO;
import com.dgi.gestionactifs.service.dto.PlanningMaintenanceDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Intervention} and its DTO {@link InterventionDTO}.
 */
@Mapper(componentModel = "spring")
public interface InterventionMapper extends EntityMapper<InterventionDTO, Intervention> {
    @Mapping(target = "panne", source = "panne", qualifiedByName = "panneId")
    @Mapping(target = "plannings", source = "plannings", qualifiedByName = "planningMaintenanceIdSet")
    InterventionDTO toDto(Intervention s);

    @Mapping(target = "plannings", ignore = true)
    @Mapping(target = "removePlanning", ignore = true)
    Intervention toEntity(InterventionDTO interventionDTO);

    @Named("panneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PanneDTO toDtoPanneId(Panne panne);

    @Named("planningMaintenanceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlanningMaintenanceDTO toDtoPlanningMaintenanceId(PlanningMaintenance planningMaintenance);

    @Named("planningMaintenanceIdSet")
    default Set<PlanningMaintenanceDTO> toDtoPlanningMaintenanceIdSet(Set<PlanningMaintenance> planningMaintenance) {
        return planningMaintenance.stream().map(this::toDtoPlanningMaintenanceId).collect(Collectors.toSet());
    }
}
