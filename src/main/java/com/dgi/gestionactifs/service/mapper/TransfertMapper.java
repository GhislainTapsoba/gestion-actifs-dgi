package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transfert} and its DTO {@link TransfertDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransfertMapper extends EntityMapper<TransfertDTO, Transfert> {
    @Mapping(target = "demandeur", source = "demandeur", qualifiedByName = "userId")
    @Mapping(target = "validateur", source = "validateur", qualifiedByName = "userId")
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    TransfertDTO toDto(Transfert s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
