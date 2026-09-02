package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.Fournisseur;
import com.dgi.gestionactifs.service.dto.FournisseurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Fournisseur} and its DTO {@link FournisseurDTO}.
 */
@Mapper(componentModel = "spring")
public interface FournisseurMapper extends EntityMapper<FournisseurDTO, Fournisseur> {}
