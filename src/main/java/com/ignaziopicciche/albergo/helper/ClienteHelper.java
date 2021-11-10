package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.enums.ClienteEnum;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteHelper {

    private final ClienteRepository clienteRepository;
    private final HotelRepository hotelRepository;
    private final StripeHelper stripeHelper;

    private static ClienteEnum clienteEnum;
    private static HotelEnum hotelEnum;

    public ClienteHelper(ClienteRepository clienteRepository, HotelRepository hotelRepository, StripeHelper stripeHelper) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
        this.stripeHelper = stripeHelper;
    }

    public Long create(ClienteDTO clienteDTO) throws StripeException {

        if (!clienteRepository.existsByDocumentoOrUsername(clienteDTO.documento, clienteDTO.username) &&
                !clienteDTO.documento.equals("") && !clienteDTO.username.equals("")) {

            Cliente cliente = Cliente.builder()
                    .nome(clienteDTO.nome)
                    .cognome(clienteDTO.cognome)
                    .documento(clienteDTO.documento)
                    .telefono(clienteDTO.telefono)
                    .username(clienteDTO.username)
                    .password(clienteDTO.password).build();

            cliente = stripeHelper.createCustomer(cliente);

            cliente = clienteRepository.save(cliente);
            return cliente.getId();
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_AE");
        throw new ApiRequestException(clienteEnum.getMessage());
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

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_NF");
        throw new ApiRequestException(clienteEnum.getMessage());
    }


    public Boolean delete(Long id) {

        if (clienteRepository.existsById(id)) {
            try {
                stripeHelper.deleteCustomerById(clienteRepository.findById(id).get().getCustomerId());
                clienteRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_DLE");
                throw new ApiRequestException(clienteEnum.getMessage());
            }
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_IDNE");
        throw new ApiRequestException(clienteEnum.getMessage());
    }


    public ClienteDTO findById(Long id) {
        if (clienteRepository.existsById(id)) {
            return new ClienteDTO(clienteRepository.findById(id).get());
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_IDNE");
        throw new ApiRequestException(clienteEnum.getMessage());
    }


    public List<ClienteDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return clienteRepository.findClientiByHotel_Id(idHotel).stream().map(x -> new ClienteDTO(x)).collect(Collectors.toList());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    public List<ClienteDTO> findAllByNomeCognome(String nome, String cognome) {
        List<Cliente> clienti;

        if (cognome == null && nome != null) {
            clienti = clienteRepository.findClientesByNomeStartingWith(nome);
            return clienti.stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
        }else if(nome == null && cognome != null){
            clienti = clienteRepository.findClientesByCognomeStartingWith(cognome);
            return clienti.stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_NF");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

}
