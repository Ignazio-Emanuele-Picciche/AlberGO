package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.exception.PrenotazioneException;
import com.ignaziopicciche.albergo.exception.ServizioException;
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

        throw new ServizioException(ServizioException.ServizioExcpetionCode.SERVIZIO_ID_NOT_EXIST);
    }

    public List<ServizioDTO> findAll(Long idHotel){
        if(hotelRepository.existsById(idHotel)){

            List<Servizio> servizi = servizioRepository.findAllByHotel_Id(idHotel);
            return servizi.stream().map(servizio -> new ServizioDTO(servizio)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
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

        throw new ServizioException(ServizioException.ServizioExcpetionCode.SERVIZIO_ALREADY_EXISTS);

    }



    public Boolean delete(Long id) {
        if (servizioRepository.existsById(id)) {
            try {
                servizioRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new ServizioException(ServizioException.ServizioExcpetionCode.SERVIZIO_DELETE_ERROR);
            }
        }

        throw new ServizioException(ServizioException.ServizioExcpetionCode.SERVIZIO_NOT_FOUND);
    }


    public Long update(ServizioDTO servizioDTO) {
        if (servizioRepository.existsById(servizioDTO.id)) {

            Servizio servizio = servizioRepository.findById(servizioDTO.id).orElseThrow(RuntimeException::new);
            servizio.setNome(servizioDTO.nome);
            servizio.setPrezzo(servizioDTO.prezzo);

            servizio = servizioRepository.save(servizio);

            return servizio.getId();
        }

        throw new ServizioException(ServizioException.ServizioExcpetionCode.SERVIZIO_ID_NOT_EXIST);
    }


}
