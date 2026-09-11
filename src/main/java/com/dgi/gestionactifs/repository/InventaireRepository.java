package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Inventaire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inventaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventaireRepository extends JpaRepository<Inventaire, Long> {}
