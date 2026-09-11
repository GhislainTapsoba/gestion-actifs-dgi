package com.dgi.gestionactifs.service.mapper;

import com.dgi.gestionactifs.domain.ServiceDgi;
import com.dgi.gestionactifs.service.dto.ServiceDgiDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ServiceDgi} and its DTO {@link ServiceDgiDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServiceDgiMapper extends EntityMapper<ServiceDgiDTO, ServiceDgi> {}
