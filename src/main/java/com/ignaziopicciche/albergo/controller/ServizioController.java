package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.service.ServizioService;
import com.stripe.exception.StripeException;
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

    @GetMapping("/listaNotInPrenotazione")
    public List<ServizioDTO> findNotInByPrenotazione(@RequestParam("idPrenotazione") Long idPrenotazione){
        return servizioService.findNotInByPrenotazione(idPrenotazione);
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


    @PutMapping("/insertServizioPrenotazione")
    public Long insertByPrentazioneAndHotel(@RequestParam("idServizio") Long idServzio, @RequestParam("idPrenotazione") Long idPrenotazione, @RequestParam("idHotel") Long idHotel, @RequestParam("paymentMethod") String paymentMethod) throws StripeException {
        return servizioService.insertByPrentazioneAndHotel(idServzio, idPrenotazione, idHotel, paymentMethod);
    }




}
