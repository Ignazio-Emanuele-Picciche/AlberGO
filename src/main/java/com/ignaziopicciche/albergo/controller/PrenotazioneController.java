package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.FatturaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazione")
public class PrenotazioneController {

    private final PrenotazioneService prenotazioneService;

    public PrenotazioneController(PrenotazioneService prenotazioneService) {
        this.prenotazioneService = prenotazioneService;
    }


    @GetMapping("/dettaglio")
    public PrenotazioneDTO findById(@RequestParam(name = "idPrenotazione") Long id){
        return prenotazioneService.findById(id);
    }

    @GetMapping("/lista")
    public List<FatturaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return prenotazioneService.findAll(idHotel);
    }


    //TODO aggiornare nella lista delle api
    //paste -> List<MIXDTO> findAll - /lista
    @GetMapping("/listaFatture")
    public List<FatturaDTO> findAllFatture(@RequestParam(name = "idCliente") Long idClinete){
        return prenotazioneService.findAllFatture(idClinete);
    }


    @GetMapping("/stanzaId")
    public List<PrenotazioneDTO> findPrenotazionesByStanza_Id(@RequestParam(name = "idStanza") Long idStanza){
        return prenotazioneService.findPrenotazionesByStanza_Id(idStanza);
    }

    @PostMapping("/create")
    public PrenotazioneDTO create(@RequestBody PrenotazioneDTO prenotazioneDTO){
        return prenotazioneService.create(prenotazioneDTO);
    }


    @PutMapping("/update")
    public Long update(@RequestBody PrenotazioneDTO prenotazioneDTO){
        return prenotazioneService.update(prenotazioneDTO);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idPrenotazione") Long id){
        return prenotazioneService.delete(id);
    }




}
