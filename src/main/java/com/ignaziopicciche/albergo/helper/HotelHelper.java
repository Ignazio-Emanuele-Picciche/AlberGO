package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.HotelDTO;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.Hotel;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * La classe HotelHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 * -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 * -es. controllare che un hotel è gia presente nel sistema;
 * -es. aggiungere, eliminare, cercare, aggiornare un hotel.
 * -varie operazioni di logica (calcoli, operazioni, controlli generici)
 * -restituire, al front-end, le eccezioni custom in caso di errore (es. L'hotel che vuoi inserire è già presente nel sistema)
 * -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class HotelHelper {

    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final StripeHelper stripeHelper;
    private final ClienteHotelHelper clienteHotelHelper;

    private static HotelEnum hotelEnum;

    public HotelHelper(HotelRepository hotelRepository, ClienteRepository clienteRepository, StripeHelper stripeHelper, ClienteHotelHelper clienteHotelHelper) {
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.stripeHelper = stripeHelper;
        this.clienteHotelHelper = clienteHotelHelper;
    }

    /**
     * Metodo che controlla se l'hotel che si vuole aggiungere non è presente nel sistema.
     * In caso positivo viene aggiunto l'hotel e vengono associati i clienti presenti nel sistema all'hotel.
     * In caso negativo viene restituita un'eccezione custom
     *
     * @param hotelDTO
     * @return Hotel
     * @throws Exception
     */
    public Hotel createHotel(HotelDTO hotelDTO) throws Exception {
        if (!hotelRepository.existsByNomeOrCodiceHotel(hotelDTO.nome, hotelDTO.codiceHotel)) {

            Hotel hotel = new Hotel();

            hotel.setNome(hotelDTO.nome);
            hotel.setDescrizione(hotelDTO.descrizione);
            hotel.setIndirizzo(hotelDTO.indirizzo);
            hotel.setStelle(hotelDTO.stelle);
            hotel.setTelefono(hotelDTO.telefono);
            hotel.setCodiceHotel(hotelDTO.codiceHotel);
            hotel.setPublicKey(hotelDTO.publicKey);

            List<Cliente> clienti = clienteRepository.findAll();

            hotel = hotelRepository.save(hotel);

            if (clienti != null && !clienti.isEmpty()) {
                for (Cliente cliente : clienti) {
                    String customerId = stripeHelper.createCustomer(cliente, hotelDTO.publicKey);
                    clienteHotelHelper.createByCliente(cliente, customerId, hotel);

                    stripeHelper.addClienteHotelCarta(cliente);
                }
            }

            return hotel;
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_AE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che controlla se l'hotel che si vuole cercare è presente nel sistema.
     * In caso positivo viene restituito l'hotell associato all'id
     * In caso negativo viene restituita un'eccezione custom
     *
     * @param id
     * @return Hotel
     */
    public Hotel findById(Long id) {
        if (hotelRepository.existsById(id)) {
            return hotelRepository.findById(id).get();
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che restuisce gli hotel il cui nome inizia per nomehotel
     *
     * @param nomeHotel
     * @return List<Hotel>
     */
    public List<Hotel> findHotelByName(String nomeHotel) {
        return hotelRepository.findHotelByNomeStartingWith(nomeHotel);
    }

    /**
     * Metodo che restituisce gli hotel il cui inidirzzo inizia per indirizzoHotel
     *
     * @param indirizzoHotel
     * @return List<Hotel>
     */
    public List<Hotel> findHotelByIndirizzo(String indirizzoHotel) {
        return hotelRepository.findHotelByIndirizzoStartingWith(indirizzoHotel);
    }

    /**
     * Metodo che controlla se esiste un hotel associato al codiceHotel passato da front-end.
     * In caso positivo restituisce l'hotel associato
     * In caso negativo restituisce un'eccezione custom (L'hotel che stai cercando, tramite codiceHotel, non esiste)
     *
     * @param codiceHotel
     * @return Hotel
     */
    public Hotel findHotelByCodiceHotel(String codiceHotel) {
        if (hotelRepository.existsByCodiceHotel(codiceHotel)) {
            return hotelRepository.findByCodiceHotel(codiceHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_CHNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che restituisce tutti gli hotel presenti nel sistema
     *
     * @return List<Hotel>
     */
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

}
