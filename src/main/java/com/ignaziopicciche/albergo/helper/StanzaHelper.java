package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.enums.CategoriaEnum;
import com.ignaziopicciche.albergo.enums.HotelEnum;
import com.ignaziopicciche.albergo.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.enums.StanzaEnum;
import com.ignaziopicciche.albergo.handler.ApiRequestException;
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

    private static StanzaEnum stanzaEnum;
    private static CategoriaEnum categoriaEnum;
    private static PrenotazioneEnum prenotazioneEnum;
    private static HotelEnum hotelEnum;


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

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_AE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }

    public Boolean delete(Long id) {
        if (stanzaRepository.existsById(id)) {
            try {
                stanzaRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_DLE");
                throw new ApiRequestException(stanzaEnum.getMessage());
            }
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_IDNE");
        throw new ApiRequestException(stanzaEnum.getMessage());
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

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_IDNE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }


    public StanzaDTO findById(Long id) {
        if (stanzaRepository.existsById(id)) {
            return new StanzaDTO(stanzaRepository.findById(id).get());
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_NF");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }


    public List<StanzaDTO> findAll(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return stanzaRepository.findStanzasByHotel_Id(idHotel).stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }


    public List<StanzaDTO> findStanzasByCategoria_IdAndDates(Long idCategoria, Date dataInizio, Date dataFine) {

        if (categoriaRepository.existsById(idCategoria)) {
            if(dataInizio.before(dataFine)) {
                List<Stanza> stanzeCategoria = stanzaRepository.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine);
                return stanzeCategoria.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_IDNE");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }


    public int findCountStanzasFuoriServizioByHotel_Id(Long idHotel){
        if(hotelRepository.existsById(idHotel)){
            return stanzaRepository.findCountStanzasFuoriServizioByHotel_Id(idHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    public List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        if(hotelRepository.existsById(idHotel)){
            if(dataInizio.before(dataFine)) {
                List<Stanza> stanzeLibere = stanzaRepository.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
                return stanzeLibere.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    public List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine){
        if(hotelRepository.existsById(idHotel)){
            if(dataInizio.before(dataFine)) {
                List<Stanza> stanzeOccupate = stanzaRepository.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
                return stanzeOccupate.stream().map(x -> new StanzaDTO(x)).collect(Collectors.toList());
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }


    public int findCountStanzeByCategoria_Id(Long idCategoria){
        if(categoriaRepository.existsById(idCategoria)){
            return stanzaRepository.findCountStanzeByCategoria_Id(idCategoria);
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_IDNE");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }

}
