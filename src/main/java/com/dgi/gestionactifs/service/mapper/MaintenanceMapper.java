package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Maintenance;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.MaintenanceDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Maintenance} and its DTO {@link MaintenanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaintenanceMapper extends EntityMapper<MaintenanceDTO, Maintenance> {
    MaintenanceDTO toDto(Maintenance s);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codeInventaire", source = "codeInventaire")
    ActifDTO toDtoActifId(Actif actif);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserId(User user);
}
