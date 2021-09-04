package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.service.StanzaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stanza")
public class StanzaController {

    @Autowired
    private StanzaService stanzaService;

    //Durante la creazione serve per filtrare le stanze in base alla categoria e DataInizio e Fine
    //FindStanzasByCategoria_IdAndHotel_IdAndDataInizioAndDataFine

    //Per Grafico Stanze
    //FindStanzasOccupateByDataInizioAndDataFineAAndHotel_Id
    //FindStanzasFuoriServizioByDataInizioAndDataFineAAndHotel_Id
    //FindStanzasLibereByDataInizioAndDataFineAAndHotel_Id

    //Per Tabella Stanze
    //FindStanzasByDataInizioAndDataFineAndHotel_Id


    @GetMapping("/dettaglio")
    @ResponseBody
    public StanzaDTO findById(@RequestParam(name = "idStanza") Long id){
        return stanzaService.findById(id);
    }

    @GetMapping("/lista")
    @ResponseBody
    public List<StanzaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return stanzaService.findAll(idHotel);
    }

    @PostMapping("/create")
    @ResponseBody
    public StanzaDTO create(@RequestBody StanzaDTO stanzaDTO){
        return stanzaService.create(stanzaDTO);
    }

    @PutMapping("/update")
    @ResponseBody
    public StanzaDTO update(@RequestBody StanzaDTO stanzaDTO){
        return stanzaService.update(stanzaDTO);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idStanza") Long id){
        return stanzaService.delete(id);
    }

}
