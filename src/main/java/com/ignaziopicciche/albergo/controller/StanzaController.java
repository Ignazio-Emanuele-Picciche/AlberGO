package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.StanzaDTO;
import com.ignaziopicciche.albergo.service.StanzaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/stanza")
public class StanzaController {

    private final StanzaService stanzaService;

    public StanzaController(StanzaService stanzaService) {
        this.stanzaService = stanzaService;
    }


    @GetMapping("/dettaglio")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public StanzaDTO findById(@RequestParam(name = "idStanza") Long id) {
        return stanzaService.findById(id);
    }

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public List<StanzaDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return stanzaService.findAll(idHotel);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StanzaDTO create(@RequestBody StanzaDTO stanzaDTO) {
        return stanzaService.create(stanzaDTO);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public StanzaDTO update(@RequestBody StanzaDTO stanzaDTO) {
        return stanzaService.update(stanzaDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Boolean delete(@RequestParam(name = "idStanza") Long id) {
        return stanzaService.delete(id);
    }



    @GetMapping("/stanzeCategoria")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StanzaDTO> findStanzasByCategoria_IdAndDates(@RequestParam("idCategoria") Long idCategoria, @RequestParam("idHotel") Long idHotel, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine) throws ParseException {
        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        return stanzaService.findStanzasByCategoria_IdAndDates(idCategoria, dataInizio, dataFine);
    }

    @GetMapping("/fuoriServizio")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public int findCountStanzasFuoriServizioByHotel_Id(@RequestParam(name = "idHotel") Long idHotel) {
        return stanzaService.findCountStanzasFuoriServizioByHotel_Id(idHotel);
    }

    @GetMapping("/libere")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StanzaDTO> findStanzasLibereByHotel_IdAndDates(@RequestParam(name = "idHotel") Long idHotel, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine) throws ParseException {
        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        return stanzaService.findStanzasLibereByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    @GetMapping("/occupate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StanzaDTO> findStanzasOccupateByHotel_IdAndDates(@RequestParam(name = "idHotel") Long idHotel, @RequestParam("dataInizio") String dInizio, @RequestParam("dataFine") String dFine) throws ParseException {
        Date dataInizio = StringUtils.isNotBlank(dInizio) ? new SimpleDateFormat("yyyy-MM-dd").parse(dInizio) : null;
        Date dataFine = StringUtils.isNotBlank(dFine) ? new SimpleDateFormat("yyyy-MM-dd").parse(dFine) : null;
        return stanzaService.findStanzasOccupateByHotel_IdAndDates(idHotel, dataInizio, dataFine);
    }

    @GetMapping("/categoriaId")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public int findCountStanzeByCategoria_Id(@RequestParam(name = "idCategoria") Long idCategoria) {
        return stanzaService.findCountStanzeByCategoria_Id(idCategoria);
    }

}
