package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.domain.Bordereau;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.User;
import com.dgi.gestionactifs.service.dto.AffectationDTO;
import com.dgi.gestionactifs.service.dto.BordereauDTO;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import com.dgi.gestionactifs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bordereau} and its DTO {@link BordereauDTO}.
 */
@Mapper(componentModel = "spring")
public interface BordereauMapper extends EntityMapper<BordereauDTO, Bordereau> {
    @Mapping(target = "transfert", source = "transfert", qualifiedByName = "transfertId")
    @Mapping(target = "affectation", source = "affectation", qualifiedByName = "affectationId")
    @Mapping(target = "emetteur", source = "emetteur", qualifiedByName = "userId")
    BordereauDTO toDto(Bordereau s);

    @Named("transfertId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransfertDTO toDtoTransfertId(Transfert transfert);

    @Named("affectationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AffectationDTO toDtoAffectationId(Affectation affectation);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
