package com.ignaziopicciche.albergo.controller;


import com.ignaziopicciche.albergo.enums.Ruolo;
import com.ignaziopicciche.albergo.security.AuthenticationRequest;
import com.ignaziopicciche.albergo.dto.AmministratoreDTO;
import com.ignaziopicciche.albergo.service.AmministratoreService;
import com.ignaziopicciche.albergo.service.AutenticazioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/amministratore")
public class AmministratoreController {

    private final AmministratoreService amministratoreService;
    private final AutenticazioneService autenticazioneService; //UserService

    public AmministratoreController(AmministratoreService amministratoreService, AutenticazioneService autenticazioneService) {
        this.amministratoreService = amministratoreService;
        this.autenticazioneService = autenticazioneService;
    }


    //Endpoint di autenticazione che prende ID utente e password e poi ritorna a JWT
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return autenticazioneService.createAuthenticationToken(authenticationRequest, Ruolo.ROLE_ADMIN);
    }


    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody AmministratoreDTO amministratoreDTO) {
        return ResponseEntity.ok(amministratoreService.create(amministratoreDTO));
    }


}
