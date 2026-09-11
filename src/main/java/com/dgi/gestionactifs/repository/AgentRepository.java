package com.dgi.gestionactifs.repository;

import com.dgi.gestionactifs.domain.Agent;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Agent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgentRepository extends JpaRepository<Agent, Long>, JpaSpecificationExecutor<Agent> {
    @Query("select agent from Agent agent where agent.utilisateur.login = ?#{authentication.name}")
    List<Agent> findByUtilisateurIsCurrentUser();
}
