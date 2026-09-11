package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.Affectation;
import com.dgi.gestionactifs.domain.AffectationActif;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import com.dgi.gestionactifs.service.dto.AffectationActifDTO;
import com.dgi.gestionactifs.service.dto.AffectationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AffectationActif} and its DTO {@link AffectationActifDTO}.
 */
@Mapper(componentModel = "spring")
public interface AffectationActifMapper extends EntityMapper<AffectationActifDTO, AffectationActif> {
    @Mapping(target = "affectation", source = "affectation", qualifiedByName = "affectationId")
    @Mapping(target = "actif", source = "actif", qualifiedByName = "actifId")
    AffectationActifDTO toDto(AffectationActif s);

    @Named("affectationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AffectationDTO toDtoAffectationId(Affectation affectation);

    @Named("actifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ActifDTO toDtoActifId(Actif actif);
}
