package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Recensement;
import com.dgi.gestionactifs.service.dto.RecensementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Recensement} and its DTO {@link RecensementDTO}.
 */
@Mapper(componentModel = "spring")
public interface RecensementMapper extends EntityMapper<RecensementDTO, Recensement> {}
