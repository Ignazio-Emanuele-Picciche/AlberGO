package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.exception.ClienteException;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class ClienteHelper {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HotelRepository hotelRepository;


    public Long create(ClienteDTO clienteDTO) {

        //exists by username, documento
        if (!clienteRepository.existsByDocumentoOrUsername(clienteDTO.documento, clienteDTO.username) &&
                !clienteDTO.documento.equals("") && !clienteDTO.username.equals("")) {

            Cliente cliente = Cliente.builder()
                    .nome(clienteDTO.nome)
                    .cognome(clienteDTO.cognome)
                    .documento(clienteDTO.documento)
                    .telefono(clienteDTO.telefono)
                    .username(clienteDTO.username)
                    .password(clienteDTO.password).build();

            cliente = clienteRepository.save(cliente);
            return cliente.getId();
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ALREADY_EXISTS);
    }


    public ClienteDTO update(ClienteDTO clienteDTO) {

        if (clienteRepository.existsById(clienteDTO.id)) {

            Cliente cliente = clienteRepository.findById(clienteDTO.id).get();

            cliente.setNome(clienteDTO.nome);
            cliente.setCognome(clienteDTO.cognome);
            cliente.setTelefono(clienteDTO.telefono);

            clienteRepository.save(cliente);
            return new ClienteDTO(cliente);
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_NOT_FOUND);
    }


    public Boolean delete(Long id) {

        if (clienteRepository.existsById(id)) {
            try {
                clienteRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_DELETE_ERROR);
            }
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ID_NOT_EXIST);
    }


    public ClienteDTO findById(Long id) {
        if (clienteRepository.existsById(id)) {
            return new ClienteDTO(clienteRepository.findById(id).get());
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ID_NOT_EXIST);
    }


    //Cercare clienti per hotel tramite prenotazione
    public List<ClienteDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return clienteRepository.findClientiByHotel_Id(idHotel).stream().map(x -> new ClienteDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }
}
