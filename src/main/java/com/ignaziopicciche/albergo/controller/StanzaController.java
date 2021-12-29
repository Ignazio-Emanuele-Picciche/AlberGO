package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.service.StanzaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * -Nella classe StanzaController vengono gestiti e organizzati tutti gli endpoint relativi alla stanza.
 * -I path delle api, ovvero delle attività che si possono svolgere relative alla stanza, iniziano con:
 * "http://localhost:8080/api/stanza/...".
 * -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe StanzaService
 * per il controllo e la validità dei dati in input delle request dal front-end.
 * -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/stanza")
public class StanzaController {

    private final StanzaService stanzaService;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo stanzaService,
     * ovvero stiamo chiedendo a Spring d'invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe StanzaService.
     *
     * @param stanzaService
     */
    public StanzaController(StanzaService stanzaService) {
        this.stanzaService = stanzaService;
    }

    /**
     * Endpoint che restituisce una stanza cercata per idStanza
     *
     * @param id
     * @return StanzaDTO
     */
    @GetMapping("/dettaglio")
    public StanzaDTO findById(@RequestParam(name = "idStanza") Long id) {
        return stanzaService.findById(id);
    }

    /**
     * Endpoint che restituisce tutte le stanze presenti in un hotel
     *
     * @param idHotel
     * @return List<StanzaDTO>
     */
    @GetMapping("/lista")
    public List<StanzaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return stanzaService.findAll(idHotel);
    }

    /**
     * Endpoint che inserissce una nuova stanza del sistema
     *
     * @param stanzaDTO
     * @return StanzaDTO
     */
    @PostMapping("/create")
    public StanzaDTO create(@RequestBody StanzaDTO stanzaDTO) {
        return stanzaService.create(stanzaDTO);
    }

    /**
     * Endpoint che aggiorna i campi di una stanza presente del sistema
     *
     * @param stanzaDTO
     * @return StanzaDTO
     */
    @PutMapping("/update")
    public StanzaDTO update(@RequestBody StanzaDTO stanzaDTO) {
        return stanzaService.update(stanzaDTO);
    }

    /**
     * Endpoint che elimina una stanza
     *
     * @param id
     * @return Boolean
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idStanza") Long id) {
        return stanzaService.delete(id);
    }

    /**
     * Questo endpoint viene usato per la parte "statistiche" nel front-end.
     * Restituisce tutte le stanze che fanno parte di una categoria vengono prenotate in un intervallo di date
     *
     * @param idCategoria
     * @param dInizio
     * @param dFine
     * @return List<StanzaDTO>
     * @throws ParseException
     */
    @GetMapping("/stanzeCategoria")
    public List<StanzaDTO> findStanzasByCategoria_IdAndDates(@RequestParam("idCategoria") Long idCategoria, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine) throws ParseException {
        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        return stanzaService.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine);
    }

    /**
     * Questo endpoint viene usato per la parte "statistiche" nel front-end.
     * Restituisce il numero di stanze fuori servizio di un hotel
     *
     * @param idHotel
     * @return int
     */
    @GetMapping("/fuoriServizio")
    public int findCountStanzasFuoriServizioByHotel_Id(@RequestParam(name = "idHotel") Long idHotel) {
        return stanzaService.findCountStanzasFuoriServizioByHotel_Id(idHotel);
    }

    /**
     * Endpoint che restituisce tutte le stanze libere di un hotel in un intervallo di date
     *
     * @param idHotel
     * @param dInizio
     * @param dFine
     * @return List<StanzaDTO>
     * @throws ParseException
     */
    @GetMapping("/libere")
    public List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(@RequestParam(name = "idHotel") Long idHotel, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine) throws ParseException {
        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        return stanzaService.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    /**
     * Endpoint che restituisce tutte le stanze occupate di un hotel in un intervallo di date
     *
     * @param idHotel
     * @param dInizio
     * @param dFine
     * @return List<StanzaDTO>
     * @throws ParseException
     */
    @GetMapping("/occupate")
    public List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(@RequestParam(name = "idHotel") Long idHotel, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine) throws ParseException {
        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        return stanzaService.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    /**
     * Endpoint che restituisce il numero di stanze che hanno la stessa
     *
     * @param idCategoria
     * @return int
     */
    @GetMapping("/categoriaId")
    public int findCountStanzeByCategoria_Id(@RequestParam(name = "idCategoria") Long idCategoria) {
        return stanzaService.findCountStanzeByCategoria_Id(idCategoria);
    }

}
