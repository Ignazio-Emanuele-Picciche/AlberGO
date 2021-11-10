package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.enums.ServizioEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Cliente;
import com.ignaziopicciche.albergo.model.PaymentData;
import com.ignaziopicciche.albergo.model.Prenotazione;
import com.ignaziopicciche.albergo.model.Servizio;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.ServizioRepository;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServizioHelper {

    private final ServizioRepository servizioRepository;
    private final HotelRepository hotelRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final StripeHelper stripeHelper;

    private static ServizioEnum servizioEnum;
    private static HotelEnum hotelEnum;
    private static PrenotazioneEnum prenotazioneEnum;

    public ServizioHelper(ServizioRepository servizioRepository, HotelRepository hotelRepository, PrenotazioneRepository prenotazioneRepository, StripeHelper stripeHelper) {
        this.servizioRepository = servizioRepository;
        this.hotelRepository = hotelRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.stripeHelper = stripeHelper;
    }


    public ServizioDTO findById(Long id) {
        if (servizioRepository.existsById(id)) {
            Servizio servizio = servizioRepository.findById(id).get();
            return new ServizioDTO(servizio);
        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage());
    }

    public List<ServizioDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {

            List<Servizio> servizi = servizioRepository.findAllByHotel_Id(idHotel);
            return servizi.stream().map(servizio -> new ServizioDTO(servizio)).collect(Collectors.toList());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }


    public Long create(ServizioDTO servizioDTO) {

        if (!servizioRepository.existsByNome(servizioDTO.nome)) {
            Servizio servizio = Servizio.builder()
                    .nome(servizioDTO.nome)
                    .prezzo(servizioDTO.prezzo)
                    .hotel(hotelRepository.findById(servizioDTO.idHotel).get()).build();

            servizio = servizioRepository.save(servizio);
            return servizio.getId();

        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_AE");
        throw new ApiRequestException(servizioEnum.getMessage());

    }


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


    //TODO testare l'addebito
    public Long insertByPrentazioneAndHotel(Long idServizio, Long idPrenotazione, Long idHotel) throws StripeException {
        if (servizioRepository.existsServizioByIdAndHotel_Id(idServizio, idHotel)
                && prenotazioneRepository.existsPrenotazioneByIdAndHotel_Id(idPrenotazione, idHotel)) {

            Prenotazione prenotazione = prenotazioneRepository.findById(idPrenotazione).get();
            Servizio servizio = servizioRepository.findById(idServizio).get();
            Cliente cliente = prenotazione.getCliente();

            if(!prenotazione.getServizi().contains(servizio)){
                servizio.addPrenotazione(prenotazione);

                String price = Double.toString(servizio.getPrezzo());
                if (price.indexOf(".") == price.length() - 2) {
                    StringBuilder priceS = new StringBuilder(price);
                    priceS.append("0");
                    price = priceS.toString();
                }
                price = price.replace(".", "");

                PaymentData paymentData = PaymentData.builder()
                        .customerId(cliente.getCustomerId())
                        .price(price)
                        .paymentMethod(cliente.getPaymentMethodId())
                        .description("Servizio "+servizio.getNome()).build();
                stripeHelper.createPaymentIntent(paymentData);

                servizio = servizioRepository.save(servizio);
                return servizio.getId();
            }

            throw new ApiRequestException("Servizio già presente in questa prenotazione");

        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage() + ", oppure la prenotazione o l'hotel che stai cercando non esistono");

    }

    public List<ServizioDTO> findNotInByPrenotazione(Long idPrenotazione) {
        if (prenotazioneRepository.existsById(idPrenotazione)) {
            List<Servizio> serviziPrenotazione = prenotazioneRepository.findById(idPrenotazione).get().getServizi();
            List<Servizio> serviziTotali = servizioRepository.findAllByHotel_Id(prenotazioneRepository.findById(idPrenotazione).get().getHotel().getId());

            List<Servizio> serviziNotInPrenotazione = new ArrayList<>();

            boolean check;
            for (Servizio st : serviziTotali) {
                check = false;
                for (Servizio sp : serviziPrenotazione) {
                    if (st.getId() == sp.getId()) {
                        check = true;
                    }
                }

                if (!check) {
                    serviziNotInPrenotazione.add(st);
                }
            }


            return serviziNotInPrenotazione.stream().map(servizio -> new ServizioDTO(servizio)).collect(Collectors.toList());
        }

        prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_IDNE");
        throw new ApiRequestException(prenotazioneEnum.getMessage());

    }


}
