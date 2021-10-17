package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.enums.ServizioEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Servizio;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.ServizioRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ServizioHelper {

    private final ServizioRepository servizioRepository;
    private final HotelRepository hotelRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    private static ServizioEnum servizioEnum;
    private static HotelEnum hotelEnum;

    public ServizioHelper(ServizioRepository servizioRepository, HotelRepository hotelRepository, PrenotazioneRepository prenotazioneRepository) {
        this.servizioRepository = servizioRepository;
        this.hotelRepository = hotelRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }


    public ServizioDTO findById(Long id) {
        if (servizioRepository.existsById(id)) {
            Servizio servizio = servizioRepository.findById(id).orElseThrow(RuntimeException::new);
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
                    .hotel(hotelRepository.findById(servizioDTO.idHotel).get())
                    .prenotazione(prenotazioneRepository.findById(servizioDTO.idPrenotazione).orElseThrow(RuntimeException::new)).build();

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

            Servizio servizio = servizioRepository.findById(servizioDTO.id).orElseThrow(RuntimeException::new);
            servizio.setNome(servizioDTO.nome);
            servizio.setPrezzo(servizioDTO.prezzo);

            servizio = servizioRepository.save(servizio);

            return servizio.getId();
        }

        servizioEnum = ServizioEnum.getServizioEnumByMessageCode("SERV_IDNE");
        throw new ApiRequestException(servizioEnum.getMessage());
    }


}
