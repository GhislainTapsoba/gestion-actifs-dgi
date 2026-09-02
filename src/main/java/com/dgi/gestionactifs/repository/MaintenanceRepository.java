package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Maintenance;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Maintenance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>, JpaSpecificationExecutor<Maintenance> {
    @Query("select maintenance from Maintenance maintenance where maintenance.technicien.login = ?#{authentication.name}")
    List<Maintenance> findByTechnicienIsCurrentUser();
}
