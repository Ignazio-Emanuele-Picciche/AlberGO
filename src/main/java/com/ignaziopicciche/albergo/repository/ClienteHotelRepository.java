package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.ClienteHotel;
import com.ignaziopicciche.albergo.model.ClienteHotelPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteHotelRepository extends JpaRepository<ClienteHotel, ClienteHotelPK> {

    ClienteHotel findByCliente_IdAndHotel_Id(Long idCliente, Long idHotel);
    ClienteHotel deleteClienteHotelByCliente_Id(Long idCliente);
    List<ClienteHotel> findByCliente_Id(Long idHotel);

}
