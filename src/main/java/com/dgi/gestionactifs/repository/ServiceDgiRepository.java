package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.ServiceDgi;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ServiceDgi entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceDgiRepository extends JpaRepository<ServiceDgi, Long>, JpaSpecificationExecutor<ServiceDgi> {}
