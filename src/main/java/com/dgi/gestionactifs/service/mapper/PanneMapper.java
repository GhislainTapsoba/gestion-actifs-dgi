package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Panne;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.PanneDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Panne} and its DTO {@link PanneDTO}.
 */
@Mapper(componentModel = "spring")
public interface PanneMapper extends EntityMapper<PanneDTO, Panne> {
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    PanneDTO toDto(Panne s);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
