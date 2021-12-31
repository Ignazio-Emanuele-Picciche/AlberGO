package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.exception.enums.ClienteEnum;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.ClienteHotel;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.AmministratoreRepository;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La classe ClienteHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 *  -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 *      -es. controllare che un cliente è gia presente nel sistema;
 *      -es. aggiungere, cercare un cliente.
 *  -varie operazioni di logica (calcoli, operazioni, controlli generici)
 *  -restituire, al front-end, le eccezioni custom in caso di errore (es. Il cliente che vuoi inserire è già presente nel sistema)
 *  -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class ClienteHelper{

    private final ClienteRepository clienteRepository;
    private final HotelRepository hotelRepository;
    private final ClienteHotelRepository clienteHotelRepository;
    private final AmministratoreRepository amministratoreRepository;
    private final StripeHelper stripeHelper;
    private final ClienteHotelHelper clienteHotelHelper;

    private final PasswordEncoder passwordEncoder;

    private static ClienteEnum clienteEnum;
    private static HotelEnum hotelEnum;

    public ClienteHelper(ClienteRepository clienteRepository, HotelRepository hotelRepository, StripeHelper stripeHelper, ClienteHotelRepository clienteHotelRepository, ClienteHotelHelper clienteHotelHelper, AmministratoreRepository amministratoreRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.hotelRepository = hotelRepository;
        this.stripeHelper = stripeHelper;
        this.clienteHotelRepository = clienteHotelRepository;
        this.clienteHotelHelper = clienteHotelHelper;
        this.amministratoreRepository = amministratoreRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Metodo che si occupa di controllare che il cliente che si vuole aggiungere non è già presente nel sistema.
     * In caso positivo viene aggiunto il cliente nel sistema.
     * In caso negativo viene restituita un'eccezione custom al front-end
     * @param clienteDTO
     * @return idCliente
     * @throws Exception
     */
    public Long create(ClienteDTO clienteDTO) throws Exception {

        if (!clienteRepository.existsByDocumentoOrUsername(clienteDTO.documento, clienteDTO.username) &&
                !amministratoreRepository.existsByUsername(clienteDTO.username)) {

            Cliente cliente = Cliente.builder()
                    .nome(clienteDTO.nome)
                    .cognome(clienteDTO.cognome)
                    .documento(clienteDTO.documento)
                    .telefono(clienteDTO.telefono)
                    .username(clienteDTO.username)
                    .password(passwordEncoder.encode(clienteDTO.password))
                    .ruolo("ROLE_USER").build();

            cliente = clienteRepository.save(cliente);

            List<Hotel> hotels = hotelRepository.findAll();

            if (!hotels.isEmpty()) {
                for (Hotel hotel : hotels) {
                    String customerId = stripeHelper.createCustomer(cliente, hotel.getPublicKey());
                    clienteHotelHelper.createByCliente(cliente, customerId, hotel);

                    stripeHelper.addClienteHotelCarta(cliente);
                }
            }

            return cliente.getId();
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_AE");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

    /**
     * Metodo che controlla se il cliente che si vuole aggiornare è presente nel sistema.
     * In caso positivo vengono aggiornati i campi "editabili".
     * In caso negativo viene restituita un'eccezione custom al front-end
     * @param clienteDTO
     * @return ClienteDTO
     */
    /*public ClienteDTO update(ClienteDTO clienteDTO) {

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
    }*/

    /**
     * Metodo che controlla se il cliente che si vuole rimuovere dal sistema esiste.
     * In caso positivo viene eliminato il cliente.
     * In caso negativo viene restituita un'eccezione custom al front-end.
     * @param idCliente
     * @return Boolean
     *
     * @Transactional è un'annotazione Spring per la gestione dichiarativa delle transazioni,
     * ovvero definisce quali istruzioni SQL vengono eseguite insieme all'interno di una transazione di database.
     */
    /*@Transactional
    public Boolean delete(Long idCliente) {

        if (clienteRepository.existsById(idCliente)) {
            try {
                List<ClienteHotel> clientiHotel = clienteHotelRepository.findByCliente_Id(idCliente);
                stripeHelper.deleteCustomerById(clientiHotel);
                clienteHotelRepository.deleteAllByCliente_Id(idCliente);

                clienteRepository.deleteById(idCliente);
                return true;
            } catch (Exception e) {
                clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_DLE");
                throw new ApiRequestException(clienteEnum.getMessage());
            }
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_IDNE");
        throw new ApiRequestException(clienteEnum.getMessage());
    }*/

    /**
     * Metodo che controlla se il cliente che si vuole cercare esiste nel sistema.
     * In caso positivo viene restituito il cliente cercato per idCliente
     * In caso negativo viene restituita al front-end un'eccezione custom
     * @param id
     * @return Cliente
     */
    public Cliente findById(Long id) {
        if (clienteRepository.existsById(id)) {
            return clienteRepository.findById(id).get();
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_IDNE");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

    /**
     * Metodo che controlla se l'hotel, per il quale si vogliono tornare tutti i clienti associati, è presente nel sistema.
     * In caso positivo vengono restituiti tutti i clienti cercati con la logica poco fa citata.
     * In caso negativo viene restituita un'eccezione custom
     * @param idHotel
     * @return List<Cliente>
     */
    public List<Cliente> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return clienteRepository.findClientiByHotel_Id(idHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che controlla che nome, cognome non siano null e che esiste il cliente associato per quell'hotel
     * In caso positivo vengono restituiti tutti che iniziano per nome o cognome
     * In caso negativo restituisce un'eccezione custom
     * @param nome
     * @param cognome
     * @param idHotel
     * @return List<Cliente>
     */
    public List<Cliente> findAllByNomeCognome(String nome, String cognome, Long idHotel) {
        List<Cliente> clienti;

        if (cognome == null && nome != null) {
            clienti = clienteRepository.findClientesByNomeStartingWith(nome, idHotel);
            return clienti;
        } else if (nome == null && cognome != null) {
            clienti = clienteRepository.findClientesByCognomeStartingWith(cognome, idHotel);
            return clienti;
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_NF");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

    /**
     * Metodo che controlla se esiste un cliente associato allo username passato da front-end
     * In caso positivo restituisce il cliente associato
     * In caso negativo restituisce un'eccezione custom (Il cliente che stai cercando non è stato trovato)
     * @param username
     * @return ClienteDTO
     */
    public Cliente findClienteByUsername(String username){
        if(clienteRepository.existsByUsername(username)){
            Cliente cliente = clienteRepository.findByUsername(username);
            return cliente;
        }

        clienteEnum = ClienteEnum.getClienteEnumByMessageCode("CLI_NF");
        throw new ApiRequestException(clienteEnum.getMessage());
    }

}
