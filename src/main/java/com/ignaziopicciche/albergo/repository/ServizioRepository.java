package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Servizio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ServizioRepository extends JpaRepository<Servizio, Long> {

    Boolean existsByNome(String nome);
    List<Servizio> findAllByHotel_Id(Long idHotel);

    Boolean existsServizioByIdAndHotel_Id(Long idServizio, Long idHotel);

}
