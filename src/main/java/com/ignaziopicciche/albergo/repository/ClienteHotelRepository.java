package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.ClienteHotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteHotelRepository extends JpaRepository<ClienteHotel, Long> {

    ClienteHotel findByCliente_IdAndHotel_Id(Long idCliente, Long idHotel);
    List<ClienteHotel> findByCliente_Id(Long idCliente);

    Boolean existsByCliente_Id(Long idCliente);
    void deleteAllByCliente_Id(Long idCliente);

}
