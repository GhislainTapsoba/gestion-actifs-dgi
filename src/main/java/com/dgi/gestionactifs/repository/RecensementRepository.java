package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Recensement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Recensement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecensementRepository extends JpaRepository<Recensement, Long>, JpaSpecificationExecutor<Recensement> {}
