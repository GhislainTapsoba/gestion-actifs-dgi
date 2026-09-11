package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Bordereau;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bordereau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BordereauRepository extends JpaRepository<Bordereau, Long>, JpaSpecificationExecutor<Bordereau> {
    @Query("select bordereau from Bordereau bordereau where bordereau.emetteur.login = ?#{authentication.name}")
    List<Bordereau> findByEmetteurIsCurrentUser();
}
