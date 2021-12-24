package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Amministratore;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * L'interfaccia AmministratoreRepository estende JpaRepository<T, ID>.
 * Quest'interfaccia, utilizzata nelle classi helper, offre diversi metodi base (es. findAll(), findById() e tanti altri)
 * per relazionarsi con il db.
 * Invece, se si ha bisogno di query SQL native si può utilizzare l’annotazione @Query.
 */

public interface AmministratoreRepository extends JpaRepository<Amministratore, Long>{

    Amministratore findByUsername(String username);

    Boolean existsByUsername(String username);
}
