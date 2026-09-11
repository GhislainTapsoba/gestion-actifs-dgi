package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.EquipementRecensement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EquipementRecensement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EquipementRecensementRepository
    extends JpaRepository<EquipementRecensement, Long>, JpaSpecificationExecutor<EquipementRecensement> {}
