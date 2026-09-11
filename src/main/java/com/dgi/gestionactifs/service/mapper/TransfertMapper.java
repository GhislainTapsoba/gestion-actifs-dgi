package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transfert} and its DTO {@link TransfertDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransfertMapper extends EntityMapper<TransfertDTO, Transfert> {
    @Mapping(target = "serviceOrigine", source = "serviceOrigine", qualifiedByName = "serviceDgiId")
    @Mapping(target = "serviceDestinataire", source = "serviceDestinataire", qualifiedByName = "serviceDgiId")
    @Mapping(target = "demandeur", source = "demandeur", qualifiedByName = "userId")
    @Mapping(target = "validateur", source = "validateur", qualifiedByName = "userId")
    TransfertDTO toDto(Transfert s);

    @Named("serviceDgiId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ServiceDgiDTO toDtoServiceDgiId(ServiceDgi serviceDgi);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
