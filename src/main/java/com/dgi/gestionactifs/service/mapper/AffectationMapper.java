package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.AffectationDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Affectation} and its DTO {@link AffectationDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffectationMapper extends EntityMapper<AffectationDTO, Affectation> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "userId")
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    AffectationDTO toDto(Affectation s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
