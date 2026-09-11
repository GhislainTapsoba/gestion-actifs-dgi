package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.EquipementRecensement;
import com.dgi.gestionactifs.domain.Recensement;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.EquipementRecensementDTO;
import com.dgi.gestionactifs.service.dto.RecensementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EquipementRecensement} and its DTO {@link EquipementRecensementDTO}.
 */
@Mapper(componentModel = "spring")
public interface EquipementRecensementMapper extends EntityMapper<EquipementRecensementDTO, EquipementRecensement> {
    @Mapping(target = "recensement", source = "recensement", qualifiedByName = "recensementId")
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    EquipementRecensementDTO toDto(EquipementRecensement s);

    @Named("recensementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RecensementDTO toDtoRecensementId(Recensement recensement);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
