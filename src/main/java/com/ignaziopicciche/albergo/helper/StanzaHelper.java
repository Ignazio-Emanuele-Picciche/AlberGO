package com.ignaziopicciche.albergo.helper;

import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.exception.enums.CategoriaEnum;
import com.ignaziopicciche.albergo.exception.enums.HotelEnum;
import com.ignaziopicciche.albergo.exception.enums.PrenotazioneEnum;
import com.ignaziopicciche.albergo.exception.enums.StanzaEnum;
import com.ignaziopicciche.albergo.exception.handler.ApiRequestException;
import com.ignaziopicciche.albergo.model.Stanza;
import com.ignaziopicciche.albergo.repository.CategoriaRepository;
import com.ignaziopicciche.albergo.repository.HotelRepository;
import com.ignaziopicciche.albergo.repository.StanzaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * La classe StanzaHelper contiene i metodi che si occupano dell'implementazione delle logiche
 * e funzionalità vere e proprie degli endpoint richiamati dal front-end. I dati che vengono forniti a questi metodi
 * provengono dal livello "service" nel quale è stato controllato che i campi obbligatori sono stati inseriti correttamente
 * nel front-end.
 * Per "logiche e funzionalita" si intende:
 * -comunicazioni con il livello "repository" che si occuperà delle operazioni CRUD e non solo:
 * -es. controllare che una stanza è già presente nel sistema;
 * -es. aggiungere, eliminare, cercare, aggiornare una stanza.
 * -varie operazioni di logica (calcoli, operazioni, controlli generici)
 * -restituire, al front-end, le eccezioni custom in caso di errore (es. La stanza che vuoi inserire è già presente nel sistema)
 * -in caso di operazioni andate a buon fine, verranno restituiti al livello service i dati che dovranno essere inviati al front-end.
 */

@Component
public class StanzaHelper {

    private final StanzaRepository stanzaRepository;
    private final HotelRepository hotelRepository;
    private final CategoriaRepository categoriaRepository;

    private static StanzaEnum stanzaEnum;
    private static CategoriaEnum categoriaEnum;
    private static PrenotazioneEnum prenotazioneEnum;
    private static HotelEnum hotelEnum;

