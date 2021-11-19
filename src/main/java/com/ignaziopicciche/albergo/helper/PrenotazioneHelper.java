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
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByCliente_EmbeddedId_Id(idCliente);
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

                PaymentData paymentData = PaymentData.builder()
                        .customerId(cliente.getEmbeddedId().getCustomerId())
                        .price(price)
                        .paymentMethod(cliente.getEmbeddedId().getPaymentMethodId())
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


    //TODO testare
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
            String price = Double.toString(prenotazione.getStanza().getCategoria().getPrezzo() * days);
            if (price.indexOf(".") == price.length() - 2) {
                StringBuilder priceS = new StringBuilder(price);
                priceS.append("0");
                price = priceS.toString();
            }
            price = price.replace(".", "");

            LocalDate dataInizioNuova = prenotazioneDTO.dataInizio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dataFineNuova = prenotazioneDTO.dataFine.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            PaymentData paymentData = PaymentData.builder()
                    .customerId(cliente.getEmbeddedId().getCustomerId())
                    .price(price)
                    .paymentMethod(cliente.getEmbeddedId().getPaymentMethodId())
                    .description("Prenotazione creata "+cliente.getNome()+" "+cliente.getCognome()+" "+dataInizioNuova+"  "+dataFineNuova).build();
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

                PaymentData paymentData = PaymentData.builder()
                        .customerId(cliente.getEmbeddedId().getCustomerId())
                        .price(price)
                        .paymentMethod(cliente.getEmbeddedId().getPaymentMethodId())
                        .description("Prenotazione aggiornata "+cliente.getNome()+" "+cliente.getCognome()+" "+dataInizioNuova+"  "+dataFineNuova).build();
                stripeHelper.createPaymentIntent(paymentData);
            }


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
