package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Rapport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Rapport entity.
 */
@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long> {
    default Optional<Rapport> findOneWithEagerRelationships(Long id) {
        return this.findById(id);
    }

    default List<Rapport> findAllWithEagerRelationships() {
        return this.findAll();
    }

    default Page<Rapport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAll(pageable);
    }

    @Query("SELECT r FROM Rapport r WHERE r.generePar = :username")
    Page<Rapport> findByGenerePar(@Param("username") String username, Pageable pageable);

    @Query("SELECT r FROM Rapport r WHERE r.typeRapport = :typeRapport")
    Page<Rapport> findByTypeRapport(@Param("typeRapport") String typeRapport, Pageable pageable);
}
