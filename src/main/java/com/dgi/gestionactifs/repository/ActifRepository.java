package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Actif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Actif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActifRepository extends JpaRepository<Actif, Long>, JpaSpecificationExecutor<Actif> {}
