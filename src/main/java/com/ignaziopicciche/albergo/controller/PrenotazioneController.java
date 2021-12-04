package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.service.PrenotazioneService;
import com.stripe.exception.StripeException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/prenotazione")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }


    @GetMapping("/dettaglio")
    public PrenotazioneDTO findById(@RequestParam(name = "idPrenotazione") Long id) {
        return prenotazioneService.findById(id);
    }

    @GetMapping("/lista")
    public List<FatturaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return prenotazioneService.findAll(idHotel);
    }

    @GetMapping("/stanzaId")
    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(@RequestParam(name = "idStanza") Long idStanza) {
        return prenotazioneService.findPrenotazionesByStanza_Id(idStanza);
    }


    @PostMapping("/create")
    public PrenotazioneDTO create(@RequestBody PrenotazioneDTO prenotazioneDTO) throws StripeException, ParseException {
        return prenotazioneService.create(prenotazioneDTO);
    }


    @PutMapping("/update")
    public Long update(@RequestBody PrenotazioneDTO prenotazioneDTO) throws ParseException, StripeException {
        return prenotazioneService.update(prenotazioneDTO);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idPrenotazione") Long id) throws StripeException {
        return prenotazioneService.delete(id);
    }


    @GetMapping("/listaFatture")
    public List<FatturaDTO> findAllFatture(@RequestParam(name = "idCliente") Long idCliente) {
        return prenotazioneService.findAllFatture(idCliente);
    }


    //find startingWith nomeCliente or cognomeCliente or dataInizio or dataFine
    @GetMapping("/searchNomeCognomeDate")
    public List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(@RequestParam("nomeCliente") String nomeCliente, @RequestParam("cognomeCliente") String cognomeCliente, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine, @RequestParam("idHotel") Long idHotel) throws ParseException {
        return prenotazioneService.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dInizio, dFine, idHotel);
    }


}
