package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.PrenotazioneClienteStanzaDTO;
import com.ignaziopicciche.albergo.dto.PrenotazioneDTO;
import com.ignaziopicciche.albergo.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazione")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;


    @GetMapping("/dettaglio")
    @ResponseBody
    public PrenotazioneDTO findById(@RequestParam(name = "idPrenotazione") Long id){
        return prenotazioneService.findById(id);
    }

    @GetMapping("/lista")
    @ResponseBody
    public List<PrenotazioneClienteStanzaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return prenotazioneService.findAll(idHotel);
    }

    @PostMapping("/create")
    @ResponseBody
    public PrenotazioneDTO create(@RequestBody PrenotazioneDTO prenotazioneDTO){
        return prenotazioneService.create(prenotazioneDTO);
    }


    /*@PutMapping("/update")
    @ResponseBody
    public PrenotazioneDTO update(@RequestBody PrenotazioneDTO prenotazioneDTO){
        return prenotazioneService.update(prenotazioneDTO);
    }*/

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idPrenotazione") Long id){
        return prenotazioneService.delete(id);
    }


}
