package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.CategorieMateriel;
import com.dgi.gestionactifs.service.dto.CategorieMaterielDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategorieMateriel} and its DTO {@link CategorieMaterielDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategorieMaterielMapper extends EntityMapper<CategorieMaterielDTO, CategorieMateriel> {}
