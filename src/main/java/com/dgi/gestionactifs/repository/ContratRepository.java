package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Contrat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Contrat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContratRepository extends JpaRepository<Contrat, Long> {}
