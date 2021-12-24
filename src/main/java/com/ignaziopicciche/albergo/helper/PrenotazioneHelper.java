package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.exception.enums.StanzaEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.*;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * La classe PrenotazioneHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 *  -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 *      -es. controllare che una prenotazione è gia presente nel sistema;
 *      -es. aggiungere, eliminare, cercare una prenotazione.
 *  -varie operazioni di logica (calcoli, operazioni, controlli generici)
 *  -restituire, al front-end, le eccezioni custom in caso di errore (es. La prenotazione che vuoi inserire è già presente nel sistema)
 *  -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class PrenotazioneHelper {

    private final PrenotazioneRepository prenotazioneRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final StanzaRepository stanzaRepository;
    private final CategoriaRepository categoriaRepository;
    private final ClienteHotelRepository clienteHotelRepository;
    private final StripeHelper stripeHelper;

    private static PrenotazioneEnum prenotazioneEnum;
    private static HotelEnum hotelEnum;
    private static StanzaEnum stanzaEnum;

    public PrenotazioneHelper(PrenotazioneRepository prenotazioneRepository, HotelRepository hotelRepository, ClienteRepository clienteRepository, StanzaRepository stanzaRepository, CategoriaRepository categoriaRepository, ClienteHotelRepository clienteHotelRepository, StripeHelper stripeHelper) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.stanzaRepository = stanzaRepository;
        this.categoriaRepository = categoriaRepository;
        this.clienteHotelRepository = clienteHotelRepository;
        this.stripeHelper = stripeHelper;
    }

    /**
     * Metodo che controlla se la prenotazione che si vuole cercare è presente nel sistema.
     * In caso positivo restituisce la prenotazione associata all'id
     * In caso negativo restituisce un'eccezione custom (La prenotazione che stai cercando non esiste)
     * @param id
     * @return PrenotazioneDTO
     */
    public PrenotazioneDTO findById(Long id) {
        if (prenotazioneRepository.existsById(id)) {
            return new PrenotazioneDTO(prenotazioneRepository.findById(id).get());
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

    /**
     * Metodo che controllare se l'hotel, per il quale si vogliono ritornare tutte le fatture delle prenotazioni associate, esiste
     * In caso positivo restituisce tutte le fatture cercate con la logica poco fa citata
     * In caso negativo restituisce un'eccezione custom
     * @param idHotel
     * @return List<FatturaDTO>
     */
    public List<FatturaDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByHotel_Id(idHotel);
            List<FatturaDTO> fatture;

            fatture = convertPrenotazioneToFattura(prenotazioni);

            return fatture;
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato che il cliente è presente nel sistema, ritorna tutte le fattura associate a quel cliente
     * @param idCliente
     * @return List<FatturaDTO>
     */
    public List<FatturaDTO> findAllFatture(Long idCliente) {
        if (clienteRepository.existsById(idCliente)) {


            Cliente cliente = clienteRepository.findById(idCliente).get();
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByCliente_Id(idCliente);
            List<FatturaDTO> fattureList = new ArrayList<>();

            for (Prenotazione p : prenotazioni) {
                Hotel hotel = p.getHotel();
                Stanza stanza = p.getStanza();
                Categoria categoria = p.getStanza().getCategoria();

                fattureList.add(new FatturaDTO(p, cliente, stanza, categoria, hotel));
            }

            return fattureList;
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato se la prenotazione è presente nel sistema, la elimina.
     * Inoltre viene controllato tutto il sistema delle penali. Ovvero se si rientra nei "giorni penale" (es 10 giorni prima del check-in)
     * si può cancellare la prenotazione ma c'è una penale da pagare. Oppure se si rientra nei "giorni blocco"
     * (es. 3 giorni prima del check-in) non si può piu cancellare la prenotazione.
     * @param idPrenotazione
     * @return Boolean
     * @throws StripeException
     */
    public Boolean delete(Long idPrenotazione) throws StripeException {
        if (prenotazioneRepository.existsById(idPrenotazione)) {

            Prenotazione prenotazione = prenotazioneRepository.getById(idPrenotazione);
            Categoria categoria = prenotazione.getStanza().getCategoria();
            LocalDate dataInizio = LocalDate.parse(prenotazione.getDataInizio().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));


            long diffDataInizioPrenotazione = ChronoUnit.DAYS.between(LocalDate.now(), dataInizio);

            if (diffDataInizioPrenotazione > categoria.getGiorniPenale()) {
                try {
                    prenotazioneRepository.deleteById(idPrenotazione);
                    return true;
                } catch (Exception e) {
                    prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DLE");
                    throw new ApiRequestException(prenotazioneEnum.getMessage());
                }
            } else if (diffDataInizioPrenotazione > categoria.getGiorniBlocco() && diffDataInizioPrenotazione <= categoria.getGiorniPenale()) {
                Cliente cliente = prenotazioneRepository.findById(idPrenotazione).get().getCliente();
                String price = Double.toString(categoria.getQtaPenale());

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
                        .description("Prenotazione eliminata "+cliente.getNome()+" "+cliente.getCognome()+" "+prenotazione.getDataInizio()+" "+prenotazione.getDataFine()).build();
                stripeHelper.createPaymentIntent(paymentData);

                try {
                    prenotazioneRepository.deleteById(idPrenotazione);
                    return true;
                } catch (Exception e) {
                    prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DLE");
                    throw new ApiRequestException(prenotazioneEnum.getMessage());
                }

            } else if (diffDataInizioPrenotazione <= categoria.getGiorniBlocco()) {
                return false;
            }

        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_NF");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

    /**
     * Prenotazione che, dovo aver controllato che la prenotazione passata non essiste nel sistema, aggiunge la nuova prenotazione.
     * Per "prenotazione che esiste nel sistema" si intende che:
     *  -La stanza che si vuole prenotare in un intervallo di date non sia già stata prenotata da un altro cliente;
     *  -La stanza che si vuole prenotare non sia fuori servizio;
     * Se tutto è andato a buon fine viene addebitato il costo della prenotazione al cliente
     * @param prenotazioneDTO
     * @return PrenotazioneDTO
     * @throws StripeException
     * @throws ParseException
     */
    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO) throws StripeException, ParseException {

        String date = LocalDate.now().toString();
        Date dataAttuale = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        if (prenotazioneRepository.checkPrenotazioneDate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.idStanza) == 0
                && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine)
                && !(prenotazioneDTO.dataInizio.equals(dataAttuale) || prenotazioneDTO.dataInizio.before(dataAttuale))) {
            Prenotazione prenotazione = new Prenotazione();

            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);
            prenotazione.setHotel(hotelRepository.findById(prenotazioneDTO.idHotel).get());
            prenotazione.setStanza(stanzaRepository.findById(prenotazioneDTO.idStanza).get());
            prenotazione.setCliente(clienteRepository.findById(prenotazioneDTO.idCliente).get());

            Cliente cliente = clienteRepository.findById(prenotazioneDTO.idCliente).get();
            long days = ChronoUnit.DAYS.between(prenotazione.getDataInizio().toInstant(), prenotazione.getDataFine().toInstant());
            days++;
            String price = Double.toString(prenotazione.getStanza().getCategoria().getPrezzo() * days);
            if (price.indexOf(".") == price.length() - 2) {
                StringBuilder priceS = new StringBuilder(price);
                priceS.append("0");
                price = priceS.toString();
            }
            price = price.replace(".", "");

            LocalDate dataInizioNuova = prenotazioneDTO.dataInizio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dataFineNuova = prenotazioneDTO.dataFine.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Hotel hotel = prenotazione.getHotel();
            ClienteHotel clienteHotel = clienteHotelRepository.findByCliente_IdAndHotel_Id(cliente.getId(), hotel.getId());

            PaymentData paymentData = PaymentData.builder()
                    .customerId(clienteHotel.getCustomerId())
                    .key(hotel.getPublicKey())
                    .price(price)
                    .paymentMethod(clienteHotel.getPaymentMethodId())
                    .description("Prenotazione creata "+cliente.getNome()+" "+cliente.getCognome()+" "+dataInizioNuova+"  "+dataFineNuova).build();
            stripeHelper.createPaymentIntent(paymentData);

            prenotazioneRepository.save(prenotazione);

            return new PrenotazioneDTO(prenotazione);
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DNC");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato che esiste la stanza associata all'idStanza passato, restituisce tutte le
     * prenotazioni effettuate per quella stanza
     * @param idStanza
     * @return List<PrenotazioneDTO>
     */
    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza) {
        if (stanzaRepository.existsById(idStanza)) {
            List<Prenotazione> prenotazioniLista = prenotazioneRepository.findPrenotazionesByStanza_Id(idStanza);
            return prenotazioniLista.stream().map(x -> new PrenotazioneDTO(x)).collect(Collectors.toList());
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_IDNE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato se esiste la prenotazione che si vuole aggiornare, aggiorna i campi della prenotazione
     * e assegna un addebito al cliente, associato alla prenotazione, calcolato in base ai giorni aggiuntivi rispetto alla prenotazione
     * prima che venisse effettuata la modifica
     * Inoltre viene controllato tutto il sistema delle penali. Ovvero se si rientra nei "giorni penale" (es 10 giorni prima del check-in)
     * si può modificare la prenotazione ma c'è una penale da pagare. Oppure se si rientra nei "giorni blocco"
     * (es. 3 giorni prima del check-in) non si possono fare modifiche alla prenotazione
     * @param prenotazioneDTO
     * @return idPrenotazione
     * @throws ParseException
     * @throws StripeException
     */
    public Long update(PrenotazioneDTO prenotazioneDTO) throws ParseException, StripeException {

        String data = LocalDate.now().toString();
        Date dataAttuale = new SimpleDateFormat("yyyy-MM-dd").parse(data);

        LocalDate dataInizioNuova = prenotazioneDTO.dataInizio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dataFineNuova = prenotazioneDTO.dataFine.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (prenotazioneRepository.checkPrenotazioneDateUpdate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.id, prenotazioneDTO.idStanza) == 0
                && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine) &&
                prenotazioneRepository.existsById(prenotazioneDTO.id) &&
                !(prenotazioneDTO.dataInizio.equals(dataAttuale) || prenotazioneDTO.dataInizio.before(dataAttuale))) {


            Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneDTO.id).get();
            Cliente cliente = prenotazione.getCliente();

            LocalDate dataInizio = LocalDate.parse(prenotazione.getDataInizio().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dataFine = LocalDate.parse(prenotazione.getDataFine().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            long durataVecchia = ChronoUnit.DAYS.between(dataInizio, dataFine);

            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);

            /*dataInizio = LocalDate.parse(dataInizioString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dataFine = LocalDate.parse(dataFineString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));*/
            long durataNuova = ChronoUnit.DAYS.between(dataInizioNuova, dataFineNuova);

            if(durataNuova > durataVecchia){
                long restoDaPagare = durataNuova-durataVecchia;

                String price = Double.toString(prenotazione.getStanza().getCategoria().getPrezzo() * restoDaPagare);
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
                        .description("Prenotazione aggiornata "+cliente.getNome()+" "+cliente.getCognome()+" "+dataInizioNuova+"  "+dataFineNuova).build();
                stripeHelper.createPaymentIntent(paymentData);
            }


            prenotazioneRepository.save(prenotazione);
            return prenotazione.getId();

        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DNC");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

    /**
     * Metodo che restituisce tutte le fatture di un hotel dove:
     *  -il nome e/o cognome del cliente, che ha effettuato la prenotazioni, iniziano per nomeCliente e/o cognomeCliente;
     *  -le prenotazioni sono state effettuate in un intervallo di date
     * @param nomeCliente
     * @param cognomeCliente
     * @param dataInizio
     * @param dataFine
     * @param idHotel
     * @return List<FatturaDTO>
     */
    public List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(String nomeCliente, String cognomeCliente, Date dataInizio, Date dataFine, Long idHotel) {
        List<Prenotazione> prenotazioni;

        if (hotelRepository.existsById(idHotel)) {
            if (dataInizio == null && dataFine == null && cognomeCliente == null && nomeCliente != null) {
                prenotazioni = prenotazioneRepository.findPrenotazionesByCliente_NomeStartingWithAndHotel_Id(nomeCliente, idHotel);
                return convertPrenotazioneToFattura(prenotazioni);
            } else if (dataInizio == null && dataFine == null && nomeCliente == null && cognomeCliente != null) {
                prenotazioni = prenotazioneRepository.findPrenotazionesByCliente_CognomeStartingWithAndHotel_Id(cognomeCliente, idHotel);
                return convertPrenotazioneToFattura(prenotazioni);
            } else if (cognomeCliente == null && dataInizio != null && dataFine != null && nomeCliente != null) {
                prenotazioni = prenotazioneRepository.findAllByNomeClienteAndDataInizioAndDataFine(nomeCliente, dataInizio, dataFine, idHotel);
                return convertPrenotazioneToFattura(prenotazioni);
            } else if (nomeCliente == null && dataInizio != null && dataFine != null && cognomeCliente != null) {
                prenotazioni = prenotazioneRepository.findAllByCognomeClienteAndDataInizioAndDataFine(cognomeCliente, dataInizio, dataFine, idHotel);
                return convertPrenotazioneToFattura(prenotazioni);
            } else if (nomeCliente == null && cognomeCliente == null && dataInizio != null && dataFine != null) {
                prenotazioni = prenotazioneRepository.findAllByDataInizioAndDataFine(dataInizio, dataFine, idHotel);
                return convertPrenotazioneToFattura(prenotazioni);
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_NF");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che converte le prenotazioni, passate come parametro, in fatture
     * @param prenotazioni
     * @return List<FatturaDTO>
     */
    public List<FatturaDTO> convertPrenotazioneToFattura(List<Prenotazione> prenotazioni) {
        List<FatturaDTO> fatture = new ArrayList<>();

        for (Prenotazione p : prenotazioni) {
            Cliente cliente = p.getCliente();
            Stanza stanza = p.getStanza();
            Categoria categoria = p.getStanza().getCategoria();
            Hotel hotel = p.getHotel();

            fatture.add(new FatturaDTO(p, cliente, stanza, categoria, hotel));
        }
        return fatture;
    }
}
