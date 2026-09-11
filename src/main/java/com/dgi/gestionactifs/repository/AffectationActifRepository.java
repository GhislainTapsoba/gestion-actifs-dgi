package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.AffectationActif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AffectationActif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationActifRepository extends JpaRepository<AffectationActif, Long>, JpaSpecificationExecutor<AffectationActif> {}
