package com.ignaziopicciche.albergo.repository;

import com.ignaziopicciche.albergo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Boolean existsByDocumentoAndHotel_Id(String documento, Long idHotel);

    List<Cliente> findClientesByHotel_Id(Long idHotel);
}
