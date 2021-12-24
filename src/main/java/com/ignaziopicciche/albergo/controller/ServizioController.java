package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.service.ServizioService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
    -Nella classe ServizioController vengono gestiti e organizzati tutti gli endpoint relativi al servizio.
    -I path delle api, ovvero delle attività che si possono svolgere relative al servizio, iniziano con:
    "http://localhost:8080/api/servizio/...".
    -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe ServizioService
    per il controllo e la validità dei dati in input delle request dal front-end.
    -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/servizio")
public class ServizioController {

    private final ServizioService servizioService;

    /**
     * In questo metodo viene implementata la logica dell'annotazione @Autowired per l'attributo servizioService,
     * ovvero stiamo chiedendo a Spring di invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe ServizioService.
     * @param servizioService
     */
    public ServizioController(ServizioService servizioService) {
        this.servizioService = servizioService;
    }


    /**
     *Endpoint che restituisce un servizio cercato per idServizio
     * @param id
     * @return ServizioDTO
     */
    @GetMapping("/dettaglio")
    public ServizioDTO findById(@RequestParam(name = "idServizio") Long id) {
        return servizioService.findById(id);
    }

    /**
     * Endpoint che restituisce tutti i servizi disponibili in un hotel
     * @param idHotel
     * @return List<ServizioDTO>
     */
    @GetMapping("/lista")
    public List<ServizioDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return servizioService.findAll(idHotel);
    }

    /**
     * Endpoint che restituisce tutti i servizi di un hotel che non sono presenti in una prenotazione
     * @param idPrenotazione
     * @return List<ServizioDTO>
     */
    @GetMapping("/listaNotInPrenotazione")
    public List<ServizioDTO> findNotInByPrenotazione(@RequestParam("idPrenotazione") Long idPrenotazione) {
        return servizioService.findNotInByPrenotazione(idPrenotazione);
    }

    /**
     * Endpoint che inserisce un nuovo servizio nel sistema
     * @param servizioDTO
     * @return idServizio
     */
    @PostMapping("/create")
    public Long create(@RequestBody ServizioDTO servizioDTO) {
        return servizioService.create(servizioDTO);
    }

    /**
     * Endpoint che aggiorna i campi di un servizio presente nel sistema
     * @param servizioDTO
     * @return idServizio
     */
    @PutMapping("/update")
    public Long update(@RequestBody ServizioDTO servizioDTO) {
        return servizioService.update(servizioDTO);
    }

    /**
     * Endpoint che elimina un servizio dal sistema
     * @param id
     * @return Boolean
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("idServizio") Long id) {
        return servizioService.delete(id);
    }

    /**
     * Endpoint che rimuove un servizio da una prenotazione
     * @param idServizio
     * @param idPrenotazione
     * @return Boolean
     */
    @DeleteMapping("/removeServizioPrenotazione")
    public Boolean removeServizioInPrenotazione(@RequestParam("idServizio") Long idServizio, @RequestParam("idPrenotazione") Long idPrenotazione) {
        return servizioService.removeServizioInPrenotazione(idServizio, idPrenotazione);
    }

    /**
     * Endpoint che inserisce il servizio, scelto dal cliente, nella prenotazione
     * @param idServzio
     * @param idPrenotazione
     * @param idHotel
     * @return idServizio
     * @throws StripeException
     */
    @PostMapping("/insertServizioPrenotazione")
    public Long insertByPrentazioneAndHotel(@RequestParam("idServizio") Long idServzio, @RequestParam("idPrenotazione") Long idPrenotazione, @RequestParam("idHotel") Long idHotel) throws StripeException {
        return servizioService.insertByPrentazioneAndHotel(idServzio, idPrenotazione, idHotel);
    }

    /**
     * Endpoint che restituisce tutti i servizi aggiunti in una prenotazione
     * @param idPrenotazione
     * @return List<ServizioDTO>
     */
    @GetMapping("/listaServiziPrenotazione")
    public List<ServizioDTO> findServiziInPrenotazione(@RequestParam("idPrenotazione") Long idPrenotazione) {
        return servizioService.findServiziInPrenotazione(idPrenotazione);
    }


}
