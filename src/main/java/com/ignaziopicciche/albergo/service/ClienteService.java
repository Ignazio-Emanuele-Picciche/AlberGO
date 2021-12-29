package com.ignaziopicciche.albergo.service;

import com.ignaziopicciche.albergo.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {
    Long create(ClienteDTO clienteDTO) throws Exception;
    ClienteDTO findById(Long id);
    List<ClienteDTO> findAll(Long idHotel);
    List<ClienteDTO> findAllByNomeCognome(String nome, String cognome, Long idHotel);
    ClienteDTO findClienteByUsername(String username);
}
