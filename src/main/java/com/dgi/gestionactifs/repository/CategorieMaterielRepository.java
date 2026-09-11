package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.CategorieMateriel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategorieMateriel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieMaterielRepository extends JpaRepository<CategorieMateriel, Long> {}
