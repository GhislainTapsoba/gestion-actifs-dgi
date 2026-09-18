package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Actif;
import com.dgi.gestionactifs.domain.enumeration.StatutActif;
import com.dgi.gestionactifs.domain.enumeration.StatutAffectation;
import java.util.List;
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
}
