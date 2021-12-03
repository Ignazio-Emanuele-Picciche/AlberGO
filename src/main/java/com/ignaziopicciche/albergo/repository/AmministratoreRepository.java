package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Amministratore;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmministratoreRepository extends JpaRepository<Amministratore, Long>{

    Amministratore findAmministratoreByUsername(String username);

    Boolean existsByUsername(String username);
}
