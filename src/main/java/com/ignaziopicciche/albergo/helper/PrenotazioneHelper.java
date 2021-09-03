package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.exception.PrenotazioneException;
import com.ignaziopicciche.albergo.model.Prenotazione;
import com.ignaziopicciche.albergo.repository.ClienteRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.StanzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PrenotazioneHelper {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private StanzaRepository stanzaRepository;


    public PrenotazioneDTO findById(Long id){
        if(prenotazioneRepository.existsById(id)){
            return new PrenotazioneDTO(prenotazioneRepository.findById(id).get());
        }

        throw new PrenotazioneException(PrenotazioneException.PrenotazioneExceptionCode.PRENOTAZIONE_ID_NOT_EXIST);
    }

    public List<PrenotazioneDTO> findAll(Long idHotel){
        if(hotelRepository.existsById(idHotel)){
            return prenotazioneRepository.findPrenotazioneByHotel_Id(idHotel).stream().map(x -> new PrenotazioneDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }


    public PrenotazioneDTO create(PrenotazioneDTO prenotazioneDTO){
        if(!prenotazioneRepository.existsById(prenotazioneDTO.id) &&  )
    }

}