    public StanzaHelper(StanzaRepository stanzaRepository, HotelRepository hotelRepository, CategoriaRepository categoriaRepository) {
        this.stanzaRepository = stanzaRepository;
        this.hotelRepository = hotelRepository;
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Metodo che crea una nuova stanza di un hotel nel sistema
     * Se le sstanza dovesse gia esistere nel sistema viene restituita un'eccezione custom
     *
     * @param stanzaDTO
     * @return StanzaDTO
     */
    public Stanza createStanza(StanzaDTO stanzaDTO) {

        if (!stanzaRepository.existsStanzaByNumeroStanzaAndHotel_Id(stanzaDTO.numeroStanza, stanzaDTO.idHotel) &&
                stanzaDTO.numeroStanza != null && !stanzaDTO.descrizione.equals("")) {

            Stanza stanza = new Stanza();

            stanza.setNumeroStanza(stanzaDTO.numeroStanza);
            stanza.setFuoriServizio(stanzaDTO.fuoriServizio);
            stanza.setDescrizione(stanzaDTO.descrizione);
            stanza.setMetriQuadri(stanzaDTO.metriQuadri);
            stanza.setHotel(hotelRepository.findById(stanzaDTO.idHotel).get());
            stanza.setCategoria(categoriaRepository.findById(stanzaDTO.idCategoria).get());

            stanza = stanzaRepository.save(stanza);
            return stanza;
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_AE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }

    /**
     * Metodo che elimina una stanza dell'hotel dal sistema
     * Nel caso in cui la stanza che si vuole rimuovere non dovesse esistere viene restituita un'eccezione custom
     *
     * @param id
     * @return Boolean
     */
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

    /**
     * Metodo che aggiorna i campi "editabili" della stanza
     * Nel caso in cui la stanza che si vuole aggiornare non è presente nel sistema viene restituita un'eccezione custom
     *
     * @param stanzaDTO
     * @return Stanza
     */
    public Stanza updateStanza(StanzaDTO stanzaDTO) {

        if (stanzaRepository.existsById(stanzaDTO.id)) {

            Stanza stanza = stanzaRepository.findById(stanzaDTO.id).get();

            //stanza.setNumeroStanza(stanzaDTO.numeroStanza);
            stanza.setDescrizione(stanzaDTO.descrizione);
            stanza.setFuoriServizio(stanzaDTO.fuoriServizio);
            //stanza.setMetriQuadri(stanzaDTO.metriQuadri);
            //stanza.setHotel(hotelRepository.findById(stanzaDTO.idHotel).get());
            //stanza.setCategoria(categoriaRepository.findById(stanzaDTO.idCategoria).get());

            stanza = stanzaRepository.save(stanza);
            return stanza;
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_IDNE");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato che esiste la stanza nel sistema, restituisce il dettaglio della stanza associata all'id passato come parametro
     * In caso di errore restituisce un'eccezione custom
     *
     * @param id
     * @return Stanza
     */
    public Stanza findById(Long id) {
        if (stanzaRepository.existsById(id)) {
            return stanzaRepository.findById(id).get();
        }

        stanzaEnum = StanzaEnum.getStanzaEnumByMessageCode("STA_NF");
        throw new ApiRequestException(stanzaEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllato che l'hotel è presente nel sistema, restituisce tutte le stanze presenti in quell'hotel
     * In caso di errore restituisce un'eccezione custom
     *
     * @param idHotel
     * @return List<Stanza>
     */
    public List<Stanza> findAllStanzeByIdHotel(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return stanzaRepository.findStanzasByHotel_Id(idHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che restituisce tutte le stanze associate a una categoria che sono state prenotate in un intervallo di date specifico
     *
     * @param idCategoria
     * @param dataInizio
     * @param dataFine
     * @return List<Stanza>
     */
    public List<Stanza> findStanzasByCategoria_IdAndDates(Long idCategoria, Date dataInizio, Date dataFine) {

        if (categoriaRepository.existsById(idCategoria)) {
            if (dataInizio.before(dataFine)) {
                return stanzaRepository.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine);
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_IDNE");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }

    /**
     * Metodo che restituisce il numero delle stanze fuori servizio di un hotel specifico
     * Nel caso in cui l'hotel a cui si fa riferimento non dovesse esistere nel sistema viene restituita un'eccezione custom
     *
     * @param idHotel
     * @return int
     */
    public int findNumeroStanzeFuoriServizioByIdHotel(Long idHotel) {
        if (hotelRepository.existsById(idHotel)) {
            return stanzaRepository.findCountStanzasFuoriServizioByHotel_Id(idHotel);
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver controllate che l'hotel è presente nel sistema, restituisce tutte le stanze libere in un'intervallo
     * di date specifico
     *
     * @param idHotel
     * @param dataInizio
     * @param dataFine
     * @return List<Stanza>
     */
    public List<Stanza> findStanzeLibereByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine) {
        if (hotelRepository.existsById(idHotel)) {
            if (dataInizio.before(dataFine)) {
                return stanzaRepository.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che restituisce le stanze occupate di un hotel in un intervallo di date specifico
     *
     * @param idHotel
     * @param dataInizio
     * @param dataFine
     * @return List<Stanza>
     */
    public List<Stanza> findStanzeOccupateByHotel_IdAndDates(Long idHotel, Date dataInizio, Date dataFine) {
        if (hotelRepository.existsById(idHotel)) {
            if (dataInizio.before(dataFine)) {
                return stanzaRepository.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
            }

            prenotazioneEnum = PrenotazioneEnum.getPrenotazioneEnumByMessageCode("PREN_DE");
            throw new ApiRequestException(prenotazioneEnum.getMessage());
        }

        hotelEnum = HotelEnum.getHotelEnumByMessageCode("HOT_IDNE");
        throw new ApiRequestException(hotelEnum.getMessage());
    }

    /**
     * Metodo che, dopo aver verificato che la categoria è presente nel sistema, ritorna il numero di stanze associate alla
     * categoria passata come parametro
     *
     * @param idCategoria
     * @return int
     */
    public int findCountStanzeByCategoria_Id(Long idCategoria) {
        if (categoriaRepository.existsById(idCategoria)) {
            return stanzaRepository.findCountStanzeByCategoria_Id(idCategoria);
        }

        categoriaEnum = CategoriaEnum.getCategoriaEnumByMessageCode("CAT_IDNE");
        throw new ApiRequestException(categoriaEnum.getMessage());
    }

}
