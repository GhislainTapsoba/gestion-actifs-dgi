package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.service.dto.ActifDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actif} and its DTO {@link ActifDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActifMapper extends EntityMapper<ActifDTO, Actif> {}
