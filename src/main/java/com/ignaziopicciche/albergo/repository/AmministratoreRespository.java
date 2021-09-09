package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Amministratore;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AmministratoreRespository extends JpaRepository<Amministratore, Long> {

    Amministratore findAmministratoreByUsername(String username);
}
