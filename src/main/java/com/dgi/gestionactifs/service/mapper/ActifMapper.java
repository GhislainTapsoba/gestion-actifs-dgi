package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.CategorieMateriel;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.CategorieMaterielDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actif} and its DTO {@link ActifDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActifMapper extends EntityMapper<ActifDTO, Actif> {
    @Mapping(target = "categorie", source = "categorie", qualifiedByName = "categorieMaterielId")
    ActifDTO toDto(Actif s);

    @Named("categorieMaterielId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategorieMaterielDTO toDtoCategorieMaterielId(CategorieMateriel categorieMateriel);
}
