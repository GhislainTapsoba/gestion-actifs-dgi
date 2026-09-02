package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Transfert;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transfert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransfertRepository extends JpaRepository<Transfert, Long>, JpaSpecificationExecutor<Transfert> {
    @Query("select transfert from Transfert transfert where transfert.demandeur.login = ?#{authentication.name}")
    List<Transfert> findByDemandeurIsCurrentUser();

    @Query("select transfert from Transfert transfert where transfert.validateur.login = ?#{authentication.name}")
    List<Transfert> findByValidateurIsCurrentUser();
}
