package com.ignaziopicciche.albergo.controller;

import com.ignaziopicciche.albergo.dto.ClienteDTO;
import com.ignaziopicciche.albergo.enums.Ruolo;
import com.ignaziopicciche.albergo.security.AuthenticationRequest;
import com.ignaziopicciche.albergo.service.AutenticazioneService;
import com.ignaziopicciche.albergo.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final ClienteService clienteService;
    private final AutenticazioneService autenticazioneService; //UserService

    public ClienteController(ClienteService clienteService, AutenticazioneService autenticazioneService) {
        this.clienteService = clienteService;
        this.autenticazioneService = autenticazioneService;
    }

    @GetMapping("/dettaglio")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ClienteDTO findById(@RequestParam(name = "idCliente") Long id){
        return clienteService.findById(id);
    }

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ClienteDTO> findAll(@RequestParam(name = "idHotel") Long idHotel){
        return clienteService.findAll(idHotel);
    }

    //TODO rimuovere
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ClienteDTO update(@RequestBody ClienteDTO clienteDTO){
        return clienteService.update(clienteDTO);
    }

    //TODO rimuovere
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Boolean delete(@RequestParam(name = "idCliente") Long idCliente){
        return clienteService.delete(idCliente);
    }

    //find all Cienti startWith nome or cognome
    @GetMapping("/searchNomeCognome")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ClienteDTO> findAllByNomeCognome(@RequestParam("nome") String nome, @RequestParam("cognome") String cognome, @RequestParam("idHotel") Long idHotel){
        return clienteService.findAllByNomeCognome(nome, cognome, idHotel);
    }

    //TODO convertire in register
    //TODO aggiornare api file da /create a /register
    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody ClienteDTO clienteDTO) throws Exception {
        return ResponseEntity.ok(clienteService.create(clienteDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return autenticazioneService.createAuthenticationToken(authenticationRequest, Ruolo.ROLE_CLIENT);
    }

}
