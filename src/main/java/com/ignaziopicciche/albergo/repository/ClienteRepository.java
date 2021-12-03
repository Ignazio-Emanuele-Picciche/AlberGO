package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    //Cercare i clienti per idHotel tramite prenotazione
    @Query("select distinct(p.cliente) from Prenotazione p where p.hotel.id = :idHotel")
    List<Cliente> findClientiByHotel_Id(@Param("idHotel") Long idHotel);

    Boolean existsByDocumentoOrUsername(String documento, String username);

    @Query("select distinct(p.cliente) from Prenotazione p where p.hotel.id = :idHotel and p.cliente.nome like :nome%")
    List<Cliente> findClientesByNomeStartingWith(@Param("nome") String nome, @Param("idHotel") Long idHotel);

    @Query("select distinct(p.cliente) from Prenotazione p where p.hotel.id = :idHotel and p.cliente.cognome like :cognome%")
    List<Cliente> findClientesByCognomeStartingWith(@Param("cognome") String cognome, @Param("idHotel") Long idHotel);

    Cliente findClienteByUsername(String username);
}
