package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.HistoriqueAction;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.HistoriqueActionDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoriqueAction} and its DTO {@link HistoriqueActionDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueActionMapper extends EntityMapper<HistoriqueActionDTO, HistoriqueAction> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "userId")
    HistoriqueActionDTO toDto(HistoriqueAction s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
