package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Panne;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Panne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanneRepository extends JpaRepository<Panne, Long>, JpaSpecificationExecutor<Panne> {
    @Query(
        "SELECT panne FROM Panne panne WHERE panne.actif.id IN " +
            "(SELECT assignment.actif.id FROM AffectationActif assignment " +
            "WHERE assignment.affectation.agent.utilisateur.login = ?#{authentication.name} " +
            "AND assignment.statut = com.dgi.gestionactifs.domain.enumeration.StatutAffectation.ACTIVE)"
    )
    Page<Panne> findPannesSurActifsAffectesAuCurrentAgent(Pageable pageable);

    @Query(
        "SELECT COUNT(panne) FROM Panne panne WHERE panne.actif.id IN " +
            "(SELECT assignment.actif.id FROM AffectationActif assignment " +
            "WHERE assignment.affectation.agent.utilisateur.login = ?#{authentication.name} " +
            "AND assignment.statut = com.dgi.gestionactifs.domain.enumeration.StatutAffectation.ACTIVE)"
    )
    long countPannesSurActifsAffectesAuCurrentAgent();
}
