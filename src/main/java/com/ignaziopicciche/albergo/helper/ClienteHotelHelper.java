package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.ClienteHotel;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.stereotype.Component;

/**
 * La classe ClienteHotelHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 *  -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 *      -es. aggiungere un cliente in un hotel.
 *  -varie operazioni di logica (calcoli, operazioni, controlli generici)
 */

@Component
public class ClienteHotelHelper {

    private final ClienteHotelRepository clienteHotelRepository;
    private final HotelRepository hotelRepository;

    public ClienteHotelHelper(ClienteHotelRepository clienteHotelRepository, HotelRepository hotelRepository) {
        this.clienteHotelRepository = clienteHotelRepository;
        this.hotelRepository = hotelRepository;
    }

    /**
     * Metodo che si occupa di asscoaire un cliente e il suo customerId (id account stripe) a un hotel.
     * @param cliente
     * @param customerId
     * @param hotel
     */
    public void createByCliente(Cliente cliente, String customerId, Hotel hotel) {

        ClienteHotel clienteHotel = ClienteHotel.builder()
                .customerId(customerId)
                .cliente(cliente)
                .hotel(hotel).build();

        clienteHotelRepository.save(clienteHotel);

    }

}
