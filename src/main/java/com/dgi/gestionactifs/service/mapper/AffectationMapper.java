package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.domain.Agent;
import com.dgi.gestionactifs.service.dto.AffectationDTO;
import com.dgi.gestionactifs.service.dto.AgentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Affectation} and its DTO {@link AffectationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffectationMapper extends EntityMapper<AffectationDTO, Affectation> {
    @Mapping(target = "agent", source = "agent", qualifiedByName = "agentId")
    AffectationDTO toDto(Affectation s);

    @Named("agentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AgentDTO toDtoAgentId(Agent agent);
}
