package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.TransfertActif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransfertActif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransfertActifRepository extends JpaRepository<TransfertActif, Long>, JpaSpecificationExecutor<TransfertActif> {}
