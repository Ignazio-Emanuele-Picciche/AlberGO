package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/dettaglio")
    public ClienteDTO findById(@RequestParam(name = "idCliente") Long id) {
        return clienteService.findById(id);
    }

    @GetMapping("/lista")
    public List<ClienteDTO> findAll(@RequestParam(name = "idHotel") Long idHotel) {
        return clienteService.findAll(idHotel);
    }

    //TODO rimuovere
    @PutMapping("/update")
    public ClienteDTO update(@RequestBody ClienteDTO clienteDTO) {
        return clienteService.update(clienteDTO);
    }

    //TODO rimuovere
    @DeleteMapping("/delete")
    public Boolean delete(@RequestParam(name = "idCliente") Long idCliente) {
        return clienteService.delete(idCliente);
    }

    //find all Cienti startWith nome or cognome
    //TODO idHOtel non ha senso visto che i clienti si troano in tutti gli hotel
    @GetMapping("/searchNomeCognome")
    public List<ClienteDTO> findAllByNomeCognome(@RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("idHotel") Long idHotel) {
        return clienteService.findAllByNomeCognome(nome, cognome, idHotel);
    }

    //TODO aggiornare api file da /create a /register
    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody ClienteDTO clienteDTO) throws Exception {
        return ResponseEntity.ok(clienteService.create(clienteDTO));
    }

    @GetMapping("/dettaglio/username")
    public ClienteDTO findClienteByUsername(@RequestParam("username") String username){
        return clienteService.findClienteByUsername(username);
    }


}
