package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ServizioDTO;
import com.ignaziopicciche.albergo.service.ServizioService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ServizioDTO findById(@RequestParam(name = "idServizio") Long id) {
        return servizioService.findById(id);
    }

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public List<ServizioDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return servizioService.findAll(idHotel);
    }

    @GetMapping("/listaNotInPrenotazione")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ServizioDTO> findNotInByPrenotazione(@RequestParam("idPrenotazione") Long idPrenotazione){
        return servizioService.findNotInByPrenotazione(idPrenotazione);
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long create(@RequestBody ServizioDTO servizioDTO) {
        return servizioService.create(servizioDTO);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Long update(@RequestBody ServizioDTO servizioDTO) {
        return servizioService.update(servizioDTO);
    }


    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean delete(@RequestParam("idServizio") Long id) {
        return servizioService.delete(id);
    }


    @DeleteMapping("/removeServizioPrenotazione")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Boolean removeServizioInPrenotazione(@RequestParam("idServizio") Long idServizio, @RequestParam("idPrenotazione") Long idPrenotazione) {
        return servizioService.removeServizioInPrenotazione(idServizio, idPrenotazione);
    }

    @PostMapping("/insertServizioPrenotazione")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Long insertByPrentazioneAndHotel(@RequestParam("idServizio") Long idServzio, @RequestParam("idPrenotazione") Long idPrenotazione, @RequestParam("idHotel") Long idHotel) throws StripeException {
        return servizioService.insertByPrentazioneAndHotel(idServzio, idPrenotazione, idHotel);
    }

    @GetMapping("/listaServiziPrenotazione")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ServizioDTO> findServiziInPrenotazione(@RequestParam("idPrenotazione") Long idPrenotazione) {
        return servizioService.findServiziInPrenotazione(idPrenotazione);
    }






}
