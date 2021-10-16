package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    Boolean existsByNome(String nome);
    List<Servizio> findAllByHotel_Id(Long idHotel);
}
