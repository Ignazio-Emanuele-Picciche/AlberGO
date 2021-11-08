package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.enums.StanzaEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.*;
import com.ignaziopicciche.albergo.repository.*;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrenotazioneHelper {

    private final PrenotazioneRepository prenotazioneRepository;
    private final HotelRepository hotelRepository;
    private final ClienteRepository clienteRepository;
    private final StanzaRepository stanzaRepository;
    private final CategoriaRepository categoriaRepository;
    private final StripeHelper stripeHelper;

    private static PrenotazioneEnum prenotazioneEnum;
    private static HotelEnum hotelEnum;
    private static StanzaEnum stanzaEnum;

    public PrenotazioneHelper(PrenotazioneRepository prenotazioneRepository, HotelRepository hotelRepository, ClienteRepository clienteRepository, StanzaRepository stanzaRepository, CategoriaRepository categoriaRepository, StripeHelper stripeHelper) {
        this.prenotazioneRepository = prenotazioneRepository;
        this.hotelRepository = hotelRepository;
        this.clienteRepository = clienteRepository;
        this.stanzaRepository = stanzaRepository;
        this.categoriaRepository = categoriaRepository;
        this.stripeHelper = stripeHelper;
    }


    public PrenotazioneDTO findById(Long id) {
        if (prenotazioneRepository.existsById(id)) {
            return new PrenotazioneDTO(prenotazioneRepository.findById(id).get());
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }

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

    //TODO testare
    public Boolean delete(Long idPrenotazione, String paymentMethod) throws StripeException {
        if (prenotazioneRepository.existsById(idPrenotazione)) {

            Prenotazione prenotazione = prenotazioneRepository.getById(idPrenotazione);
            Categoria categoria = prenotazione.getStanza().getCategoria();
            //Cliente cliente = prenotazione.getCliente();

            long diffDataInizioPrenotazione = ChronoUnit.DAYS.between(LocalDate.now(), prenotazione.getDataInizio().toInstant());

            if (diffDataInizioPrenotazione > categoria.getGiorniPenale()) {
                try {
                    prenotazioneRepository.deleteById(idPrenotazione);
                    return true;
                } catch (Exception e) {
                    prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DLE");
                    throw new ApiRequestException(prenotazioneEnum.getMessage());
                }
            } else if (diffDataInizioPrenotazione > categoria.getGiorniBlocco() && diffDataInizioPrenotazione <= categoria.getGiorniPenale()) {
                String customerId = prenotazione.getCliente().getCustomerId();
                String price = Double.toString(categoria.getQtaPenale());

                PaymentData paymentData = PaymentData.builder().customerId(customerId).price(price).paymentMethod(paymentMethod).build();
                stripeHelper.createPaymentIntent(paymentData);
                try {
                    prenotazioneRepository.deleteById(idPrenotazione);
                    return true;
                } catch (Exception e) {
                    prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DLE");
                    throw new ApiRequestException(prenotazioneEnum.getMessage());
                }

            } else if (diffDataInizioPrenotazione <= categoria.getGiorniBlocco()) {
                //TODO chiedere a giovanni cosa vuole che gli torni
                //throw new ApiRequestException("Impossibile cancellare la prenotazione, prenotazione bloccata");
                return false;
            }

        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_NF");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


    //TODO testare
    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO, String paymentMethod) throws StripeException, ParseException {

        String data = new Date().toString();
        Date dataAttuale = new SimpleDateFormat("yyyy-MM-dd").parse(data);

        if (prenotazioneRepository.checkPrenotazioneDate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.idStanza) == 0
                && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine)
                && !(prenotazioneDTO.dataInizio.equals(dataAttuale) || prenotazioneDTO.dataInizio.before(dataAttuale))) {
            Prenotazione prenotazione = new Prenotazione();

            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);
            prenotazione.setHotel(hotelRepository.findById(prenotazioneDTO.idHotel).get());
            prenotazione.setStanza(stanzaRepository.findById(prenotazioneDTO.idStanza).get());
            prenotazione.setCliente(clienteRepository.findById(prenotazioneDTO.idCliente).get());


            String customerId = prenotazione.getCliente().getCustomerId();
            long days = ChronoUnit.DAYS.between(prenotazione.getDataInizio().toInstant(), prenotazione.getDataFine().toInstant());
            String price = Double.toString(prenotazione.getStanza().getCategoria().getPrezzo() * days);
            PaymentData paymentData = PaymentData.builder().customerId(customerId).price(price).paymentMethod(paymentMethod).build();
            stripeHelper.createPaymentIntent(paymentData);

            prenotazioneRepository.save(prenotazione);

            return new PrenotazioneDTO(prenotazione);
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DNC");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(Long idStanza) {
        if (stanzaRepository.existsById(idStanza)) {
            List<Prenotazione> prenotazioniLista = prenotazioneRepository.findPrenotazionesByStanza_Id(idStanza);
            return prenotazioniLista.stream().map(x -> new PrenotazioneDTO(x)).collect(Collectors.toList());
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_IDNE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }


    //TODO gestire il prezzo per la modifica delle date
    public Long update(PrenotazioneDTO prenotazioneDTO) throws ParseException {

        String data = new Date().toString();
        Date dataAttuale = new SimpleDateFormat("yyyy-MM-dd").parse(data);

        if (prenotazioneRepository.checkPrenotazioneDateUpdate(prenotazioneDTO.dataInizio, prenotazioneDTO.dataFine, prenotazioneDTO.id, prenotazioneDTO.idStanza) == 0
                && prenotazioneDTO.dataInizio.before(prenotazioneDTO.dataFine) &&
                prenotazioneRepository.existsById(prenotazioneDTO.id) &&
                !(prenotazioneDTO.dataInizio.equals(dataAttuale) || prenotazioneDTO.dataInizio.before(dataAttuale))) {


            Prenotazione prenotazione = prenotazioneRepository.findById(prenotazioneDTO.id).get();
            prenotazione.setDataInizio(prenotazioneDTO.dataInizio);
            prenotazione.setDataFine(prenotazioneDTO.dataFine);

            prenotazioneRepository.save(prenotazione);
            return prenotazione.getId();

        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DNC");
        throw new ApiRequestException(prenotazioneEnum.getMessage());
    }


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
