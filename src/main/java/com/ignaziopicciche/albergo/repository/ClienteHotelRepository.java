package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.ClienteHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteHotelRepository extends JpaRepository<ClienteHotel, Long> {

    ClienteHotel findByCliente_IdAndHotel_Id(Long idCliente, Long idHotel);
    List<ClienteHotel> findByCliente_Id(Long idCliente);

    void deleteAllByCliente_Id(@Param("idCliente") Long idCliente);

}
