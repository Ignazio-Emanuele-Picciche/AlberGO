package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/dettaglio")
    @ResponseBody
    public ClienteDTO findById(@RequestParam(name = "idCliente") Long id){
        return clienteService.findById(id);
    }

    @GetMapping("/lista")
    @ResponseBody
    public List<ClienteDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return clienteService.findAll(idHotel);
    }

    @PostMapping("/create")
    public Long create(@RequestBody ClienteDTO clienteDTO){
        return clienteService.create(clienteDTO);
    }

    @PutMapping("/update")
    @ResponseBody
    public ClienteDTO update(@RequestBody ClienteDTO clienteDTO){
        return clienteService.update(clienteDTO);
    }

    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idCliente") Long id){
        return clienteService.delete(id);
    }


    //find all Cienti startWith nome or cognome
    @GetMapping("/searchNomeCognome")
    public List<ClienteDTO> findAllByNomeCognome(@RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("idHotel") Long idHotel){
        return clienteService.findAllByNomeCognome(nome, cognome, idHotel);
    }


}
