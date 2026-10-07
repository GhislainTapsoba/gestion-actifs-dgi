package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.StatutAffectation;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Actif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActifRepository extends JpaRepository<Actif, Long>, JpaSpecificationExecutor<Actif> {
    @Query("SELECT a FROM Actif a WHERE a.etat = :statut")
    List<Actif> findByStatut(@Param("statut") StatutActif statut);

    List<Actif> findByEtat(StatutActif etat);

    @Query(
        "SELECT a FROM Actif a WHERE a NOT IN " +
            "(SELECT aa.actif FROM AffectationActif aa WHERE aa.statut = com.dgi.gestionactifs.domain.enumeration.StatutAffectation.ACTIVE)"
    )
    List<Actif> findEquipementsNonAffectes();

    @Query(
        "SELECT DISTINCT assignment.actif FROM AffectationActif assignment " +
            "WHERE assignment.affectation.agent.utilisateur.login = ?#{authentication.name} " +
            "AND assignment.statut = com.dgi.gestionactifs.domain.enumeration.StatutAffectation.ACTIVE"
    )
    Page<Actif> findActifsAffectesAuCurrentAgent(Pageable pageable);

    @Query(
        "SELECT COUNT(DISTINCT assignment.actif.id) FROM AffectationActif assignment " +
            "WHERE assignment.affectation.agent.utilisateur.login = ?#{authentication.name} " +
            "AND assignment.statut = com.dgi.gestionactifs.domain.enumeration.StatutAffectation.ACTIVE"
    )
    long countActifsAffectesAuCurrentAgent();

    @Query(
        "SELECT CASE WHEN COUNT(assignment) > 0 THEN TRUE ELSE FALSE END FROM AffectationActif assignment " +
            "WHERE assignment.affectation.agent.utilisateur.login = ?#{authentication.name} " +
            "AND assignment.statut = com.dgi.gestionactifs.domain.enumeration.StatutAffectation.ACTIVE " +
            "AND assignment.actif.id = :actifId"
    )
    boolean isActifAffecteAuCurrentAgent(@Param("actifId") Long actifId);
}
