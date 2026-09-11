package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.HistoriqueAction;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoriqueAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction, Long>, JpaSpecificationExecutor<HistoriqueAction> {
    @Query(
        "select historiqueAction from HistoriqueAction historiqueAction where historiqueAction.utilisateur.login = ?#{authentication.name}"
    )
    List<HistoriqueAction> findByUtilisateurIsCurrentUser();
}
