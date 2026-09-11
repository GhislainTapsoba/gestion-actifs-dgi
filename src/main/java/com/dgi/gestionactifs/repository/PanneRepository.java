package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Panne;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Panne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanneRepository extends JpaRepository<Panne, Long>, JpaSpecificationExecutor<Panne> {}
