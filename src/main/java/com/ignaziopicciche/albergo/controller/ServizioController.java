package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.service.ServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servizio")
public class ServizioController {

    private final ServizioService servizioService;

    public ServizioController(ServizioService servizioService) {
        this.servizioService = servizioService;
    }


    @GetMapping("/dettaglio")
    public ServizioDTO findById(@RequestParam(name = "idServizio") Long id) {
        return servizioService.findById(id);
    }

    @GetMapping("/lista")
    public List<ServizioDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return servizioService.findAll(idHotel);
    }


    @PostMapping("/create")
    public Long create(@RequestBody ServizioDTO servizioDTO) {
        return servizioService.create(servizioDTO);
    }


    @PutMapping("/update")
    public Long update(@RequestBody ServizioDTO servizioDTO) {
        return servizioService.update(servizioDTO);
    }


    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam("idServizio") Long id) {
        return servizioService.delete(id);
    }


}
