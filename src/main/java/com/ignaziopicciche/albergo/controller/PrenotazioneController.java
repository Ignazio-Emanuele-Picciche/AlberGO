package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.service.PrenotazioneService;
import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/*
    -Nella classe PrenotazioneController vengono gestiti ed organizzati tutti gli endpoint relativi alla prenotazione.
    -I path delle api, ovvero delle attività che si possono svolgere relative alla prenotazione, iniziano con:
    "http://localhost:8080/api/prenotazione/...".
    -Nei metodi presenti in questa classe vengono semplicemente richiamati i metodi dela classe PrenotazioneService
    per il controllo e la validità dei dati in input delle request dal front-end.
    -Infine tutte le response ricevute dal livello "service" verranno inviare al front-end.
 */

@RestController
@RequestMapping("/api/prenotazione")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    /**
     * In questo metodo viene implmenetata la logica dell'annotazione @Autowired per l'attributo prenotazioneService,
     * ovvero stiamo chiedendo a Spring di invocare il metodo setter in questione subito
     * dopo aver istanziato il bean della classe PrenotazioneService.
     * @param prenotazioneService
     */
    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }

    /**
     * Endpoint che restituisce una prenotazione cercata per idPrenotazione
     * @param id
     * @return PrenotazioneDTO
     */
    @GetMapping("/dettaglio")
    public PrenotazioneDTO findById(@RequestParam(name = "idPrenotazione") Long id) {
        return prenotazioneService.findById(id);
    }

    /**
     * Enpoint che restituisce tutte le fatture fatte in un hotel
     * @param idHotel
     * @return List<FatturaDTO>
     */
    @GetMapping("/lista")
    public List<FatturaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return prenotazioneService.findAll(idHotel);
    }

    /**
     * Endpoint che restitusce tutte le prenotazioni in cui è stata scelta una specifica stanza di un hotel
     * @param idStanza
     * @return List<PrenotazioneDTO>
     */
    @GetMapping("/stanzaId")
    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(@RequestParam(name = "idStanza") Long idStanza) {
        return prenotazioneService.findPrenotazionesByStanza_Id(idStanza);
    }

    /**
     * Enpoint per la creazione di una nuova prenotazione e relativo addebito
     * @param prenotazioneDTO
     * @return PrenotazioneDTO
     * @throws StripeException
     * @throws ParseException
     */
    @PostMapping("/create")
    public PrenotazioneDTO create(@RequestBody PrenotazioneDTO prenotazioneDTO) throws StripeException, ParseException {
        return prenotazioneService.create(prenotazioneDTO);
    }

    /**
     * Enpoint per l'aggiornamento dei dati di una prenotazione presente nel sistema(es. cambio date)
     * @param prenotazioneDTO
     * @return idPrenotazione
     * @throws ParseException
     * @throws StripeException
     */
    @PutMapping("/update")
    public Long update(@RequestBody PrenotazioneDTO prenotazioneDTO) throws ParseException, StripeException {
        return prenotazioneService.update(prenotazioneDTO);
    }

    /**
     * Endpoint per l'eliminazione di una prenotazione
     * @param id
     * @return Boolean
     * @throws StripeException
     */
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idPrenotazione") Long id) throws StripeException {
        return prenotazioneService.delete(id);
    }

    /**
     * Endpoint che restituisce tutte le fatture fatte da un cliente
     * @param idCliente
     * @return List<FatturaDTO>
     */
    @GetMapping("/listaFatture")
    public List<FatturaDTO> findAllFatture(@RequestParam(name = "idCliente") Long idCliente) {
        return prenotazioneService.findAllFatture(idCliente);
    }

    /**
     * Endpoint che restituisce tutte le fatture di un hotel dove nome e cognome del cliente, associato alla fattura,
     * iniziano per nomeCliente e cognomeCliente.
     * Inoltre la ricerca viene fatta in un intervallo di date (dInizio e dFine).
     * @param nomeCliente
     * @param cognomeCliente
     * @param dInizio
     * @param dFine
     * @param idHotel
     * @returnList<FatturaDTO>
     * @throws ParseException
     */
    @GetMapping("/searchNomeCognomeDate")
    public List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(@RequestParam("nomeCliente") String nomeCliente, @RequestParam("cognomeCliente") String cognomeCliente, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine, @RequestParam("idHotel") Long idHotel) throws ParseException {
        return prenotazioneService.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dInizio, dFine, idHotel);
    }


}
