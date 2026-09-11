package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Inventaire;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.InventaireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Inventaire} and its DTO {@link InventaireDTO}.
 */
@Mapper(componentModel = "spring")
public interface InventaireMapper extends EntityMapper<InventaireDTO, Inventaire> {
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    InventaireDTO toDto(Inventaire s);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
