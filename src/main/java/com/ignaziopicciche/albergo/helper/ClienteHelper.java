package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.exception.ClienteException;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteHelper {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HotelRepository hotelRepository;


    public ClienteDTO create(ClienteDTO clienteDTO){

        if(!clienteRepository.existsByDocumentoAndHotel_Id(clienteDTO.documento, clienteDTO.idHotel) &&
                !clienteDTO.documento.equals("")){

            Cliente cliente = new Cliente();

            cliente.setNome(clienteDTO.nome);
            cliente.setCognome(clienteDTO.cognome);
            cliente.setDocumento(clienteDTO.documento);
            cliente.setTelefono(clienteDTO.telefono);
            cliente.setHotel(hotelRepository.findById(clienteDTO.idHotel).get());

            clienteRepository.save(cliente);
            return new ClienteDTO(cliente);
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ALREADY_EXISTS);
    }



    public ClienteDTO update(ClienteDTO clienteDTO){

        if(clienteRepository.existsById(clienteDTO.id)){

            Cliente cliente = clienteRepository.findById(clienteDTO.id).get();

            cliente.setNome(clienteDTO.nome);
            cliente.setCognome(clienteDTO.cognome);
            cliente.setTelefono(clienteDTO.telefono);

            clienteRepository.save(cliente);
            return new ClienteDTO(cliente);
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_NOT_FOUND);
    }


    public Boolean delete(Long id){

        if(clienteRepository.existsById(id)){
            try{
                clienteRepository.deleteById(id);
                return true;
            }catch (Exception e){
                throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_DELETE_ERROR);
            }
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ID_NOT_EXIST);
    }


    public ClienteDTO findById(Long id){
        if(clienteRepository.existsById(id)){
            return new ClienteDTO(clienteRepository.findById(id).get());
        }

        throw new ClienteException(ClienteException.ClienteExcpetionCode.CLIENTE_ID_NOT_EXIST);
    }

    public List<ClienteDTO> findAll(Long idHotel){
        if(hotelRepository.existsById(idHotel)){
            return clienteRepository.findClientesByHotel_Id(idHotel).stream().map(x -> new ClienteDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }
}
