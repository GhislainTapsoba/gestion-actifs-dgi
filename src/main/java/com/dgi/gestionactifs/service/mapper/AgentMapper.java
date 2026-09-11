package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Agent;
import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.AgentDTO;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {
    @Mapping(target = "service", source = "service", qualifiedByName = "serviceDgiId")
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "userId")
    AgentDTO toDto(Agent s);

    @Named("serviceDgiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServiceDgiDTO toDtoServiceDgiId(ServiceDgi serviceDgi);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
