package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Transfert;
import com.dgi.gestionactifs.domain.TransfertActif;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.TransfertActifDTO;
import com.dgi.gestionactifs.service.dto.TransfertDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransfertActif} and its DTO {@link TransfertActifDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransfertActifMapper extends EntityMapper<TransfertActifDTO, TransfertActif> {
    @Mapping(target = "transfert", source = "transfert", qualifiedByName = "transfertId")
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    TransfertActifDTO toDto(TransfertActif s);

    @Named("transfertId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransfertDTO toDtoTransfertId(Transfert transfert);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
