package com.ignaziopicciche.albergo.security.repositories;

import com.ignaziopicciche.albergo.security.models.Amministratore;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmministratoreRepository extends JpaRepository<Amministratore, Long>{

    Amministratore findAmministratoreByUsername(String username);

    Boolean existsByUsername(String username);
}
