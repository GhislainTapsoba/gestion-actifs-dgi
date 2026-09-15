package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Rapport;
import com.dgi.gestionactifs.service.dto.RapportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rapport} and its DTO {@link RapportDTO}.
 */
@Mapper(componentModel = "spring")
public interface RapportMapper extends EntityMapper<RapportDTO, Rapport> {
    Rapport toEntity(RapportDTO rapportDTO);

    RapportDTO toDto(Rapport rapport);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RapportDTO toDtoId(Rapport rapport);
}
