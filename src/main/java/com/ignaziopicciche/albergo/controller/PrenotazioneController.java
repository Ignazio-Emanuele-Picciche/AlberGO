package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.service.PrenotazioneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public PrenotazioneDTO create(@RequestBody PrenotazioneDTO prenotazioneDTO) {
        return prenotazioneService.create(prenotazioneDTO);
    }


    @PutMapping("/update")
    public Long update(@RequestBody PrenotazioneDTO prenotazioneDTO) {
        return prenotazioneService.update(prenotazioneDTO);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idPrenotazione") Long id) {
        return prenotazioneService.delete(id);
    }


    @GetMapping("/listaFatture")
    public List<FatturaDTO> findAllFatture(@RequestParam(name = "idCliente") Long idClinete) {
        return prenotazioneService.findAllFatture(idClinete);
    }


    //find startingWith nomeCliente or cognomeCliente or dataInizio or dataFine
    @GetMapping("/searchNomeCognomeDate")
    public List<FatturaDTO> findAllByNomeCognomeClienteAndDataInizioAndDataFine(@RequestParam("nomeCliente") String nomeCliente, @RequestParam("cognomeCliente") String cognomeCliente, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine, @RequestParam("idHotel") Long idHotel) throws ParseException {
        return prenotazioneService.findAllByNomeCognomeClienteAndDataInizioAndDataFine(nomeCliente, cognomeCliente, dInizio, dFine, idHotel);
    }


}
