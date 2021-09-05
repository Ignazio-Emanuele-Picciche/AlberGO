package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.CategoriaDTO;
import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.exception.CategoriaException;
import com.ignaziopicciche.albergo.exception.HotelException;
import com.ignaziopicciche.albergo.exception.StanzaException;
import com.ignaziopicciche.albergo.model.Prenotazione;
import com.ignaziopicciche.albergo.model.Stanza;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.PrenotazioneRepository;
import com.ignaziopicciche.albergo.repository.StanzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StanzaHelper {

    @Autowired
    private StanzaRepository stanzaRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;


    public StanzaDTO create(StanzaDTO stanzaDTO) {

        if (!stanzaRepository.existsStanzaByNumeroStanzaAndHotel_Id(stanzaDTO.numeroStanza, stanzaDTO.idHotel) &&
                stanzaDTO.numeroStanza != null && !stanzaDTO.descrizione.equals("")) {

            Stanza stanza = new Stanza();

            stanza.setNumeroStanza(stanzaDTO.numeroStanza);
            stanza.setFuoriServizio(stanzaDTO.fuoriServizio);
            stanza.setDescrizione(stanzaDTO.descrizione);
            stanza.setMetriQuadri(stanzaDTO.metriQuadri);
            stanza.setHotel(hotelRepository.findById(stanzaDTO.idHotel).get());
            stanza.setCategoria(categoriaRepository.findById(stanzaDTO.idCategoria).get());

            stanzaRepository.save(stanza);
            return new StanzaDTO(stanza);
        }

        throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_ALREADY_EXISTS);
    }

    public Boolean delete(Long id) {
        if (stanzaRepository.existsById(id)) {
            try {
                stanzaRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_DELETE_ERROR);
            }
        }

        throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_ID_NOT_EXIST);
    }


    public StanzaDTO update(StanzaDTO stanzaDTO) {

        if (stanzaRepository.existsById(stanzaDTO.id)) {

            Stanza stanza = stanzaRepository.findById(stanzaDTO.id).get();

            //stanza.setNumeroStanza(stanzaDTO.numeroStanza);
            stanza.setDescrizione(stanzaDTO.descrizione);
            stanza.setFuoriServizio(stanzaDTO.fuoriServizio);
            //stanza.setMetriQuadri(stanzaDTO.metriQuadri);
            //stanza.setHotel(hotelRepository.findById(stanzaDTO.idHotel).get());
            //stanza.setCategoria(categoriaRepository.findById(stanzaDTO.idCategoria).get());

            stanzaRepository.save(stanza);
            return new StanzaDTO(stanza);
        }

        throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_ID_NOT_EXIST);
    }


    public StanzaDTO findById(Long id) {
        if (stanzaRepository.existsById(id)) {
            return new StanzaDTO(stanzaRepository.findById(id).get());
        }
        throw new StanzaException(StanzaException.StanzaExceptionCode.STANZA_NOT_FOUND);
    }


    public List<StanzaDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return stanzaRepository.findStanzasByHotel_Id(idHotel).stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }


    public List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine) {

        if (hotelRepository.existsById(idHotel)) {
            List<Prenotazione> prenotazioni = prenotazioneRepository.findPrenotazionesByHotel_IdAndDates(idHotel, dataInizio, dataFine);
            List<Stanza> stanzeOccupate = prenotazioni.stream().map(x -> stanzaRepository.findById(x.getStanza().getId()).get()).collect(Collectors.toList());

            return stanzeOccupate.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());

        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }

    public List<StanzaDTO> findStanzasFuoriServizioByHotel_IDAndDates(Long idHotel, Date dataInizio, Date dataFine) {
        if (hotelRepository.existsById(idHotel)) {
            List<Stanza> stanzeFuoriServizio = stanzaRepository.findStanzasFuoriServizioByHotel_IDAndDates(idHotel, dataInizio, dataFine);
            return stanzeFuoriServizio.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }

    public List<StanzaDTO> findStanzasByCategoria_IdAndHotel_IdAndDates(Long idCategoria, Long idHotel, Date dataInizio, Date dataFine) {

        if (categoriaRepository.existsById(idCategoria) && hotelRepository.existsById(idHotel)) {
            List<Stanza> stanzeCategoria = stanzaRepository.findStanzasByCategoria_IdAndHotel_IdAndDates(idCategoria, idHotel, dataInizio, dataFine);
            return stanzeCategoria.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
        //throw new CategoriaException(CategoriaException.CategoriaExcpetionCode.CATEGORIA_ID_NOT_EXIST);
    }


    public List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine) {
        if (hotelRepository.existsById(idHotel)) {
            List<Stanza> stanzeLibere = stanzaRepository.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
            return stanzeLibere.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);

    }

    public List<StanzaDTO> FindStanzasByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        if(hotelRepository.existsById(idHotel)){
            List<Stanza> stanze = stanzaRepository.FindStanzasByHotel_IdAndDates(idHotel, dataInizio, dataFine);
            return stanze.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
        }

        throw new HotelException(HotelException.HotelExceptionCode.HOTEL_ID_NOT_EXIST);
    }



    

}
