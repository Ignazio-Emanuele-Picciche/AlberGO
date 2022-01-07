package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.exception.enums.ServizioEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.ClienteHotelRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.ServizioRepository;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * La classe ServizioHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 * -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 * -es. controllare che un servzio è gia presente nel sistema;
 * -es. aggiungere, eliminare, cercare, aggiornare un servizio.
 * -varie operazioni di logica (calcoli, operazioni, controlli generici)
 * -restituire, al front-end, le eccezioni custom in caso di errore (es. Il servizio che vuoi inserire è già presente nel sistema)
 * -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class ServizioHelper {

    private final ServizioRepository servizioRepository;
    private final HotelRepository hotelRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final ClienteHotelRepository clienteHotelRepository;
    private final StripeHelper stripeHelper;

    private static ServizioEnum servizioEnum;
    private static HotelEnum hotelEnum;
    private static PrenotazioneEnum prenotazioneEnum;

    public ServizioHelper(ServizioRepository servizioRepository, HotelRepository hotelRepository, PrenotazioneRepository prenotazioneRepository, ClienteHotelRepository clienteHotelRepository, StripeHelper stripeHelper) {
        this.servizioRepository = servizioRepository;
        this.hotelRepository = hotelRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.clienteHotelRepository = clienteHotelRepository;
        this.stripeHelper = stripeHelper;
    }

    /**
     * Metodo che, dopo aver controllato che il servizio è presente nel sistema, restituisce un servizio associato all'id passato
     * Nel caso in cui non è presente nel sistema restituisce un'eccezione custom
     *
     * @param id
     * @return Servizio
     */
    public Servizio findById(Long id) {
        if (servizioRepository.existsById(id)) {
            return servizioRepository.findById(id).get();
        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato che l'hotel esiste nel sistema, restituisce tutti i servizi presenti in quell'hotel
     * Nel caso l'hotel non dovesse esistere viene restituita un'eccezione custom.
     *
     * @param idHotel
     * @return List<Servizio>
     */
    public List<Servizio> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return servizioRepository.findAllByHotel_Id(idHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver verificato che il servizio che si vuole aggiungere non è presente nel sistema,
     * viene aggiunto il nuovo servizio dell'hotel.
     * In caso di errore viene restituita un'eccezione custom
     *
     * @param servizioDTO
     * @return Servizio
     */
    public Servizio createServizio(ServizioDTO servizioDTO) {

        if (!servizioRepository.existsByNomeAndHotel_Id(servizioDTO.nome, servizioDTO.idHotel)) {
            Servizio servizio = Servizio.builder()
                    .nome(servizioDTO.nome)
                    .prezzo(servizioDTO.prezzo)
                    .hotel(hotelRepository.findById(servizioDTO.idHotel).get()).build();

            servizio = servizioRepository.save(servizio);
            return servizio;

        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_AE");
        throw new ApiRequestException(servizioEnum.getMessage());

    }

    /**
     * Metodo che, dopo aver controllato che il servizio è presente nel sistema, lo elimina.
     * Nel caso in cui non dovesse esistere o in caso di problemi durante l'eliminazione viene restituita un'eccezione custom
     *
     * @param id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        if (servizioRepository.existsById(id)) {
            try {
                servizioRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_DLE");
                throw new ApiRequestException(servizioEnum.getMessage());
            }
        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_NF");
        throw new ApiRequestException(servizioEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver verificato che il servizio è presente nel sistema, aggiorna i campi "editabili" da front-end.
     * Nel caso il servizio non dovesse esistere viene resituita un'eccezione custom (Il servizio che stai cercando non esiste)
     *
     * @param servizioDTO
     * @return idServizio
     */
    public Long update(ServizioDTO servizioDTO) {
        if (servizioRepository.existsById(servizioDTO.id)) {

            Servizio servizio = servizioRepository.findById(servizioDTO.id).get();
            servizio.setNome(servizioDTO.nome);
            servizio.setPrezzo(servizioDTO.prezzo);

            servizio = servizioRepository.save(servizio);

            return servizio.getId();
        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato il servizio e la prenotazione sono associati allo stesso hotel, inserisce il servizio
     * selezionalo nella prenotazione presente.
     * Inoltre al cliente viene addebitato il costo del servizio appena viene aggiunto alla prenotazione.
     * Nel caso in cui le associazioni non corrispondono viene restituita un'eccezione custom
     *
     * @param idServizio
     * @param idPrenotazione
     * @param idHotel
     * @return idServizio
     * @throws StripeException
     */
    public Long insertByPrentazioneAndHotel(Long idServizio, Long idPrenotazione, Long idHotel) throws StripeException {
        if (servizioRepository.existsServizioByIdAndHotel_Id(idServizio, idHotel)
                && prenotazioneRepository.existsPrenotazioneByIdAndHotel_Id(idPrenotazione, idHotel)) {

            Prenotazione prenotazione = prenotazioneRepository.findById(idPrenotazione).get();
            Servizio servizio = servizioRepository.findById(idServizio).get();
            Cliente cliente = prenotazione.getCliente();

            if (!prenotazione.getServizi().contains(servizio)) {
                servizio.addPrenotazione(prenotazione);

                String price = Double.toString(servizio.getPrezzo());
                if (price.indexOf(".") == price.length() - 2) {
                    StringBuilder priceS = new StringBuilder(price);
                    priceS.append("0");
                    price = priceS.toString();
                }
                price = price.replace(".", "");

                Hotel hotel = prenotazione.getHotel();
                ClienteHotel clienteHotel = clienteHotelRepository.findByCliente_IdAndHotel_Id(cliente.getId(), hotel.getId());

                PaymentData paymentData = PaymentData.builder()
                        .customerId(clienteHotel.getCustomerId())
                        .key(hotel.getPublicKey())
                        .price(price)
                        .paymentMethod(clienteHotel.getPaymentMethodId())
                        .description("Servizio " + servizio.getNome()).build();
                stripeHelper.createPaymentIntent(paymentData);

                servizio = servizioRepository.save(servizio);
                return servizio.getId();
            }

            throw new ApiRequestException("Servizio già presente in questa prenotazione");

        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage() + ", oppure la prenotazione o l'hotel che stai cercando non esistono");

    }

    /**
     * Metodo che, dopo aver controllato che la prenotazione è presente nel sistema, restituisce tutti i servizi dell'hotel, in cui è
     * stata effettuata la prenotazione, che non sono stati inseriti nella prenotazione.
     * In caso di errore restituisce un'eccezione custom
     *
     * @param idPrenotazione
     * @return List<Servizio>
     */
    public List<Servizio> findNotInByPrenotazione(Long idPrenotazione) {
        if (prenotazioneRepository.existsById(idPrenotazione)) {
            List<Servizio> serviziPrenotazione = prenotazioneRepository.findById(idPrenotazione).get().getServizi();
            List<Servizio> serviziTotali = servizioRepository.findAllByHotel_Id(prenotazioneRepository.findById(idPrenotazione).get().getHotel().getId());

            List<Servizio> serviziNotInPrenotazione = new ArrayList<>();

            boolean check;
            for (Servizio st : serviziTotali) {
                check = false;
                for (Servizio sp : serviziPrenotazione) {
                    if (Objects.equals(st.getId(), sp.getId())) {
                        check = true;
                    }
                }

                if (!check) {
                    serviziNotInPrenotazione.add(st);
                }
            }


            return serviziNotInPrenotazione;
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());

    }

    /**
     * Metodo che, dopo aver verificato che la prenotazione e il servizio associato esistono, rimuove il servizio dalla prenotazione.
     * In caso di errori, es. non esiste la prenotazione o il servizio, viene restituita un'eccezione custom
     *
     * @param idServizio
     * @param idPrenotazione
     * @return Boolean
     */
    public Boolean removeServizioInPrenotazione(Long idServizio, Long idPrenotazione) {

        if (servizioRepository.existsById(idServizio)) {
            if (prenotazioneRepository.existsById(idPrenotazione)) {
                try {
                    Prenotazione prenotazione = prenotazioneRepository.findById(idPrenotazione).get();
                    Servizio servizio = servizioRepository.findById(idServizio).get();
                    if (servizio.getPrenotazioni().contains(prenotazione)) {
                        servizio.removePrenotazione(prenotazione);
                        servizioRepository.save(servizio);
                        return true;
                    }
                } catch (Exception e) {
                    servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_DLE");
                    throw new ApiRequestException(servizioEnum.getMessage());
                }

                throw new ApiRequestException("La prenotazione non contiene il servizio che si vuole rimuovere");
            }
            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage());

    }

    /**
     * Metodo che, dopo aver verificato che la prenotazione esiste nel sistema, ritorna tutti i servizi aggiunti in quella prenotazione
     * In caso di errore restituisce un'eccezione custom
     *
     * @param idPrenotazione
     * @return List<Servizio>
     */
    public List<Servizio> findServiziInPrenotazione(Long idPrenotazione) {
        if (prenotazioneRepository.existsById(idPrenotazione)) {
            Prenotazione prenotazione = prenotazioneRepository.findById(idPrenotazione).get();
            return prenotazione.getServizi();
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


}
